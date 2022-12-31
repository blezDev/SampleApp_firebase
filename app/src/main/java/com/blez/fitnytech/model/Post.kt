package com.blez.fitnytech.model

import com.google.firebase.firestore.PropertyName


data class Post(
    @get:PropertyName("time_upload")
    val creationTime : Int=0,
    @get:PropertyName("description")
    val description : String= "",
    @get:PropertyName("image")
    val image : String="",
    @get:PropertyName("username")
    val username: String=""
)
