package com.example.tudulis

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tudulis.ui.theme.TudulisTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showForm by remember { mutableStateOf(false) }

            TudulisTheme {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showForm = true }) {
                            Text("+")
                        }
                    }
                ) { innerPadding ->
                    if (showForm) {
                        LamanTambahUbah(
                            modifier = Modifier.padding(innerPadding),
                            onSave = { showForm = false }
                        )
                    } else {
                        LamanUtama(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LamanUtama(modifier: Modifier = Modifier) {
    var cariUrusan by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Statistik
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Cyan)
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = "Statistik",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.LightGray, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Belum Selesai", fontWeight = FontWeight.Bold)
                            Text("var")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.LightGray, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Sudah Selesai", fontWeight = FontWeight.Bold)
                            Text("var")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cari
        TextField(
            value = cariUrusan,
            onValueChange = { cariUrusan = it },
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
            text = "Urusan Belum Selesai    v",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
         DaftarUrusan(tasks = listOf("Belanja", "Buat janji temu", "ETS PPB"))

        // Selesai
        Text(
            text = "Urusan Selesai      v",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DaftarUrusan(tasks = listOf("Bersih2", "Cuci baju"), finished = true)
    }
}

@Composable
fun LamanTambahUbah(
    modifier: Modifier = Modifier,
    onSave: () -> Unit = {}
) {
    var showLamanUtama by remember { mutableStateOf(false) }

    if (showLamanUtama) {
        LamanUtama()
    } else {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var taskText by remember { mutableStateOf("") }
        var dueDate by remember { mutableStateOf("") }
        var selectedTag by remember { mutableStateOf("None") }

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
            TextField(
                value = taskText,
                onValueChange = { taskText = it },
                label = { Text("Urusan apa kali ini?") },
                modifier = Modifier.fillMaxWidth()
            )

            // Pilih tanggal
            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (dueDate.isNotEmpty()) "Terakhir: $dueDate" else "Kapan terakhir?")
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
                    listOf("Kerja", "Pribadi", "Mendesak").forEach { tag ->
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
                    onClick = { showLamanUtama = true },
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
}

@Composable
fun DaftarUrusan(tasks: List<String>, finished: Boolean = false) {
    var konfirmasiHapus by remember { mutableStateOf(false) }

    Column {
        tasks.forEach { task ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = task)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {  }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }

                        IconButton(onClick = { konfirmasiHapus = true  }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (konfirmasiHapus) {
        DialogKonfirmasiHapus(
            onDismiss = { konfirmasiHapus = false },
            onConfirmDelete = {
                konfirmasiHapus = false
            }
        )
    }
}

@Composable
fun DialogKonfirmasiHapus(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text("Yakin ingin menghapus urusan?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmDelete()
                }
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TudulisPreview() {
    TudulisTheme {
        LamanUtama()
    }
}