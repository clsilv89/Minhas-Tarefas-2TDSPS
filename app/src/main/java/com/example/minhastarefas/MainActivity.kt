package com.example.minhastarefas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minhastarefas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private val listaTarefa = arrayListOf<Tarefa>()
    private val listaCategorias = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id)

        navHostFragment?.let { fragment ->
            navController = fragment.findNavController()
        }

        navController?.let { navController ->
            setupActionBarWithNavController(navController)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController?.navigateUp() == true) {
                    navController?.navigateUp()
                } else {
                    this@MainActivity.finish()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: false || super.onSupportNavigateUp()
    }

    fun getTarefa(descricao: String) {
        navController?.navigateUp()
        listaTarefa.add(
            Tarefa(
                descricao = descricao,
                completa = false
            )
        )
        listaCategorias.addAll(categorizaTarefas(descricao))
        println(descricao)
        println(listaCategorias)
    }

    private fun categorizaTarefas(descricao: String): List<String> {
        val listaPalavras = descricao.trim().split("\\s+".toRegex())
        val hashTags = arrayListOf<String>()
        for(i in listaPalavras) {
            if(i.contains("#")) {
                if(!listaCategorias.contains(i)) {
                    hashTags.add(i)
                }
            }
        }
        return hashTags
    }

//    override fun onBackPressed() {
//        navController?.navigateUp()
//    }

//    private fun abrirTelaListaTarefas() {
//        val listaTarefasFragment = ListaTarefasFragment.newInstance({
//            abrirTelaCriarTarefas()
//        }, "")
//
//        supportFragmentManager.beginTransaction().replace(
//            binding.frameLayout.id,
//            listaTarefasFragment
//        ).commit()
//    }
//
//    private fun abrirTelaCriarTarefas() {
//        val criaTarefasFragment = CriaTarefasFragment.newInstance({
//            val tarefa = Tarefa(
//                descricao = it,
//                completa = false
//            )
//            println(tarefa)
//        }, "")
//
//        supportFragmentManager.beginTransaction().replace(
//            binding.frameLayout.id,
//            criaTarefasFragment
//        ).commit()
//    }
}