package com.example.tudulis

import android.app.DatePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.text.Layout
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tudulis.ui.theme.TudulisTheme
import java.util.Calendar

data class Task(
    val name: String,
    val dueDate: String,
    val tag: String,
    val isDone: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showForm by remember { mutableStateOf(false) }
            val tasks = remember { mutableStateListOf<Task>() }
            var editingTaskIndex by remember { mutableStateOf<Int?>(null) }

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
                            tasks = tasks,
                            editingIndex = editingTaskIndex,
                            onSave = { task ->
                                if (editingTaskIndex != null) {
                                    tasks[editingTaskIndex!!] = task
                                    editingTaskIndex = null
                                } else {
                                    tasks.add(task)
                                }
                                showForm = false
                            },
                            onCancel = {
                                showForm = false
                                editingTaskIndex = null
                            }
                        )
                    } else {
                        LamanUtama(
                            modifier = Modifier.padding(innerPadding),
                            tasks = tasks,
                            onEdit = { task ->
                                editingTaskIndex = tasks.indexOf(task)
                                showForm = true
                            },
                            onDelete = { task ->
                                tasks.remove(task)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LamanUtama(
    modifier: Modifier = Modifier,
    tasks: SnapshotStateList<Task>,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    var cariUrusan by remember { mutableStateOf("") }
    val belumSelesaiCount = tasks.count { !it.isDone }
    val sudahSelesaiCount = tasks.count { it.isDone }

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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Statistik",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.LightGray, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("  Belum Selesai", fontWeight = FontWeight.Bold)
                            Text("$belumSelesaiCount", fontSize = 32.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.LightGray, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("  Sudah Selesai", fontWeight = FontWeight.Bold)
                            Text("$sudahSelesaiCount", fontSize = 32.sp)
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
        DaftarUrusan(
            tasks = tasks.filter { !it.isDone },
            onToggleDone = { task ->
                val index = tasks.indexOf(task)
                if (index != -1) {
                    tasks[index] = task.copy(isDone = true)
                }
            },
            onEdit = onEdit,
            onDelete = onDelete
        )

        // Selesai
        Text(
            text = "Urusan Selesai      v",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DaftarUrusan(
            tasks = tasks.filter { it.isDone },
            onToggleDone = { task ->
                val index = tasks.indexOf(task)
                if (index != -1) {
                    tasks[index] = task.copy(isDone = false)
                }
            },
            onEdit = onEdit,
            onDelete = onDelete
        )
    }
}

@Composable
fun LamanTambahUbah(
    modifier: Modifier = Modifier,
    tasks: SnapshotStateList<Task>,
    editingIndex: Int? = null,
    onSave: (Task) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val existingTask = editingIndex?.let { tasks[it] }

    var taskText by remember { mutableStateOf(existingTask?.name ?: "") }
    var dueDate by remember { mutableStateOf(existingTask?.dueDate ?: "") }
    var selectedTag by remember { mutableStateOf(existingTask?.tag ?: "None") }

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

        // Kembali / Simpan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Kembali")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    val task = Task(taskText, dueDate, selectedTag, existingTask?.isDone ?: false)
                    onSave(task)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Simpan")
            }
        }

    }
}

@Composable
fun DaftarUrusan(
    tasks: List<Task>,
    onToggleDone: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onEdit: (Task) -> Unit
) {
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

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
                    Row(modifier = Modifier.weight(1f)) {
                        Row {
                            TextButton(onClick = { onToggleDone(task) }) {
                                Text(if (task.isDone) "X" else "O")
                            }
                        }
                        Column {
                            Text(text = task.name)
                            Row {
                                Text(text = task.tag + " | ")
                                Text(text = task.dueDate)
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { onEdit(task) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }

                        IconButton(onClick = { taskToDelete = task }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    taskToDelete?.let {
        DialogKonfirmasiHapus(
            onDismiss = { taskToDelete = null },
            onConfirmDelete = {
                onDelete(it)
                taskToDelete = null
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

//@Preview(showBackground = true)
//@Composable
//fun TudulisPreview() {
//    TudulisTheme {
//        LamanUtama()
//    }
//}