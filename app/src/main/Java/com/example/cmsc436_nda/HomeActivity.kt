package com.example.cmsc436_nda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.example.cmsc436_nda.databinding.ActivityHomeBinding
import android.content.Intent
import android.util.Log
import android.widget.*
import com.example.cmsc436_nda.com.example.cmsc436_nda.GoNoGoActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HomeActivity:AppCompatActivity(),LifecycleOwner {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var databaseUsers:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater);
        binding.startGoNoGo.setOnClickListener { startGoNoGo() }
        binding.startTrailMaking.setOnClickListener { startTrailMaking() }
       // binding.startCorsiBlockTapping.setOnClickListener{ startCorsiBlockTapping()}
        addUser(Firebase.auth.currentUser);
        setContentView(binding.root);
    }

    private fun startGoNoGo(){
        val intent=Intent(this, GoNoGoActivity::class.java);
        startActivity(intent);
    }
    private fun startTrailMaking(){
        val intent=Intent(this, TrailMakingActivity::class.java);
        startActivity(intent);
    }




    private fun checkLoggedIn(){
        val user = Firebase.auth.currentUser
        if (user != null) {
            Log.e(TAG,"User is logged in");
        } else {
            Log.e(TAG,"user is not logged in");
        }
    }
    private fun addUser(user: FirebaseUser?) {
        var firebaseUserId =
            if(user!=null){
                user.uid
            }else{
                //the user is a guest/hasn't logged in
                "0";
            }
        //this is the id the database will you to refer to the user
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users")

        val currentUser = MyUser(firebaseUserId, 1, 1);

        databaseUsers.child(firebaseUserId).setValue(currentUser).addOnSuccessListener {
            Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
        }

    }

    companion object{
        private final const val ONE_SECOND=1000L
        private const val TAG="True Grinder";
    }
}