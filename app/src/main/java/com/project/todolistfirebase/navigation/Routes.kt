package com.project.todolistfirebase.navigation

// Define todas as rotas (telas) possíveis no nosso app.
// Usar uma sealed class garante que não erremos o nome da rota em texto.
sealed class Routes(val route: String) {
    data object Login : Routes("login")
    data object SignUp : Routes("signup")
    data object TaskList : Routes("tasklist") // Já vamos deixar a rota da lista de tarefas pronta
}