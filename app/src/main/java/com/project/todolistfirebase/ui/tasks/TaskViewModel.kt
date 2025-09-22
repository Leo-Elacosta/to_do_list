package com.project.todolistfirebase.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.todolistfirebase.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    //Stateflow para guardar e expor a lista de tarefas na UI
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        val userId = auth.currentUser?.uid ?: return //Sai da função se não houver usuario logado

        //referencia para a colocação "tasks" no firestore
        val tasksCollection = firestore.collection("tasks")

        //escuta por atualizações em tempo real
        tasksCollection
            .whereEqualTo(
                "userId",
                userId
            ) // FILTRO: Apenas tarefas cujo userId seja o do usuário logado
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Lidar com erro, se necessário
                    return@addSnapshotListener
                }

                snapshot?.let { querySnapshot ->
                    // MAPEAMENTO MANUAL - A forma mais segura e à prova de falhas
                    val taskList = querySnapshot.documents.mapNotNull { document ->
                        Task(
                            id = document.id,
                            title = document.getString("title") ?: "",
                            isCompleted = document.getBoolean("isCompleted") ?: false,
                            userId = document.getString("userId") ?: ""
                        )
                    }
                    _tasks.value = taskList
                }
            }
    }
    fun addTask(title: String) {
        val userId =
            auth.currentUser?.uid ?: return // Sai da função se não houver usuário logado

        viewModelScope.launch {
            // Em vez de criar um objeto Task, criamos um "mapa" com os dados
            val newTask = hashMapOf(
                "title" to title,
                "isCompleted" to false,
                "userId" to userId
            )

            firestore.collection("tasks").add(newTask).await()
        }
    }

    //Atualiza o status de uma task
    fun updateTask(task: Task, isCompleted: Boolean) {
        if (task.id.isEmpty()) return //Não pode atualizar task sem ID
        android.util.Log.d(
            "DEBUG_CHECK",
            "2. ViewModel recebeu o update para '${task.title}'. ID: ${task.id}"
        )

        if (task.id.isEmpty()) {
            android.util.Log.d("DEBUG_CHECK", "ERRO: Task sem ID, impossível atualizar.")
            return
        }

        viewModelScope.launch {
            try {
                firestore.collection("tasks").document(task.id)
                    .update("isCompleted", isCompleted)
                    .await()
                android.util.Log.d("DEBUG_CHECK", "3. Sucesso! Firebase confirmou o update.")
            } catch (e: Exception) {
                android.util.Log.d(
                    "DEBUG_CHECK",
                    "3. ERRO ao atualizar no Firebase: ${e.message}"
                )
            }
        }

        viewModelScope.launch {
            //atualiza o campo Completed do documento correspondente
            firestore.collection("tasks")
                .document(task.id)
                .update("isCompleted", isCompleted)
                .await()
        }
    }

    //Deleta a task
    fun deleteTask(task: Task) {
        if (task.id.isEmpty()) return //Não pode deletar task sem ID
        viewModelScope.launch {
            firestore.collection("tasks")
                .document(task.id)
                .delete().await()
        }
    }
}