package com.example.repository.dao

import com.example.repository.model.User
import java.util.*

interface UserDAO {
    suspend fun addUser(email: String, password: String): User
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User): Boolean
    suspend fun findUserById(userId: UUID): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun getAllUsers(): List<User>
}