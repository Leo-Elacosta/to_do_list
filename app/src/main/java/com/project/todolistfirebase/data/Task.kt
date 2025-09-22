package com.project.todolistfirebase.data

data class Task (
    val id: String = "",
    val title: String = "",
    val isCompleted: Boolean = false,
    val userId: String = ""
)