package com.example.myapplication

data class NoteItem(val title: String, val description: String,val noteId: String){
    constructor() : this("", "",noteId = "")
}
