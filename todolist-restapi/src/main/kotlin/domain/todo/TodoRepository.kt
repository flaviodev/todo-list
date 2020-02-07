package domain.todo

import domain.todo.entities.TodoItem

interface TodoRepository {
    fun getAll(): List<TodoItem>

    fun get(id: Int): TodoItem?

    fun create(todoItem: TodoItem)

    fun update(todoItem: TodoItem)

    fun delete(id: Int)
}