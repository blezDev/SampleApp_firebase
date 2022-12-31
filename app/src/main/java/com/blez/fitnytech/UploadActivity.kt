package com.blez.fitnytech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blez.fitnytech.databinding.ActivityUploadBinding
import com.blez.fitnytech.model.Post
import com.blez.fitnytech.utils.TokenManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference : StorageReference
    private lateinit var tokenManager: TokenManager
    private var uri : Uri?= Uri.parse("hello")

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
       binding.imageView.setImageURI(it)
        if (it != null) {
            uri = it
        }

    }
    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_upload)
        supportActionBar?.title = "!!!Upload Your Post here!!!"
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        tokenManager = TokenManager(this)



        binding.imageBTN.setOnClickListener {
        contract.launch("image/*")
        }
        binding.submitBTN.setOnClickListener {
            if(binding.editTextTextMultiLine.text.isNullOrEmpty() || uri == Uri.parse("hello")){
                Toast.makeText(this, "OOPS!!! Check the form before posting", Toast.LENGTH_SHORT).show()
            }else{
            val photoReference = storageReference.child("images/${System.currentTimeMillis()}")
                photoReference.putFile(uri!!)
                    .continueWithTask {photoUploadTask->
                        photoReference.downloadUrl
                    }.continueWithTask{downloadUriTask->

                            val post = Post(
                                creationTime = System.currentTimeMillis().toInt(),
                                description = binding.editTextTextMultiLine.text.toString(),
                                image = downloadUriTask.result.toString(),
                                username = tokenManager.getUserName()!!
                            )
                        firestore.collection("posts").add(post)
                    }.addOnCompleteListener {postCreationTask->
                        if(!postCreationTask.isSuccessful){
                            Log.e("TAG",postCreationTask.exception.toString())
                            Toast.makeText(this, "Failed to save post", Toast.LENGTH_SHORT).show()
                        }
                         if (postCreationTask.isSuccessful){
                             binding.editTextTextMultiLine.text.clear()
                             Toast.makeText(this, "Post Successful", Toast.LENGTH_SHORT).show()
                             val intent = Intent(this,HomeActivity::class.java)
                             startActivity(intent)
                             finish()
                         }
                        
                    }

            }

        }
    }




}