package com.example.cmsc436_nda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cmsc436_nda.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.util.Log
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
        firebaseAuth= requireNotNull(FirebaseAuth.getInstance());
        binding.login.setOnClickListener { authenticate()  }
        binding.register.setOnClickListener { register() }
        binding.skip.setOnClickListener { startActivity(Intent(this,HomeActivity::class.java)) }
        setContentView(binding.root);
    }
    private fun authenticate(){
        Log.e(TAG,"Log In Button Hit")
        firebaseAuth.signInWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).
        addOnCompleteListener {
            attempt->
            Log.e(TAG,"FireBase api returned")
            if(attempt.isSuccessful){
                startActivity(Intent(this,HomeActivity::class.java))
            }else{
                Toast.makeText(this,"Invalid Email or Password",Toast.LENGTH_SHORT)
                    .show()
            }

        };
    }
    private fun register(){
        Log.e(TAG, "Register button hit")
        firebaseAuth.createUserWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString())
            .addOnCompleteListener{
                task->
                Log.e(TAG,"FireBase api returned")
                if(task.isSuccessful){
                    startActivity(Intent(this,HomeActivity::class.java));
                }else{
                    Toast.makeText(this, "Please try a different Email/Password",Toast.LENGTH_SHORT);
                }

            }
    }
    companion object{
        const val TAG= "Authenitcation Test"
    }
}