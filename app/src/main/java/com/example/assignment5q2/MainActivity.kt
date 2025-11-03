package com.example.assignment5q2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assignment5q2.ui.theme.Assignment5Q2Theme
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List

data class Task(val id: Int, val title: String) {
    var isDone by mutableStateOf(false)
}
class DailyHubViewModel : ViewModel() {
    var notes by mutableStateOf("")
        private set

    private var _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> = _tasks

    private var nextTaskId = 1

    fun updateNotes(newNotes: String) {
        notes = newNotes
    }

    fun addTask(title: String) {
        _tasks.add(Task(nextTaskId++, title))
    }

    fun toggleTask(id: Int) {
        _tasks.find { it.id == id }?.let { it.isDone = !it.isDone }
    }
}

sealed class Routes(val route: String) {
    object Notes : Routes("notes")
    object Tasks : Routes("tasks")
    object Calendar : Routes("calendar")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment5Q2Theme {
                val navController = rememberNavController()
                val viewModel: DailyHubViewModel = viewModel()

                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Notes.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.Notes.route) { NotesScreen(viewModel) }
                        composable(Routes.Tasks.route) { TasksScreen(viewModel) }
                        composable(Routes.Calendar.route) { CalendarScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Routes.Notes to Icons.Default.List,
        Routes.Tasks to Icons.Default.Check,
        Routes.Calendar to Icons.Default.DateRange
    )
    NavigationBar {
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route
        items.forEach { (route, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null) },
                selected = currentRoute == route.route,
                onClick = {
                    navController.navigate(route.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Routes.Notes.route) { saveState = true }
                    }
                }
            )
        }
    }
}

@Composable
fun NotesScreen(viewModel: DailyHubViewModel) {
    var text by remember { mutableStateOf(viewModel.notes) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.updateNotes(it)
            },
            label = { Text("Your Notes") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TasksScreen(viewModel: DailyHubViewModel) {
    var newTask by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = newTask,
            onValueChange = { newTask = it },
            label = { Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            if (newTask.isNotBlank()) {
                viewModel.addTask(newTask)
                newTask = ""
            }
        }, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Add Task")
        }
        LazyColumn {
            items(viewModel.tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleTask(task.id) }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(task.title)
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { viewModel.toggleTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text("Calendar Placeholder")
    }
}