package com.kelompok.rebook.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompok.rebook.data.Book
import com.kelompok.rebook.data.BookStatus
import com.kelompok.rebook.data.MockLocalDatabase
import com.kelompok.rebook.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBackClick: () -> Unit,
    onUploadSuccess: () -> Unit,
    viewModel: BookViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var publishYear by remember { mutableStateOf("") }
    var coverUrl by remember { mutableStateOf<String?>(null) }
    
    var isSearchingMetadata by remember { mutableStateOf(false) }
    var isNotFound by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Buku") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Text(text = if (isDarkTheme) "☀️" else "🌙")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Otomatis Isi Data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Masukkan judul atau ISBN untuk mengisi metadata secara otomatis dari API.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            if (isNotFound) isNotFound = false 
                        },
                        placeholder = { Text("Judul atau ISBN...") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (isSearchingMetadata) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            } else {
                                IconButton(onClick = {
                                    if (searchQuery.isBlank()) return@IconButton
                                    scope.launch {
                                        isSearchingMetadata = true
                                        isNotFound = false
                                        val metadata = viewModel.fetchMetadata(searchQuery)
                                        if (metadata != null) {
                                            title = metadata.title
                                            author = metadata.author
                                            description = metadata.description
                                            publishYear = metadata.publishYear ?: ""
                                            subject = metadata.subject ?: ""
                                            coverUrl = metadata.coverUrl
                                        } else {
                                            isNotFound = true
                                        }
                                        isSearchingMetadata = false
                                    }
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                }
                            }
                        },
                        isError = isNotFound,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    if (isNotFound) {
                        Text(
                            text = "Buku tidak ditemukan. Silakan isi data secara manual.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }
                }
            }

            HorizontalDivider()

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Buku") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Penulis") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Kategori / Mata Kuliah") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = publishYear,
                onValueChange = { publishYear = it },
                label = { Text("Tahun Terbit") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank()) {
                        val newBook = Book(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            author = author,
                            description = description,
                            coverUrl = coverUrl ?: "https://via.placeholder.com/300x450.png?text=No+Cover",
                            subject = subject,
                            publishYear = publishYear,
                            ownerName = "Pengguna Saya",
                            ownerWhatsApp = "628123456789",
                            status = BookStatus.AVAILABLE
                        )
                        MockLocalDatabase.myUploads.add(newBook)
                        onUploadSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && author.isNotBlank()
            ) {
                Text("Simpan & Publikasikan")
            }
        }
    }
}
