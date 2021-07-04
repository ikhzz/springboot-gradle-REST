package com.ikhz.simpleCrudApplication.utility

import io.jsonwebtoken.Jwts
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class EncryptionHelper(
    @Value("\${secret.password}")
    private val passSecret: String,

    @Value("\${secret.token}")
    val tokenSecret: String
) {

    private val encryption = StandardPBEStringEncryptor()

    init {
        // set password key
        encryption.setPassword(passSecret)
    }

    fun encryption(password: String): String {
        return encryption.encrypt(password)
    }

    fun decryption(password: String): String {
        return encryption.decrypt(password)
    }

    fun tokenDecryption(token: String): String {
        return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).body.subject
    }
}