package com.example.bookstore.model

data class BookData(
    val id : Int = 0,
    val bookTitle: String,
    val price : Double,
    val authorName : String,
    val bookImage : String
)
