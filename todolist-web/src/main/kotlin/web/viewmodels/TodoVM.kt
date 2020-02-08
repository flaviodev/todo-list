package web.viewmodels

import model.TodoItem
import shared.User

data class TodoVM(private val todos: List<TodoItem>, private val user: User) {
    val userName = user.name
    val todoItems = todos
}





