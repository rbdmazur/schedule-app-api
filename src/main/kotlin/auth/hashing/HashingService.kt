package com.example.auth.hashing

interface HashingService {
    fun generateHash(value: String): String
    fun verify(value: String, hashedPassword: String): Boolean
}