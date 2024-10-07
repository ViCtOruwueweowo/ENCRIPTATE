package com.example.myapplication

import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

class MainActivity : AppCompatActivity() {

    private lateinit var inputText: EditText
    private lateinit var encryptButton: Button
    private lateinit var decryptButton: Button
    private lateinit var outputText: TextView

    private var secretKey: Key? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.inputText)
        encryptButton = findViewById(R.id.encryptButton)
        decryptButton = findViewById(R.id.decryptButton)
        outputText = findViewById(R.id.outputText)
        secretKey = generateKey()

        encryptButton.setOnClickListener {
            val textToEncrypt = inputText.text.toString()
            if (textToEncrypt.isNotEmpty()) {
                val encryptedText = encrypt(textToEncrypt, secretKey!!)
                outputText.text = encryptedText
            }
        }

        decryptButton.setOnClickListener {
            val textToDecrypt = inputText.text.toString()
            if (textToDecrypt.isNotEmpty()) {
                val decryptedText = decrypt(textToDecrypt, secretKey!!)
                outputText.text = "$decryptedText"
            }
        }
    }

    // Metodo para la llave de otra encriptacion AES
    private fun generateKey(): Key {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(128)
        return keyGen.generateKey()
    }

    // Encriptación
    private fun encrypt(data: String, key: Key): String {
        val base64Encoded = Base64.encodeToString(data.toByteArray(), Base64.DEFAULT)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedValue = cipher.doFinal(base64Encoded.toByteArray())
        val base64Encrypted = Base64.encodeToString(encryptedValue, Base64.DEFAULT)
        return applyCaesarCipher(base64Encrypted, 5)
    }

    // Desencriptación
    private fun decrypt(data: String, key: Key): String {
        val base64Encrypted = applyCaesarCipher(data, -5)  // Desplazamiento inverso de 5
        val decodedValue = Base64.decode(base64Encrypted, Base64.DEFAULT)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decryptedValue = cipher.doFinal(decodedValue)
        val base64Decoded = String(decryptedValue)
        return String(Base64.decode(base64Decoded, Base64.DEFAULT))
    }
    private fun applyCaesarCipher(input: String, shift: Int): String {
        val result = StringBuilder()
        for (char in input) {
            val shiftedChar = char + shift
            result.append(shiftedChar)
        }
        return result.toString()
    }
}
