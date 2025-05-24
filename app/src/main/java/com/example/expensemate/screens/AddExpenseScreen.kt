package com.example.expensemate.screens

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensemate.data.Expense
import java.util.*
import com.example.expensemate.viewmodel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Select Category") }
    var otherCategoryName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var addToCalendar by remember { mutableStateOf(false) }
    var billImageUri by remember { mutableStateOf<Uri?>(null) }
    var editingExpenseId by remember { mutableStateOf<Int?>(null) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val context = LocalContext.current
    val expenses by viewModel.expenses.collectAsState()
    val categories = listOf("Food", "Transport", "Bills", "Shopping", "Others")

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> billImageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Expense", fontSize = 22.sp, color = Color(0xFFFFD700),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                // Amount Input
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (\$)", color = Color(0xFFFFD700)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color(0xFFFFD700),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color(0xFFFFD700),
                        unfocusedLabelColor = Color(0xFFFFD700)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Category dropdown
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    TextField(
                        value = selectedCategory,
                        onValueChange = {},
                        label = { Text("Category", color = Color(0xFFFFD700)) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable { expanded = true },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color(0xFFFFD700),
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color(0xFFFFD700),
                            unfocusedLabelColor = Color(0xFFFFD700)
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedCategory == "Others") {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = otherCategoryName,
                        onValueChange = { otherCategoryName = it },
                        label = { Text("Enter custom category", color = Color(0xFFFFD700)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color(0xFFFFD700),
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color(0xFFFFD700),
                            unfocusedLabelColor = Color(0xFFFFD700)
                        )
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, day -> selectedDate = "$day/${month + 1}/$year" },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
                ) {
                    Text(if (selectedDate.isEmpty()) "Pick a Date" else selectedDate)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
                ) {
                    Text("Upload Bill Image")
                }

                Spacer(modifier = Modifier.height(16.dp))

                billImageUri?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Bill Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(150.dp).fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Add to Calendar?", color = Color.White, modifier = Modifier.weight(1f))
                    Switch(checked = addToCalendar, onCheckedChange = { addToCalendar = it })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val category = if (selectedCategory == "Others") otherCategoryName else selectedCategory
                        if (amount.isNotEmpty() && category.isNotEmpty() && selectedDate.isNotEmpty()) {
                            val expense = Expense(
                                id = editingExpenseId ?: 0,
                                amount = amount.toDouble(),
                                category = category,
                                date = selectedDate,
                                userId = userId ?: ""
                            )
                            if (editingExpenseId != null) {
                                viewModel.updateExpense(expense)
                                editingExpenseId = null
                            } else {
                                viewModel.addExpense(expense)
                            }
                            amount = ""
                            selectedCategory = "Select Category"
                            otherCategoryName = ""
                            selectedDate = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
                ) {
                    Text("Save Expense", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    "Your Expenses:",
                    color = Color(0xFFFFD700),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(expenses) { exp ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💰 \$${exp.amount}  📅 ${exp.date}  📂 ${exp.category}",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(
                                onClick = {
                                    amount = exp.amount.toString()
                                    selectedCategory = if (exp.category in categories) exp.category else "Others"
                                    otherCategoryName = if (selectedCategory == "Others") exp.category else ""
                                    selectedDate = exp.date
                                    editingExpenseId = exp.id
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text("Edit")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.deleteExpense(exp) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        }
                    }
                }
            }


        }
    }
}
