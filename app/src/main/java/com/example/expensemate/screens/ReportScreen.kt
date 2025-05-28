package com.example.expensemate.screens

import android.graphics.Color as AndroidColor
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensemate.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavHostController,
    viewModel: ExpenseViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val expenses = viewModel.expenses.collectAsState().value
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    val monthlyExpenses = expenses.filter {
        runCatching {
            val d = sdf.parse(it.date)!!
            Calendar.getInstance().apply { time = d }.get(Calendar.MONTH) == currentMonth
        }.getOrDefault(false)
    }

    val total = monthlyExpenses.sumOf { it.amount }
    val byCategory = monthlyExpenses.groupBy { it.category }.mapValues { it.value.sumOf { it.amount } }
    val dailyTrend = monthlyExpenses.groupBy { it.date }.mapValues { it.value.sumOf { it.amount } }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Expense Report", fontSize = 22.sp, color = Color(0xFFFFD700))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("This Month's Expenses", color = Color(0xFFFFD700), fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("$${"%.2f".format(total)}", color = Color.White, fontSize = 28.sp)
                }
            }

            Text("Category Distribution", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        PieChart(ctx).apply {
                            setUsePercentValues(true)
                            description.isEnabled = false
                            isDrawHoleEnabled = true
                            holeRadius = 40f
                            setEntryLabelColor(AndroidColor.WHITE)
                        }
                    },
                    update = { chart ->
                        val entries = byCategory.map { (cat, sum) -> PieEntry(sum.toFloat(), cat) }
                        val set = PieDataSet(entries, "").apply {
                            colors = ColorTemplate.MATERIAL_COLORS.toList()
                            valueTextColor = AndroidColor.WHITE
                            valueTextSize = 12f
                        }
                        chart.data = PieData(set)
                        chart.invalidate()
                    }
                )
            }

            Text("Category Trend", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        BarChart(ctx).apply {
                            description.isEnabled = false
                            setDrawValueAboveBar(true)
                            axisRight.isEnabled = false
                            xAxis.isEnabled = true
                            legend.isEnabled = false
                            axisLeft.textColor = AndroidColor.WHITE
                        }
                    },
                    update = { chart ->
                        val entries = byCategory.entries.mapIndexed { idx, (cat, sum) ->
                            BarEntry(idx.toFloat(), sum.toFloat())
                        }
                        val set = BarDataSet(entries, "").apply {
                            colors = ColorTemplate.COLORFUL_COLORS.toList()
                            valueTextColor = AndroidColor.WHITE
                            valueTextSize = 12f
                        }
                        chart.data = BarData(set).apply { barWidth = 0.5f }
                        chart.xAxis.apply {
                            valueFormatter = IndexAxisValueFormatter(byCategory.keys.toList())
                            textColor = AndroidColor.WHITE
                            granularity = 1f
                            setDrawLabels(true)
                        }
                        chart.invalidate()
                    }
                )
            }

            Text("Spending Over Time", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        BarChart(ctx).apply {
                            description.isEnabled = false
                            axisRight.isEnabled = false
                            axisLeft.textColor = AndroidColor.WHITE
                            xAxis.textColor = AndroidColor.WHITE
                            legend.isEnabled = false
                        }
                    },
                    update = { chart ->
                        val entries = dailyTrend.entries.mapIndexed { idx, (date, sum) ->
                            BarEntry(idx.toFloat(), sum.toFloat())
                        }
                        val set = BarDataSet(entries, "").apply {
                            colors = ColorTemplate.MATERIAL_COLORS.toList()
                            valueTextColor = AndroidColor.WHITE
                            valueTextSize = 12f
                        }
                        chart.data = BarData(set).apply { barWidth = 0.5f }
                        chart.xAxis.apply {
                            valueFormatter = IndexAxisValueFormatter(dailyTrend.keys.toList())
                            granularity = 1f
                            setDrawLabels(true)
                        }
                        chart.invalidate()
                    }
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Text(
                    "💡 Tip: Regularly tracking your expenses helps you save more!",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
