package com.example.auth.hashing

import org.apache.commons.codec.digest.DigestUtils

class SHA256HashingService : HashingService {
    override fun generateHash(value: String): String =
        DigestUtils.sha256Hex(value)

    override fun verify(value: String, hashedPassword: String): Boolean =
        DigestUtils.sha256Hex(value) == hashedPassword
}