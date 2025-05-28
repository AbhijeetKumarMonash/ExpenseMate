package com.example.expensemate.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    // ✅ Only return expenses that belong to a specific user
    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY id DESC")
    fun getAllExpenses(userId: String): Flow<List<Expense>>

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("UPDATE expenses SET firestoreId = :fsId WHERE id = :localId")
    suspend fun updateFirestoreId(localId: Int, fsId: String)

    @Query("SELECT * FROM expenses WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getByFirestoreId(firestoreId: String): Expense?

    @Query("SELECT * FROM expenses WHERE firestoreId = :firestoreId LIMIT 1")
    suspend fun getExpenseByFirestoreId(firestoreId: String): Expense?



}
