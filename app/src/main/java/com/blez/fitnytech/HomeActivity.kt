package com.blez.fitnytech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.blez.fitnytech.databinding.ActivityHomeBinding
import com.blez.fitnytech.model.Post
import com.blez.fitnytech.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {
   private lateinit var binding: ActivityHomeBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var auth : FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var adapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        tokenManager = TokenManager(this)
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        val postsReference = fireStore.collection("posts")
            .limit(10)
            .orderBy("time_upload",Query.Direction.DESCENDING)
        postsReference.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot==null){
                Log.e("TAG","Errpr ${error.toString()}",error)
                return@addSnapshotListener
            }else{
              val posts =  snapshot.toObjects(Post::class.java)
                adapter = PostAdapter(this,posts)
                binding.postRecyclerView.adapter = adapter

            }
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_menu){
            tokenManager.deteleCredit()
            auth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)

    }
}