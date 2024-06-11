package com.example.myappdecomida.actividades

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myappdecomida.MainActivity
import com.example.myappdecomida.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class ActivitySignUp : AppCompatActivity(){

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Variables del signup
        val names : TextView = findViewById(R.id.signup_name)
        val email : TextView = findViewById(R.id.signup_email)
        val user : TextView = findViewById(R.id.signup_username)
        val spassword : TextView = findViewById(R.id.signup_password)
        val spassword2 : TextView = findViewById(R.id.signup_password2)
        val signupbutton : Button = findViewById(R.id.signup_button)

        signupbutton.setOnClickListener {
            val pass1 = spassword.text.toString()
            val pass2 = spassword2.text.toString()

            if (pass1 == pass2) {
                if (isValidPassword(pass1)) {
                    createAccount(email.text.toString(), pass1)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Mensaje de advertencia")
                    builder.setMessage("La contraseña no cumple con los requisitos, debe contener una mayúscula, un número, un caracter especial y debe tener un máximo de 8 caracteres")
                    builder.setPositiveButton("OK", null)
                    val dialog = builder.create()
                    dialog.show()
                    spassword.requestFocus()
                }
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                spassword.requestFocus()
            }
        }
        firebaseAuth = Firebase.auth
    }

    fun isValidPassword(password: String): Boolean {
        val pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[!\\?])[A-Za-z\\d!\\?]{1,8}\$")
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun createAccount(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    Toast.makeText(baseContext, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("Ocurrió el siguiente error: ${task.exception?.message}")
                    builder.setPositiveButton("OK", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
    }

    fun onLoginLinkClicked(view: View) {
        // Código que se ejecutará cuando se haga clic en el TextView para iniciar sesión
        val intent = Intent(this,  MainActivity::class.java)
        startActivity(intent)
    }
}