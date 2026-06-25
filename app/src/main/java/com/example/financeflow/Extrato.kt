package com.example.financeflow

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class extrato : AppCompatActivity() {

    private lateinit var recyclerTransacoes: RecyclerView
    private lateinit var transacaoAdapter: TransacoesAdapter
    private lateinit var transacoesList: MutableList<Transacoes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        // 1. Vinculo ID do XML
        recyclerTransacoes = findViewById(R.id.rvtransacoes)

        // 2. Configuração do LayoutManager
        recyclerTransacoes.layoutManager = LinearLayoutManager(this)

        transacoesList = mutableListOf()
        transacaoAdapter = TransacoesAdapter(transacoesList)

        // 3. Vinculo do Adapter
        recyclerTransacoes.adapter = transacaoAdapter

        val btvoltar = findViewById<Button>(R.id.btvoltar)
        btvoltar.setOnClickListener {
            finish()
        }

        buscarDadosDoFalebase()
    }// final do Oncreat

    private fun buscarDadosDoFalebase() {
        val database = FirebaseDatabase.getInstance()
        val transacoesRef = database.getReference("transacoes")
        transacoesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transacoesList.clear()
                for (item in snapshot.children) {
                    val t = Transacoes(
                        valor = item.child("02-Valor").value?.toString() ?: "",
                        descricao = item.child("03-Descrição").value?.toString() ?: "",
                        data = item.child("04-Data").value?.toString() ?: "",
                        tipo = item.child("05-Tipo").value?.toString() ?: "",
                        hora = item.child("06-Hora").value?.toString() ?: ""
                    )
                    transacoesList.add(t)
                }
                transacaoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText ( baseContext, "Erro:${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}