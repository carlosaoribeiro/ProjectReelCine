
# 🎬 ReelCineProject

An Android app for social movie recommendations, where users can suggest films in private circles, search movie data via the TMDB API, and share recommendations in real time with Firebase integration.

---

## 🎯 Inspiration Behind the Name

**ReelCine** is a fusion of “Reel” (as in film reel) and “Cine” (cinema in Portuguese), representing the connection between classic movie experiences and modern digital sharing. The name reflects the app's mission: creating a social and collaborative cinema-like experience, where friends share movie suggestions as if chatting after a film session.

---

## 🩹 Problem We're Solving

Today, it’s hard to remember or recover good movie recommendations shared by friends. Suggestions get lost in WhatsApp chats, social media threads, or forgotten notes.  
**ReelCine** solves this by creating a dedicated space for **organized, visual, and trustworthy movie recommendations** within private groups, featuring personalized comments, posters, and real-time sync.

---

## ✅ Features

- 🔐 Firebase Authentication (sign-up and login)  
- 👤 Save name, email, and UID with `SessionManager`  
- 🔍 Search for movies using the TMDB API  
- ➕ Recommend movies with comments and posters  
- 👥 Create and manage private groups ("circles")  
- 📋 Display recommendations in a collaborative feed  
- 🧾 Cards with poster, title, author, comment, and timestamp  
- 🔄 Real-time sync with Firebase Firestore  
- 🌙 Modern dark theme with rounded buttons  

---

## 🧪 Requirements

- Android Studio (recommended: Hedgehog or newer)  
- Minimum Android SDK: 24  
- Firebase project with Firestore and Authentication enabled  
- TMDB API key (https://www.themoviedb.org/documentation/api)  
- Required permissions:
  - `INTERNET`
  - `ACCESS_NETWORK_STATE`

---

## 🖼️ Screenshots

<div align="center">
  <img src="screenshots/splash.png" width="180"/>
  <img src="screenshots/login.png" width="180"/>
  <img src="screenshots/home.png" width="180"/>
  <img src="screenshots/group_feed.png" width="180"/>
  <img src="screenshots/recommend_movie.png" width="180"/>
</div>

---

## 🚀 How to Use

1. Install the app on your Android device  
2. Sign up or log in with email and password  
3. Create or join a group  
4. Tap **+ Recommend Movie** to search for a movie and add a comment  
5. View recommendations in the **Feed** with author name, poster, and message  
6. All actions are saved automatically in Firestore in real time  

---

## 🧱 Main Screens

- `SplashActivity`: Splash screen  
- `LoginActivity`: Firebase login  
- `CadastroUsuarioActivity`: User registration  
- `MainActivity`: Main menu with navigation  
- `GruposActivity`: Group listing and creation  
- `RecomendacoesGrupoFragment`: Group feed with recommendations  
- `RecomendarFilmeActivity`: Search and recommend movies  
- `FeedActivity`: General feed with all recommendations  

---

## 🛠️ Technologies Used

- Kotlin  
- MVVM Architecture  
- Firebase Authentication  
- Firebase Firestore  
- Retrofit (TMDB API)  
- RecyclerView with custom adapters  
- Glide for image loading  
- Material Design  
- Custom dark theme (#0B132B background with white text and rounded purple buttons)  

---

## 📁 Firestore Structure

- `usuarios`  
  - Fields: `nome`, `email`, `avatarUrl`, `uid`  

- `usuarios/{uid}/recomendacoes`  
  - Fields: `titulo`, `comentario`, `posterPath`, `timestamp`, `usuarioNome`, `avatarUrl`  

- `grupos`  
  - Fields: `nome`, `descricao`, `adminId`  

- `grupos/{grupoId}/recomendacoes`  
  - Fields: `titulo`, `comentario`, `posterPath`, `timestamp`, `usuarioNome`, `avatarUrl`  

---

## 📌 Notes

- Each group has its own recommendation feed  
- Movie search via TMDB API with real-time results  
- `SessionManager` handles local user data  
- Clean and accessible layout with dark interface  
- Easily expandable for likes, comments, and cross-recommendations  

---

## 🔐 Security and Privacy

All data is securely stored in **Firebase Firestore** and only accessible to authenticated users. Authentication is handled by **Firebase Authentication** with UID-based access control.

---

## 📄 License

This project is for educational and personal portfolio use. For commercial use or contributions, please contact the author.
