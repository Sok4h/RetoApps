package com.sokah.retoapps.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.USER1_EMAIL
import com.sokah.retoapps.USER2_EMAIL
import com.sokah.retoapps.USERPASS
import com.sokah.retoapps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (prefs.getLoggedUser() != null) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {

            if (binding.inputEmail.editText?.text!!.toString()
                    .isNotEmpty() && binding.inputPassword.editText?.text!!.toString().isNotEmpty()
            ) {

                Login()
            } else {

                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_LONG)
                    .show()
            }

        }

    }

    fun Login() {

        val email = binding.inputEmail.editText?.text!!.toString()
        val password = binding.inputPassword.editText?.text!!.toString()
        if (email.contentEquals(USER1_EMAIL) || email.contentEquals(USER2_EMAIL)) {

            val intent = Intent(this, MainActivity::class.java)
            if (password.contentEquals(USERPASS)) {
                if (email.contentEquals(USER1_EMAIL)) {
                    prefs.saveLoggedUser("USER_1")
                } else {
                    prefs.saveLoggedUser("USER_2")
                }

            }
            startActivity(intent)
            finish()

        }else{

            Toast.makeText(this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show()
        }

    }


}
