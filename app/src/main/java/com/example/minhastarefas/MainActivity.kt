package com.example.minhastarefas

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minhastarefas.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences
    private var navController: NavController? = null
    private val listaTarefa = arrayListOf<Tarefa>()
    private val listaCategorias = arrayListOf<String>()
    private val gson = GsonBuilder().create()
    val adapter = TarefasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)

        recuperaDados("TAREFAS")

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
        adapter.submitList(listaTarefa)
        adapter.onClick = {
            val index = listaTarefa.indexOf(it)
            listaTarefa[index].completa = !listaTarefa[index].completa

            salvaLista(listaTarefa)
        }
        adapter.openDialog = {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Excluir Tarefa?")
            dialog.setMessage("Tem certeza de que deseja excluir essa tarefa?")
            dialog.setPositiveButton("Sim", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val index = listaTarefa.indexOf(it)
                    listaTarefa.removeAt(index)
                    salvaLista(listaTarefa)
                    adapter.notifyDataSetChanged()
                }
            })
            dialog.setNegativeButton("Não", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    // Não faz nada
                }
            })
            dialog.show()
        }
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
        salvaLista(listaTarefa)
    }

    private fun salvaLista(lista: List<Tarefa>) {
        val string = gson.toJson(lista)

        sharedPrefs.edit().putString("TAREFAS", string).apply()
    }

    private fun recuperaDados(chave: String) {
        val string = sharedPrefs.getString(chave, "[]")
        val lista = gson.fromJson(
            string,
            Array<Tarefa>::class.java
        )
        listaTarefa.addAll(lista)
    }

    private fun categorizaTarefas(descricao: String): List<String> {
        val listaPalavras = descricao.trim().split("\\s+".toRegex())
        val hashTags = arrayListOf<String>()
        for (i in listaPalavras) {
            if (i.contains("#")) {
                if (!listaCategorias.contains(i)) {
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