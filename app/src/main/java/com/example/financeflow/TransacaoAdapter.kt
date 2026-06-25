package com.example.financeflow
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.ColorMatrixColorFilter

class TransacaoAdapter(private val transacoes: List<Transacoes>) :
    RecyclerView.Adapter<TransacaoAdapter.transacaoViewHolder>() {
    class transacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descricaoTextView: TextView = itemView.findViewById(R.id.tvdecricao_item)
        val tipoTextView: TextView = itemView.findViewById(R.id.tvtipo_item)
        val valorTextView: TextView = itemView.findViewById(R.id.tvvalor_item)
        val dataTextView: TextView = itemView.findViewById(R.id.tvdata_item)
        val horaTextView: TextView = itemView.findViewById(R.id.tvhora_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transacaoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transacao, parent, false)
        return transacaoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: transacaoViewHolder, position: Int) {
        val transacaoAtual = transacoes[position]
        holder.descricaoTextView.text = transacaoAtual.descricao
        holder.tipoTextView.text = transacaoAtual.tipo
        holder.valorTextView.text = transacaoAtual.valor
        holder.dataTextView.text = transacaoAtual.data
        holder.horaTextView.text = transacaoAtual.hora

        if (transacaoAtual.tipo=="Crédito"){
            holder.valorTextView.setTextColor(Color.BLUE)
            holder.tipoTextView.setTextColor(Color.BLUE)
        }
        else{
            holder.valorTextView.setTextColor(Color.RED)
            holder.tipoTextView.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return transacoes.size
    }
}