package com.example.expensemate.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemate.RetrofitClient
import com.example.expensemate.data.Expense
import com.example.expensemate.data.ExpenseDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.State


class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    init {
        // 🔍 Log for debugging and set userId
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            val uid = auth.currentUser?.uid
            Log.d("ExpenseViewModel", "Auth state changed. UID: $uid")
            _userId.value = uid
            syncFromFirestore(uid)
        }
    }
    // In ExpenseViewModel.kt
    private val _dailyAdvice = mutableStateOf("Loading tip...")
    val dailyAdvice: State<String> = _dailyAdvice

    fun fetchDailyAdvice() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getAdvice()
                _dailyAdvice.value = response.slip.advice
            } catch (e: Exception) {
                _dailyAdvice.value = "Couldn't load tip today!"
            }
        }
    }

    // ✅ Observe expenses only when userId is non-null
    val expenses: StateFlow<List<Expense>> = _userId
        .filterNotNull()
        .flatMapLatest { uid ->
            dao.getAllExpenses(uid)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val docRef = Firebase.firestore.collection("expenses").document()
                val expenseWithIds = expense.copy(userId = uid, firestoreId = docRef.id)

                dao.insertExpense(expenseWithIds)

                docRef.set(expenseWithIds)
                    .addOnSuccessListener {
                        Log.d("ExpenseViewModel", "✅ Synced to Firestore with ID ${docRef.id}")
                    }
                    .addOnFailureListener {
                        Log.e("ExpenseViewModel", "❌ Firestore sync failed", it)
                    }
            }
        }
    }



    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            dao.deleteExpense(expense)

            if (expense.firestoreId.isNotEmpty()) {
                Firebase.firestore.collection("expenses")
                    .document(expense.firestoreId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("ExpenseViewModel", "🗑️ Firestore expense deleted")
                    }
                    .addOnFailureListener {
                        Log.e("ExpenseViewModel", "❌ Firestore delete failed", it)
                    }
            }
        }
    }

    fun syncFromFirestore(userId: String?) {
        if (userId.isNullOrEmpty()) return

        Firebase.firestore.collection("expenses")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                Log.d("FirestoreSync", "Fetched ${result.size()} documents")
                viewModelScope.launch {
                    result.forEach { doc ->
                        try {
                            val remoteExpense = doc.toObject(Expense::class.java)
                            Log.d("FirestoreSync", "Expense: $remoteExpense")
                            val existing = dao.getExpenseByFirestoreId(remoteExpense.firestoreId)
                            if (existing == null) {
                                dao.insertExpense(remoteExpense.copy(id = 0)) // Room will generate ID
                            }
                        } catch (e: Exception) {
                            Log.e("FirestoreSync", "❌ Failed to parse document", e)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreSync", "❌ Error syncing: ${e.message}", e)
            }
    }






    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val updated = expense.copy(userId = uid)
                dao.updateExpense(updated)

                if (updated.firestoreId.isNotEmpty()) {
                    Firebase.firestore.collection("expenses")
                        .document(updated.firestoreId)
                        .set(updated)
                        .addOnSuccessListener {
                            Log.d("ExpenseViewModel", "✅ Firestore updated")
                        }
                        .addOnFailureListener {
                            Log.e("ExpenseViewModel", "❌ Firestore update failed", it)
                        }
                }
            }
        }
    }


}
