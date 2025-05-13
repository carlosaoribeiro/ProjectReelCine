# 🎬 ReelCineProject

Um aplicativo Android de recomendações sociais de filmes, onde os usuários podem sugerir filmes em círculos privados, buscar dados da API TMDB e compartilhar recomendações em tempo real com integração ao Firebase.

---

## ✅ Funcionalidades

- 🔐 Autenticação com Firebase (login e cadastro)  
- 👤 Salvamento de nome, e-mail e UID com `SessionManager`  
- 🔍 Busca de filmes usando a API do TMDB  
- ➕ Recomendar filmes com comentários e pôster  
- 👥 Criar e gerenciar grupos privados ("círculos")  
- 📋 Listar recomendações em um feed colaborativo  
- 🧾 Cards com pôster, título, autor, comentário e horário  
- 🔄 Sincronização em tempo real com Firebase Firestore  
- 🌙 Tema escuro moderno com botões arredondados  

---

## 🧪 Requisitos

- Android Studio (recomendado: Hedgehog ou mais recente)  
- SDK mínimo Android: 24  
- Projeto no Firebase com Firestore e Authentication ativados  
- Chave de API do TMDB (https://www.themoviedb.org/documentation/api)  
- Permissões necessárias:
  - `INTERNET`
  - `ACCESS_NETWORK_STATE`

---

## 🖼️ Capturas de Tela

<div align="center">
  <img src="screenshots/splash.png" width="180"/>
  <img src="screenshots/login.png" width="180"/>
  <img src="screenshots/home.png" width="180"/>
  <img src="screenshots/group_feed.png" width="180"/>
  <img src="screenshots/recommend_movie.png" width="180"/>
</div>

---

## 🚀 Como Usar

1. Instale o app no seu dispositivo Android  
2. Crie sua conta ou faça login com e-mail e senha  
3. Crie ou entre em um grupo  
4. Toque em **+ Recomendar Filme** para buscar um filme e adicionar um comentário  
5. Veja as recomendações no **Feed** com nome do autor, imagem e comentário  
6. Todas as ações são salvas automaticamente no Firestore em tempo real  

---

## 🧱 Telas Principais

- `SplashActivity`: Tela de carregamento  
- `LoginActivity`: Login via Firebase  
- `CadastroUsuarioActivity`: Cadastro de usuário  
- `MainActivity`: Menu principal com navegação  
- `GruposActivity`: Listagem e criação de grupos  
- `RecomendacoesGrupoFragment`: Feed do grupo com recomendações  
- `RecomendarFilmeActivity`: Busca e recomendação de filmes  
- `FeedActivity`: Feed geral de recomendações  

---

## 🛠️ Tecnologias Utilizadas

- Kotlin  
- Arquitetura MVVM  
- Firebase Authentication  
- Firebase Firestore  
- Retrofit (API TMDB)  
- RecyclerView com adapters personalizados  
- Glide para carregamento de imagens  
- Material Design  
- Tema escuro personalizado (#0B132B com texto branco e botões roxos arredondados)  

---

## 📁 Estrutura no Firestore

- `usuarios`  
  - Campos: `nome`, `email`, `avatarUrl`, `uid`  

- `usuarios/{uid}/recomendacoes`  
  - Campos: `titulo`, `comentario`, `posterPath`, `timestamp`, `usuarioNome`, `avatarUrl`  

- `grupos`  
  - Campos: `nome`, `descricao`, `adminId`  

- `grupos/{grupoId}/recomendacoes`  
  - Campos: `titulo`, `comentario`, `posterPath`, `timestamp`, `usuarioNome`, `avatarUrl`  

---

## 📌 Observações

- Cada grupo possui seu próprio feed de recomendações  
- Busca de filmes via TMDB com resultados em tempo real  
- `SessionManager` gerencia os dados locais do usuário  
- Layout moderno e acessível, com interface escura  
- Permite expansão futura para comentários, curtidas e recomendações cruzadas  

---

## 🔐 Segurança e Privacidade

Todos os dados são armazenados com segurança no **Firebase Firestore** e acessíveis apenas pelo usuário autenticado. A autenticação é feita pelo **Firebase Authentication** com controle de acesso baseado no UID.

---

## 📄 Licença

Este projeto é de uso educacional e para portfólio pessoal. Para uso comercial ou contribuições, entre em contato com o autor.
