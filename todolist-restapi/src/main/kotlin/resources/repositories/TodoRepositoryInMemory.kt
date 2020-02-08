package resources.repositories

import domain.todo.TodoRepository
import domain.todo.entities.TodoItem
import domain.todo.types.Importance
import java.time.LocalDate

class TodoRepositoryInMemory : TodoRepository {

    override fun getAll(): List<TodoItem> {
        return todos
    }

    override fun get(id: Int): TodoItem? {
        return todos.firstOrNull { it.id == id }
    }

    override fun create(todoItem: TodoItem) {
        todos.add(todoItem)
    }

    override fun update(todoItem: TodoItem) {
        todos.firstOrNull { it.id == todoItem.id }?.let {
            todos.add(todos.indexOf(it), todoItem)
            todos.remove(it)
        } ?: throw IllegalStateException("Todo item not found")
    }

    override fun delete(id: Int) {
        todos.firstOrNull { it.id == id }?.let {
            todos.remove(it)
        } ?: throw IllegalStateException("Todo item not found")
    }
}

val todo1 = TodoItem(
    id = 1,
    title = "Add RestAPI Data Access",
    details = "Add database support",
    assignedTo = "Me",
    dueDate = LocalDate.of(2020, 12, 18),
    importance = Importance.HIGH
)

val todo2 = TodoItem(
    id = 2,
    title = "Add RestAPI Service",
    details = "Add a service to get the data",
    assignedTo = "Me",
    dueDate = LocalDate.of(2020, 12, 18),
    importance = Importance.MEDIUM
)

val todos = mutableListOf(
    todo1,
    todo2
)