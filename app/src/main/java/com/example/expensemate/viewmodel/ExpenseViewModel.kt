package com.example.expensemate.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemate.data.Expense
import com.example.expensemate.data.ExpenseDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
                dao.insertExpense(expense.copy(userId = uid))
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                dao.updateExpense(expense.copy(userId = uid))
            }
        }
    }
}
