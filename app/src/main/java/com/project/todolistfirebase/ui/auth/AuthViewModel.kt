package com.project.todolistfirebase.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//Sealed Class é para representar os diferentes estados da autenticação
sealed class AuthState {
    data object Idle : AuthState() //estado inicial
    data object Loading : AuthState() //estado de carregamento
    data object Success : AuthState() //estado de sucesso
    data class Error(val message: String) : AuthState() //estado de erro
}


class AuthViewModel : ViewModel() {
    //instância do firebase auth
    private val auth = FirebaseAuth.getInstance()

    //stateflor para exibir o estado da autenticação na UI
    //É privado para que não seja acessado diretamente
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    //versão publica e somente leitura do stateflow
    val authState = _authState.asStateFlow()

    fun singUp(email: String, password: String){
        viewModelScope.launch {
            //emite o estado de carregamento
            _authState.value = AuthState.Loading
            try {
                //tentativa de criar o usuário no firebase
                auth.createUserWithEmailAndPassword(email, password).await()
                //se der certo emite o estado de sucesso
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                //se der errado emite o estado de erro com a mensagem de erro
                val errorMessage = e.message ?: "Erro desconhecido"
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                //tentativa de login
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Erro desconhecido"
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }
}