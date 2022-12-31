package com.blez.fitnytech

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.blez.fitnytech.databinding.ActivityMainBinding
import com.blez.fitnytech.utils.TokenManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSigninClient : GoogleSignInClient
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        tokenManager = TokenManager(this)
        if (!tokenManager.getEmail().isNullOrEmpty()){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        auth = FirebaseAuth.getInstance()
        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSigninClient = GoogleSignIn.getClient(this,gson)

    binding.LoginBTN.setOnClickListener {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()){
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        tokenManager.saveEmail(email)
                        tokenManager.saveUserName(email.replace("@gmail.com","").trim())
                        Log.e("TAG",tokenManager.getUserName().toString())
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

        binding.googleSign.setOnClickListener {
            sigInGoogle()

        }


    }
    private fun sigInGoogle(){
        val siginInIntent = googleSigninClient.signInIntent
        launcher.launch(siginInIntent)

    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount?  = task.result
            if(account!= null){
                updateUI(account)
            }
        }
        else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener { 
            if(it.isSuccessful) {
                account.email?.let { it1 -> tokenManager.saveEmail(it1) }
                tokenManager.saveUserName(account.email?.replace("@gmail.com","")?.trim()!!)
                Log.e("TAG",tokenManager.getUserName().toString())
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()



            }else Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}