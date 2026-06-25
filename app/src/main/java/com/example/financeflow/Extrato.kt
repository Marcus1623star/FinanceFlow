package com.example.financeflow

import android.os.Bundle
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

    private lateinit var Transacoes: RecyclerView
    private lateinit var transacaoAdapter: TransacaoAdapter
    private lateinit var transacoesList: MutableList<Transacoes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_extrato)
        Transacoes = findViewById(R.id.rvtransacoes)
        Transacoes.layoutManager = LinearLayoutManager(this)
        transacoesList = mutableListOf()
        transacaoAdapter = TransacaoAdapter(transacoesList)
        Transacoes.adapter = transacaoAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rvtransacoes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        buscarDadosDoFalebase()
    }
    private fun buscarDadosDoFalebase() {
        val database = FirebaseDatabase.getInstance()
        val transacoesRef =database.getReference("transacoes")
        transacoesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transacoesList.clear()
                for(item in snapshot.children){
                    //Para não vir informações vazias

                    val valor = item.child("02-Valor").value?.toString() ?: "R$ 0,00"
                    val descricao = item.child("03-Descrição").value?.toString() ?: "Sem descrição"
                    val data = item.child("04-Data").value?.toString() ?: "--/--/----"
                    val tipo = item.child("05-Tipo").value?.toString() ?: "Indefinido"
                    val hora = item.child("06-Hora").value?.toString() ?: "00:00"

                    val t = Transacoes(
                        valor = valor,
                        descricao = descricao,
                        data = data,
                        tipo = tipo,
                        hora = hora
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