package com.example.financeflow

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btlistar = findViewById<Button>(R.id.btlistar)
        val etvalor = findViewById<EditText>(R.id.etvalor)
        val etdescricao = findViewById<EditText>(R.id.etdescricao)
        val etdata = findViewById<EditText>(R.id.etdata)
        val rgtipo = findViewById<RadioGroup>(R.id.rgtipo)
        val btsalvar = findViewById<Button>(R.id.btsalvar)

        etdata.setOnClickListener {
            val c = Calendar.getInstance()
            val ano = c.get(Calendar.YEAR)
            val mes = c.get(Calendar.MONTH)
            val dia = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, year, month, day ->

                val dataformatada = String.format("%02d/%02d/%04d", day, month + 1, year)
                etdata.setText(dataformatada)
            }, ano, mes, dia)
            dpd.show()
        }

        btsalvar.setOnClickListener {
            val numero = (1..Long.MAX_VALUE).random()
            val valor = etvalor.text.toString()
            val descricao = etdescricao.text.toString()
            val dataselecionada = etdata.text.toString()
            val tipodetransacao = rgtipo.checkedRadioButtonId
// Aqui o ID (número) sai uma String (texto)
            val tipoString =
                if (tipodetransacao == R.id.rbcredito) {
                    "Crédito"
                } else {
                    "Débito"
                }

            //validação dos campos
            if (valor.isNotEmpty() && descricao.isNotEmpty() && dataselecionada.isNotEmpty()) {
                //pegar data e hora atual
                val horaatual = Date()
                val horaformatada =
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(horaatual)

                //salvar no firebase
                val dados = mapOf(
                    "01-NTransação" to numero,
                    "02-Valor" to valor,
                    "03-Descrição" to descricao,
                    "04-Data" to dataselecionada,
                    "05-Tipo" to tipoString,
                    "06-Hora" to horaformatada
                )
                val database = FirebaseDatabase.getInstance()
                val transacoesRef = database.getReference("transacoes")
                transacoesRef.push().setValue(dados).addOnSuccessListener {
                    Toast.makeText(this, "Transação $numero salva com sucesso!", Toast.LENGTH_LONG)
                        .show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Erro:${it.message}", Toast.LENGTH_LONG).show()
                }
                //Limpar Tela
                etvalor.text.clear()
                etdescricao.text.clear()
                etdata.text.clear()
                rgtipo.clearCheck()
            } else {
                Toast.makeText(this, "Preencha Todos os Campos", Toast.LENGTH_LONG).show()
            }
        }
        btlistar.setOnClickListener {
            val intent = android.content.Intent(this, extrato::class.java)
            startActivity(intent)
        }
    }
}