package com.luanasilva.datetrivia

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.luanasilva.datetrivia.api.DateAPI
import com.luanasilva.datetrivia.api.RetrofitHelper
import com.luanasilva.datetrivia.databinding.ActivityMainBinding
import com.luanasilva.datetrivia.model.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "info_trivia"
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val retrofitDateTrivia by lazy {
        RetrofitHelper.retrofitDateTrivia
    }

    private val listaMes = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private val lista30Dias = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30)
    private val lista31Dias = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)
    private val lista29Dias = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spinnerCarregarExibicaoMes()


        binding.btnEnviar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                carregarDiaeMesSelecionado()
            }
        }

    }


    private fun spinnerCarregarExibicaoMes() {
        binding.spinnerMes.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listaMes
        )

        binding.spinnerMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val mesSelecionado = listaMes[position]
                atualizarSpinnerDia(mesSelecionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nenhuma ação necessária
            }
        }
    }

    private fun atualizarSpinnerDia(mesSelecionado: String) {



        val meses30Dias = setOf("April", "June", "September", "November")
        val meses31Dias = setOf("January", "March", "May", "July", "August", "October", "December")

        val dias = when (mesSelecionado) {
            in meses30Dias -> lista30Dias
            in meses31Dias -> lista31Dias
            else -> lista29Dias
        }

        binding.spinnerDia.adapter = ArrayAdapter<Int>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            dias
        )


    }

    private suspend fun carregarDiaeMesSelecionado() {


        var retorno: Response<Date>? =null
        var diaSelecionado = binding.spinnerDia.selectedItem
        var mesSelecionado = binding.spinnerMes.selectedItemPosition + 1
        var day: Int = diaSelecionado.toString().toInt()
        var month:Int = mesSelecionado.toString().toInt()



        try {
            val diaEMesAPI = retrofitDateTrivia.create(DateAPI::class.java)
            retorno = diaEMesAPI.recuperarDataQuery(month, day)
            Log.i(TAG, "Postagens recuperadas")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Erro ao recuperar texto: ${e.message}")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {
                val curiosidadeDoDia = retorno.body()
                val mostrarTexto = "${curiosidadeDoDia?.text}"


                withContext(Dispatchers.Main) {

                    binding.textDay.text = day.toString()
                    binding.textMonth.text = binding.spinnerMes.selectedItem.toString()

                    binding.groupResultado.isVisible =true
                    binding.textConteudo.text = mostrarTexto
                }
            }
        }
    }
}


