package com.rocksolidknowledges.models

import com.rocksolidknowledge.todolist.shared.TodoItem
import com.rocksolidknowledge.todolist.shared.User


data class TodoVM(private val todos: List<TodoItem>, private val user: User) {
    val userName = user.name
    val todoItems = todos
}





