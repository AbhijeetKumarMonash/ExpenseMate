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
}
