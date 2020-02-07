package domain.todo

import domain.todo.entities.TodoItem
import resources.repositories.TodoRepositoryInMemory

class TodoService {

    private val repository = TodoRepositoryInMemory()

    fun getAll(): List<TodoItem> {
        return repository.getAll()
    }

    fun get(id: Int): TodoItem? {
        return repository.get(id)
    }

    fun create(todoItem: TodoItem) {
        repository.create(todoItem)
    }

    fun update(todoItem: TodoItem) {
        repository.update(todoItem)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }
}
