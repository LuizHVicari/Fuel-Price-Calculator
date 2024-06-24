package com.github.fuelpricecalculator

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.fuelpricecalculator.databinding.ActivityFuelAutonomyBinding
import java.util.Locale

class FuelAutonomy : AppCompatActivity() {

    private lateinit var binding : ActivityFuelAutonomyBinding
    private var valueFuel1 = 0.0f
    private var valueFuel2 = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFuelAutonomyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        valueFuel1 = intent.getFloatExtra("valueFuel1", 0.0f)
        valueFuel2 = intent.getFloatExtra("valueFuel2", 0.0f)

        val distancePerCost1 = 1.0 / valueFuel1
        val distancePerCost2 = 1.0 / valueFuel2

        if (distancePerCost1 > distancePerCost2) {
            binding.llFuel1.layoutParams.width = dpToPixel(300)
            binding.llFuel2.layoutParams.width = dpToPixel((300 * distancePerCost2 / distancePerCost1).toInt())

        } else {
            binding.llFuel2.layoutParams.width = dpToPixel(300)
            binding.llFuel1.layoutParams.width = dpToPixel((300 * distancePerCost1 / distancePerCost2).toInt())
        }

        binding.tvFuel1.text = getString(R.string.fuel_1_autonomy, String.format(Locale.getDefault(), "%.2f", distancePerCost1 * 100.0))
        binding.tvFuel2.text = getString(R.string.fuel_2_autonomy, String.format(Locale.getDefault(), "%.2f", distancePerCost2 * 100.0))
    }

    private fun dpToPixel(dp: Int): Int {
        val scale = resources.displayMetrics.density
        val pixels = (dp * scale + 0.5f).toInt()
        return pixels
    }
}