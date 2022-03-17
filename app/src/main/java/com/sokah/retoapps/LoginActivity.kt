package com.sokah.retoapps

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sokah.retoapps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{

            if(binding.inputEmail.editText?.text!!.toString().isNotEmpty()&&binding.inputPassword.editText?.text!!.toString().isNotEmpty()){

                Login()
            }

            else{

                Toast.makeText(this,"Por favor complete todos los campos",Toast.LENGTH_LONG).show()
            }

        }

    }

    fun Login(){

        val email = binding.inputEmail.editText?.text!!.toString()
        val password = binding.inputPassword.editText?.text!!.toString()
        if(email.contentEquals(USER1_EMAIL)|| email.contentEquals(USER2_EMAIL)){

            if(password.contentEquals(USERPASS)){

                var intent = Intent(this,MainActivity::class.java)

                startActivity(intent)
                finish()
//                val preferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
//                val editor = preferences.edit()
//                editor.apply {
//
//                    putString("USER_EMAIL",email)
//                }
            }

        }


    }
}