package com.carlosribeiro.reelcineproject.ui.recomendacao // Mantenha seu pacote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityMovieRatingBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.ui.*
import com.carlosribeiro.reelcineproject.ui.adapter.FilmeBuscaAdapter
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.carlosribeiro.reelcineproject.viewmodel.BuscarFilmeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.security.Timestamp

class MovieRatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieRatingBinding
    private lateinit var viewModel: BuscarFilmeViewModel
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var adapter: FilmeBuscaAdapter
    private var filmeSelecionado: FilmeUi? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupNavigationDrawer()
        setupViewModel()
        setupRecyclerView()
        setupListeners()

        showSearchState()
    }

    private fun setupNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val headerView = binding.navigationView.getHeaderView(0)
        val avatarHeader = headerView.findViewById<ImageView>(R.id.imageUserAvatar)
        val nomeHeader = headerView.findViewById<TextView>(R.id.textUserName)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.getInstance().collection("usuarios").document(uid ?: "")
            .get().addOnSuccessListener { doc ->
                val nome = doc.getString("nome") ?: "Usuário"
                val avatarUrl = doc.getString("avatarUrl")
                nomeHeader.text = nome
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this).load(avatarUrl).circleCrop().into(avatarHeader)
                }
            }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_feed -> startActivity(Intent(this, FeedActivity::class.java))
                R.id.nav_grupos -> startActivity(Intent(this, GruposActivity::class.java))
                R.id.nav_recomendacoes -> { /* Já estamos aqui */ }
                R.id.nav_perfil -> startActivity(Intent(this, PerfilActivity::class.java))
                R.id.nav_logout -> finishAffinity()
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[BuscarFilmeViewModel::class.java]
        viewModel.resultado.observe(this) { filmes ->
            showLoading(false)
            if (filmes.isNotEmpty()) {
                binding.searchResultsRecyclerView.visibility = View.VISIBLE
                adapter.submitList(filmes)
            } else {
                Toast.makeText(this, "Nenhum filme encontrado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = FilmeBuscaAdapter(filmes = emptyList()) { filme ->
            filmeSelecionado = filme
            esconderTeclado()
            showRatingState()
        }
        binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchResultsRecyclerView.adapter = adapter
    }

    private fun setupListeners() {
        // Listener para o BOTÃO de busca
        binding.searchButton.setOnClickListener {
            performSearch()
        }

        // Listener para o TECLADO
        binding.searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.submitRatingButton.setOnClickListener { saveRatingToFirestore() }
        binding.changeMovieButton.setOnClickListener { showSearchState() }
    }

    private fun performSearch() {
        val query = binding.searchInputEditText.text.toString().trim()
        if (query.isNotBlank()) {
            esconderTeclado()
            showLoading(true)
            adapter.submitList(emptyList())
            viewModel.buscarFilmes(query)
        } else {
            Toast.makeText(this, "Digite o nome do filme", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveRatingToFirestore() {
        val ratingValue = binding.movieRatingBar.rating
        val filme = filmeSelecionado
        val usuario = auth.currentUser

        if (filme == null || usuario == null) {
            Toast.makeText(this, "Erro: filme ou usuário não identificado.", Toast.LENGTH_SHORT).show()
            return
        }

        if (ratingValue == 0f) {
            Toast.makeText(this, "Por favor, selecione pelo menos uma estrela.", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = usuario.uid

        // 🔹 Busca os dados do usuário logado (nome + avatar) antes de salvar
        firestore.collection("usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                val nomeUsuario = doc.getString("nome") ?: "Usuário"
                val avatarUrl = doc.getString("avatarUrl") ?: ""

                val ratingData: HashMap<String, Any> = hashMapOf(
                    "movieId" to filme.id,
                    "movieTitle" to filme.titulo,
                    "posterPath" to filme.posterPath,
                    "backdropPath" to filme.backdropPath,
                    "rating" to ratingValue,
                    "timestamp" to com.google.firebase.Timestamp.now(),
                    "userId" to uid,
                    "usuarioNome" to nomeUsuario,
                    "avatarUrl" to avatarUrl
                )

                firestore.collection("ratings")
                    .add(ratingData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Filme avaliado com sucesso!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, FeedActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao salvar avaliação: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar dados do usuário: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun showSearchState() {
        binding.searchInputLayout.visibility = View.VISIBLE
        binding.searchButton.visibility = View.VISIBLE
        binding.searchResultsRecyclerView.visibility = View.GONE // Escondido até ter resultados
        binding.ratingSection.visibility = View.GONE
        filmeSelecionado = null
        adapter.submitList(emptyList())
    }

    private fun showRatingState() {
        binding.searchInputLayout.visibility = View.GONE
        binding.searchButton.visibility = View.GONE
        binding.searchResultsRecyclerView.visibility = View.GONE
        binding.ratingSection.visibility = View.VISIBLE
        binding.selectedMovieTitle.text = filmeSelecionado?.titulo ?: "Filme"
        binding.movieRatingBar.rating = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { imm.hideSoftInputFromWindow(it.windowToken, 0) }
    }
}