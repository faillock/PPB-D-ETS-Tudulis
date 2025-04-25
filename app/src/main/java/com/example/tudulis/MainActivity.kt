package com.example.tudulis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tudulis.ui.theme.TudulisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TudulisTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Tudulis(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { LamanTambahUbah() }) {
                            Text("+")
                        }
                    }
                ) { innerPadding ->
                    ToDoScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LamanUtama(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Statistik
        Text(
            text = "Statistik",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Cari
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Cari urusan...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Tag
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Kerja", "Pribadi", "Mendesak").forEach { tag ->
                AssistChip(
                    onClick = { /* TODO: Filter by tag */ },
                    label = { Text(tag) }
                )
            }
        }

        // Belum Selesai
        Text(
            text = "Urusan Belum Selesai",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        TaskList(tasks = listOf("Belanja", "Buat janji temu", "ETS PPB"))

        // Selesai
        Text(
            text = "Finished Tasks",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        TaskList(tasks = listOf("Bersih2", "Cuci baju"), finished = true)
    }
}

@Composable
fun LamanTambahUbah(
    modifier: Modifier = Modifier,
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var taskText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("None") }

    // Date picker dialog
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                dueDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            },
            year,
            month,
            day
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Urusan
        OutlinedTextField(
            value = taskText,
            onValueChange = { taskText = it },
            label = { Text("Task") },
            modifier = Modifier.fillMaxWidth()
        )

        // PIlih tanggal
        OutlinedButton(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (dueDate.isNotEmpty()) "Due: $dueDate" else "Select Due Date")
        }

        // Pilih tag
        var expanded by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tag: $selectedTag")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Work", "Personal", "Urgent").forEach { tag ->
                    DropdownMenuItem(
                        text = { Text(tag) },
                        onClick = {
                            selectedTag = tag
                            expanded = false
                        }
                    )
                }
            }
        }

        // Simpan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { LamanUtama() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Kembali")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text("Simpan")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TudulisPreview() {
    TudulisTheme {
        LamanUtama()
    }
}