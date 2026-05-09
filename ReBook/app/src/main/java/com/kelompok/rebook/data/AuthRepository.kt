package com.kelompok.rebook.data

object AuthRepository {
    private val registeredUsers = mutableMapOf(
        "user@students.unila.ac.id" to UserData("Mahasiswa Unila", "user123", "Mahasiswa", "6281234567891")
    )

    private var currentUserEmail: String? = null

    fun register(name: String, email: String, pass: String, status: String, phone: String): Boolean {
        if (registeredUsers.containsKey(email)) return false
        registeredUsers[email] = UserData(name, pass, status, phone)
        return true
    }

    fun login(email: String, pass: String): Boolean {
        val isValid = registeredUsers[email]?.password == pass
        if (isValid) currentUserEmail = email
        return isValid
    }

    fun logout() {
        currentUserEmail = null
    }

    fun getCurrentUser(): UserProfile? {
        val email = currentUserEmail ?: return null
        val data = registeredUsers[email] ?: return null
        return UserProfile(data.name, email, data.status, data.phone)
    }

    fun updateProfile(name: String, phone: String, status: String): Boolean {
        val email = currentUserEmail ?: return false
        val existingData = registeredUsers[email] ?: return false
        registeredUsers[email] = existingData.copy(name = name, phone = phone, status = status)
        return true
    }
}

data class UserData(
    val name: String,
    val password: String,
    val status: String,
    val phone: String
)

data class UserProfile(
    val name: String,
    val email: String,
    val status: String,
    val phone: String
)
