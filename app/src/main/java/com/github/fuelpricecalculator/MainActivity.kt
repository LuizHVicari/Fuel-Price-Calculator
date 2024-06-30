package com.github.fuelpricecalculator

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.fuelpricecalculator.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var gasolineLanguages : Array<String>
    private lateinit var ethanolLanguages : Array<String>
    private lateinit var naturalGasLanguages : Array<String>
    private lateinit var customLanguages : Array<String>
    private var valueFuel1 = 0.0f
    private var valueFuel2 = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gasolineLanguages = arrayOf("Gasoline", "Gasolina")
        ethanolLanguages = arrayOf("Ethanol", "Etanol")
        naturalGasLanguages = arrayOf("Natural Gas", "Gás Natural")
        customLanguages = arrayOf("Custom", "Personalizado")


        binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView( binding.root )

        val systemLanguage = Locale.getDefault().language
        val spinnerArray = if (systemLanguage == "pt"){
            R.array.fuels_pt
        } else {
            R.array.fuels_en
        }

        ArrayAdapter.createFromResource(
            this,
            spinnerArray ,
            R.layout.spinner_item).also {
                adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                binding.spFuels1.adapter = adapter
                binding.spFuels2.adapter = adapter
        }

        binding.btCalculate.setOnClickListener{
            btCalculateOnClickListener()
        }

        binding.btShowDifference.setOnClickListener {
            btShowDifferenceOnClick()
        }

        binding.tvTitle.setOnClickListener{
            tvTitleOnClick()
        }
    }

    private fun tvTitleOnClick(){
        binding.tfConsumption1.hint = ""
        binding.tfConsumption1.setText("")
        binding.tfConsumption2.hint = ""
        binding.tfConsumption2.setText("")
        binding.tfCost1.hint = ""
        binding.tfCost1.setText("")
        binding.tfCost2.hint = ""
        binding.tfCost2.setText("")
    }

    private fun btShowDifferenceOnClick() {
        val intent = Intent(this, FuelAutonomy::class.java)
        val b = Bundle()
        b.putFloat("valueFuel1", valueFuel1)
        b.putFloat("valueFuel2", valueFuel2)
        intent.putExtras(b)
        startActivity(intent)
    }

    private fun btCalculateOnClickListener(){
        val spinnerItem1 = binding.spFuels1.selectedItem.toString()
        val spinnerItem2 = binding.spFuels2.selectedItem.toString()

        val customFuelConsumption1 = binding.tiConsumption1.editText?.text.toString().toFloatOrNull()
        val customFuelConsumption2 = binding.tiConsumption2.editText?.text.toString().toFloatOrNull()

        val cost1 = binding.tiCost1.editText?.text.toString().toFloatOrNull()
        val cost2 = binding.tiCost2.editText?.text.toString().toFloatOrNull()

        if (!validateConsumption(spinnerItem1, customFuelConsumption1, binding.tiConsumption1)
            ||
            !validateConsumption(spinnerItem2, customFuelConsumption2, binding.tiConsumption2)
            ||
            !validateCost(cost1, binding.tiCost1) || !validateCost(cost2, binding.tiCost2)
            ) {
            return
        }

        val fuelConsumption1 = customFuelConsumption1 ?: getFuelConsumption(spinnerItem1)
        val fuelConsumption2 = customFuelConsumption2 ?: getFuelConsumption(spinnerItem2)

        valueFuel1 = ((1.0 / fuelConsumption1) * cost1!!).toFloat()
        valueFuel2 = ((1.0 / fuelConsumption2) * cost2!!).toFloat()

        if (valueFuel1 > valueFuel2) {
            binding.tvResult.text = getString(R.string.the_best_fuel_option_is_s, "2")
        } else if (valueFuel1 < valueFuel2) {
            binding.tvResult.text = getString(R.string.the_best_fuel_option_is_s, "1")
        } else {
            binding.tvResult.text = getString(R.string.fuel_option_does_not_matter)
        }
        binding.tvResult.visibility = View.VISIBLE
        binding.btShowDifference.visibility = View.VISIBLE
        binding.btShowDifference.isClickable = true

    }

    private fun getFuelConsumption(spinnerValue: String): Float{
        return if (gasolineLanguages.contains(spinnerValue)) {
            15.0f
        } else if (ethanolLanguages.contains(spinnerValue)) {
            10.0f
        } else if (naturalGasLanguages.contains(spinnerValue)) {
            12.0f
        } else {
            0.0f
        }
    }

    private fun createAlert(title: String, message: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()
    }

    private fun  validateConsumption(spinnerItem : String, consumption: Float?, textInput: TextInputLayout): Boolean {
        if (customLanguages.contains(spinnerItem) && consumption == null) {
            textInput.requestFocus()
            createAlert(getString(R.string.comsumption_not_found), getString(R.string.consumption_error_message))
            return false
        }
        return true
    }

    private fun validateCost(cost : Float?, textInput: TextInputLayout): Boolean {
        if (cost == null) {
            textInput.requestFocus()
            createAlert(getString(R.string.cost_not_found),getString(R.string.cost_error))
            return false
        }
        return true
    }
}

