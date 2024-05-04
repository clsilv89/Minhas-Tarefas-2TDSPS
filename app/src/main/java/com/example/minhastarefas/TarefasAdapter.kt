package com.example.minhastarefas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minhastarefas.databinding.ItemTarefaCompletaLayoutBinding
import com.example.minhastarefas.databinding.ItemTarefaNaoCompletaLayoutBinding

class TarefasAdapter : ListAdapter<Tarefa, RecyclerView.ViewHolder>(DiffCallback()) {

    var onClick: (Tarefa) -> Unit = {}
    var openDialog: (Tarefa) -> Unit = {}

    inner class TarefaCompletaViewHolder(val binding: ItemTarefaCompletaLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tarefa: Tarefa) {
            binding.textViewTarefaDescricao.text = tarefa.descricao
            binding.radioButtonCompletarTarefa.apply {
                setOnClickListener {
                    onClick(tarefa)
                    notifyDataSetChanged()
                }
                this.isChecked = tarefa.completa
            }
            binding.textViewTarefaDescricao.setOnLongClickListener {
                openDialog(tarefa)
                return@setOnLongClickListener true
            }
        }
    }

    inner class TarefaNaoCompletaViewHolder(val binding: ItemTarefaNaoCompletaLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tarefa: Tarefa) {
            binding.textViewTarefaDescricao.text = tarefa.descricao
            binding.radioButtonCompletarTarefa.apply {
                setOnClickListener {
                    onClick(tarefa)
                    notifyDataSetChanged()
                }
                this.isChecked = tarefa.completa
            }
            binding.textViewTarefaDescricao.setOnLongClickListener {
                openDialog(tarefa)
                return@setOnLongClickListener true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Tarefa>() {
        override fun areItemsTheSame(oldItem: Tarefa, newItem: Tarefa): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Tarefa, newItem: Tarefa): Boolean {
            return oldItem.descricao == newItem.descricao
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).completa) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val tarefaCompletaViewHolder = TarefaCompletaViewHolder(
            ItemTarefaCompletaLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
        val tarefaNaoCompletaViewHolder = TarefaNaoCompletaViewHolder(
            ItemTarefaNaoCompletaLayoutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )

        return if (viewType == 0) tarefaCompletaViewHolder else tarefaNaoCompletaViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TarefaCompletaViewHolder -> {
                holder.bind(
                    getItem(position)
                )
            }

            is TarefaNaoCompletaViewHolder -> {
                holder.bind(
                    getItem(position)
                )
            }
        }
    }
}