package com.example

import db.Database
import db.Task
import java.util.concurrent.TimeUnit
import java.util.Vector
import java.io.File
import kotlin.system.exitProcess

//data class Task(
//	val title: String,
//	val description: String,
//	val due_date: String,
//	val priority_level: UInt,
//	var status: Boolean = false,
//)

fun showTasks(tasks: Vector<Task>) {
	println("----------------------------------------------------------------")		
	if (tasks.size == 0) {
		println("Add a task!")
	} else {
		for (item in tasks) {
			println("Title: ${item.title} | Due Date: ${item.due_date} | Priority: ${item.priority} | Status: ${item.status}")
		}
	}
	println("----------------------------------------------------------------")		
}

fun titleExists(title: String, tasks: Vector<Task>): Boolean {
	for (task in tasks) {
		if (task.title == title) {
			return true
		}
	}
	return false
}

fun writeTasks(tasks: Vector<Task>, file_name: String) {
    val file = File(file_name)
    file.printWriter().use { writer ->
        tasks.forEach { task ->
            writer.println("${task.title},${task.due_date},${task.priority},${task.status},${task.description}")
        }
    }
}

fun insertTasksToDatabase(tasks: Vector<Task>) {
	val database = Database()
	for (task in tasks) {
		database.insertTask(
			task.title,
			task.description,
			task.due_date,
			task.priority.toInt(),
			task.status
		)
	}
}

fun loadTasks(file_name: String): Vector<Task> {
    val tasks = Vector<Task>()
    val file = File(file_name)
    if (file.exists()) {
        file.forEachLine { line ->
            val fields = line.split(",")
            if (fields.size == 5) {
                val title = fields[0]
                val due_date = fields[1]
                val priority_level = fields[2].toUInt()
                val status = fields[3].toBoolean()
                val description = fields[4]
                tasks.addElement(Task(title, description, due_date , priority_level, status))
            }
        }
    } else {
        println("File not found: $file_name")
    }
    return tasks
}

fun main() {
	
	println("Welcome to Kaskman, an open source application built with Kotlin!")
	val database = Database()
	TimeUnit.SECONDS.sleep(5)
//	var tasks = loadTasks("my_tasks.csv")
	var tasks = database.getTasks()

	while (true) {

		showTasks(tasks)
		println("Enter a command (type h for help)")		
		val user_input = readLine()

		if (user_input != null) {
			when(user_input) {
				"h" -> {
					println("Add a new task: a")
					println("View a task: v")
					println("Delete a task: d")
					println("Complete a task: c")
					println("Quit: q")
				}
				"a" -> {
					println("Set a title for your task: ")
					val title = readLine() ?: "default_title"
					if (titleExists(title, tasks)) {
						println("That same title already exists! Use a different one")
						continue
					}
					println("Write a description for your task")
					val desc = readLine() ?: "default_title"

					println("Set the due date for your task")

					val due_date = readLine() ?: "default_title"
					println("Set a priority level for your task 1-10")

					val priority = readLine()!!.toUInt()
					var new_task = Task(title, desc, due_date, priority)
					tasks.addElement(new_task)
					continue
				}
				"v" -> {
					println("Enter the task title to be viewed")
					val title = readLine() ?: "DefaultTitle"
					var found = false
					for (item in tasks) {
						if (title == item.title) {
							found = true
							println("Title: ${title}")
							println("Due date: ${item.due_date}")
							println("Priority: ${item.priority}")
							println("Status: ${item.status}")
							println("Description: ${item.description}")
							break
						}
					}
					if (!found) {
						println("There is no such task title as $title")
					}
				}
				"c" -> {
					println("Enter the task title to be completed")
					val title = readLine() ?: "DefaultTitle"
					var found = false
					for (item in tasks) {
						if (title == item.title) {
							found = true
							item.status = true 
							break
						}
					}
					if (!found) {
						println("There is no such task title as $title")
					}
				}
				"q" -> {
					println("Saving tasks to sql databse")
//					println("Saving tasks to file 'my_tasks.csv'")
//					writeTasks(tasks, "my_tasks.csv")
					TimeUnit.SECONDS.sleep(2)
					insertTasksToDatabase(tasks)
					println("Thanks for using Kaskman, quitting...")
					TimeUnit.SECONDS.sleep(2)
					exitProcess(0)
				}
				"d" -> {
					println("Enter the task title to be deleted")
					val title = readLine() ?: "DefaultTitle"
					var found = false
					for (item in tasks) {
						if (title == item.title) {
							found = true
							tasks.remove(item)
							break
						}
					}
					if (!found) {
						println("There is no such task title as $title")
					}
				}
				else -> {
					println("The term $user_input is not recognized as the name of a command")
					println("Type h for help")
				}
			}
		} else {
			println("Incorrect input")
		}

	println("============================================================================================")
	println("============================================================================================")
    }
}
	
	
