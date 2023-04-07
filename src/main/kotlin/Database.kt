package db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.sql.ResultSet
import java.util.Vector

data class Task(
    val title: String,
    val description: String,
    val due_date: String,
    val priority: UInt,
    var status: Boolean = false,
)

class Database {
    private lateinit var connection: Connection
    private lateinit var statement: Statement

    init {
        connect()
        createTable()
    }

    private fun connect() {
        val url = "jdbc:sqlite:todo.db"
        connection = DriverManager.getConnection(url)
        statement = connection.createStatement()
    }

    fun close() {
        statement.close()
        connection.close()
    }

    private fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT,
                due_date TEXT,
                priority INTEGER,
                status INTEGER
            )
        """.trimIndent()

        statement.execute(sql)
    }

    fun insertTask(title: String, description: String, dueDate: String, priority: Int, status: Boolean) {
        val sql = """
            INSERT INTO tasks (title, description, due_date, priority, status)
            VALUES ('$title', '$description', '$dueDate', $priority, $status)
        """.trimIndent()

        statement.execute(sql)
    }

    fun getTasks(): Vector<Task> {
        val tasks = Vector<Task>()
        val sql = "SELECT title, description, due_date, priority, status FROM tasks"
        val statement = connection.createStatement()
        val resultSet: ResultSet = statement.executeQuery(sql)

        while (resultSet.next()) {
            val title = resultSet.getString("title")
            val description = resultSet.getString("description")
            val due_date = resultSet.getString("due_date")
            val priority = resultSet.getInt("priority").toUInt()
            val status = resultSet.getBoolean("status")
            val task = Task(title, description, due_date, priority, status)
            tasks.add(task)
        }

        return tasks
    }

    // Implement other database operations like update, delete, and fetch here
}