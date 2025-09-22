# To-Do List App com Compose e Firebase 📝

Aplicativo de lista de tarefas para Android, desenvolvido para demonstrar habilidades com as tecnologias mais modernas do ecossistema nativo. Este projeto permite que usuários se cadastrem, gerenciem suas tarefas e tenham seus dados sincronizados em tempo real através do Firebase.


## ✨ Funcionalidades

- **Autenticação de Usuários:** Sistema completo de Cadastro e Login com e-mail e senha.
- **Gerenciamento de Tarefas (CRUD):**
    - **Criar:** Adicionar novas tarefas através de uma caixa de diálogo.
    - **Ler:** Visualizar a lista de tarefas, separadas entre ativas e concluídas.
    - **Atualizar:** Marcar/desmarcar tarefas como concluídas.
    - **Deletar:** Remover tarefas da lista.
- **Dados em Tempo Real:** As tarefas são sincronizadas instantaneamente com o banco de dados, graças ao Cloud Firestore.
- **UI Reativa e Moderna:** Interface construída 100% com Jetpack Compose, seguindo os princípios do Material Design 3.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Arquitetura:** MVVM (Model-View-ViewModel)
- **Banco de Dados:** [Cloud Firestore](https://firebase.google.com/docs/firestore)
- **Autenticação:** [Firebase Authentication](https://firebase.google.com/docs/auth)
- **Navegação:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **Gerenciamento de Estado:** StateFlow e ViewModel

## 🚀 Próximos Passos
- [ ] Implementar um indicador de carregamento (loading).
- [ ] Adicionar funcionalidade para editar tarefas existentes.
- [ ] Implementar um botão de Logout.