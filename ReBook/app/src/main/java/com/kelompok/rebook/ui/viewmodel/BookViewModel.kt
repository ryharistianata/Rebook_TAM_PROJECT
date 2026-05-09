package com.kelompok.rebook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok.rebook.data.ApiResult
import com.kelompok.rebook.data.Book
import com.kelompok.rebook.data.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    private val _booksState = MutableStateFlow<ApiResult<List<Book>>>(ApiResult.Loading)
    val booksState: StateFlow<ApiResult<List<Book>>> = _booksState

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook

    private val _fullDescription = MutableStateFlow<String?>(null)
    val fullDescription: StateFlow<String?> = _fullDescription

    private val _wishlist = MutableStateFlow<List<Book>>(emptyList())
    val wishlist: StateFlow<List<Book>> = _wishlist

    init {
        searchBooks("science")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _booksState.value = ApiResult.Loading
            _booksState.value = BookRepository.searchBooks(query)
        }
    }

    fun selectBook(book: Book) {
        _selectedBook.value = book
        _fullDescription.value = null
        fetchFullDescription(book.id)
    }

    private fun fetchFullDescription(workId: String) {
        viewModelScope.launch {
            if (!workId.contains("-")) { 
                val result = BookRepository.getBookDetail(workId)
                if (result is ApiResult.Success) {
                    _fullDescription.value = result.data["description"]
                }
            }
        }
    }

    suspend fun fetchMetadata(query: String): Book? {
        val result = BookRepository.searchBooks(query, limit = 1)
        return if (result is ApiResult.Success && result.data.isNotEmpty()) {
            result.data.first()
        } else null
    }

    fun toggleWishlist(book: Book) {
        val current = _wishlist.value.toMutableList()
        if (current.any { it.id == book.id }) {
            current.removeAll { it.id == book.id }
        } else {
            current.add(book)
        }
        _wishlist.value = current
    }

    fun isInWishlist(bookId: String): Boolean {
        return _wishlist.value.any { it.id == bookId }
    }
}
