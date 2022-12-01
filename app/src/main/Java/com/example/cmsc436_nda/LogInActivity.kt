package com.example.cmsc436_nda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cmsc436_nda.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.Toast
import com.google.firebase.ktx.initialize
import com.google.firebase.provider.FirebaseInitProvider

class LogInActivity:AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth;
    private lateinit var binding:LoginBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        FirebaseInitProvider();
        binding=LoginBinding.inflate(layoutInflater);
        firebaseAuth= FirebaseAuth.getInstance();
        binding.login.setOnClickListener { authenticate()  }
        binding.register.setOnClickListener { register() }
        setContentView(binding.root);
    }
    private fun authenticate(){
        firebaseAuth.signInWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).
        addOnCompleteListener {
            attempt->
            if(attempt.isSuccessful){
                var intent=Intent(this,HomeActivity::class.java);
                startActivity(intent)
            }else{
                Toast.makeText(this,"Invalid Email or Password",Toast.LENGTH_SHORT)
                    .show()
            }

        };
    }
    private fun register(){
        firebaseAuth.createUserWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString())
            .addOnCompleteListener{
                task->
                if(task.isSuccessful){
                    val intent=Intent(this,HomeActivity::class.java)
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Please try a different Email/Password",Toast.LENGTH_SHORT);
                }

            }
    }
}