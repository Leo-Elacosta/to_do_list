package com.project.todolistfirebase.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.todolistfirebase.ui.auth.AuthState
import com.project.todolistfirebase.ui.auth.AuthViewModel
import com.project.todolistfirebase.ui.auth.LoginScreen
import com.project.todolistfirebase.ui.auth.SignUpScreen
import com.project.todolistfirebase.ui.tasks.TaskListScreen
import com.project.todolistfirebase.ui.tasks.TaskViewModel

@Composable
fun AppNavigation() {

    //NavController é o cerebro da navegação. Ele é responsável por gerenciar a navegação entre as telas.
    val navController = rememberNavController()
    //Criamos a instancia do viewModel
    val authViewModel : AuthViewModel = viewModel()
    //Coletamos o estado da autenticação do viewModel
    val authState by authViewModel.authState.collectAsState()

    val context = LocalContext.current

    //LaunchEffect observa o authstate e executa ações com base no estado
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                //se o estado for sucesso, navega para a lista de tarefas
                Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.TaskList.route) {
                    popUpTo(Routes.Login.route) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                //se o estado for erro, exibe uma mensagem de erro
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit //não faz nada nos outros estados
        }
    }

    //navHost é o container que exibe a tela atual
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route //Primeira tela a ser exibida
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                //passamos as funções de login e cadastro para a tela de login
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onSignUpClick = {
                    //navega para a tela de cadastro
                    navController.navigate(Routes.SignUp.route)
                }
            )
        }

        //Define qual composable será exibido quando a rota for "signUp"
        composable(Routes.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { email, password ->
                    authViewModel.singUp(email, password)
                },
                onLoginClick = {
                    //navega para a tela de login
                    navController.navigate(Routes.Login.route)
                }
            )
        }

        //define o composable para a rota da lista de tarefas (por quanto vazia)
        composable(Routes.TaskList.route) {
            // 1. Criamos a instância do TaskViewModel
            val taskViewModel: TaskViewModel = viewModel()

            // 2. Coletamos a lista de tarefas do ViewModel.
            //    O 'collectAsState' garante que a tela se recomponha sempre que a lista mudar.
            val tasks by taskViewModel.tasks.collectAsState()

            // 3. Passamos a lista de tarefas e as funções do ViewModel para a nossa tela.
            TaskListScreen(
                tasks = tasks,
                onAddTaskClick = { title ->
                    taskViewModel.addTask(title)
                },
                onTaskCheckedChange = { task, isChecked ->
                    taskViewModel.updateTask(task, isChecked)
                },
                onDeleteTaskClick = { task ->
                    taskViewModel.deleteTask(task)
                }
            )
        }
    }
}