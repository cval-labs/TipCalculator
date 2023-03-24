package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.NumberPicker
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tipcalculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val seekBar get() = binding.seekBar
    private val billAmount get() = binding.billAmount
    private val numberPicker get() = binding.partyNum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSeekBarListener()
        setupBaseAmount()
        setupNumberPickerListener()
    }

    private fun setupNumberPickerListener() {
        numberPicker.minValue = 1
        numberPicker.maxValue = 6
        numberPicker.wrapSelectorWheel = true
        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("DEBUG", "Selected number in picker is $newVal")
            tipAndTotalCalc() // Calculates tip and total amounts as user changes party value
        }


    }

    private fun setupSeekBarListener() {
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentSign = "%"
                binding.tipPercentage.text = String.format("%d%s", progress, percentSign)
                tipAndTotalCalc() // Calculates tip and total amounts as seekbar progress is changed

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    private fun setupBaseAmount() {
        billAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            override fun afterTextChanged(s: Editable?) {
                // Fires right after the text has changed
                Log.i("setupBaseAmount()", "afterTextChanged $s")
                tipAndTotalCalc() // Calculates tip and total amounts as user enters numbers
            }

        })
    }

    private fun tipAndTotalCalc() {
        val userBillAmount: Double
        val percentage: Int
        val tip: Double
        val total: Double
        val default = "0.00"

        // Sets default string values when EditText is empty
        if (billAmount.text.isEmpty()) {
            binding.tipAmount.text = default
            binding.totalAmount.text = default
            return
        }

        // Calculates tip and total
        if (numberPicker.value != 1) {
            userBillAmount = billAmount.text.toString().toDouble()
            percentage = seekBar.progress
            tip = (userBillAmount * percentage / 100) / (numberPicker.value)
            total = (userBillAmount + (userBillAmount * percentage / 100))  / (numberPicker.value)
            // Round to 2 decimal places
            binding.tipAmount.text = String.format("%.2f", tip)
            binding.totalAmount.text = String.format("%.2f", total)

        }
        else {
            userBillAmount = billAmount.text.toString().toDouble()
            percentage = seekBar.progress
            tip = userBillAmount * percentage / 100
            total = userBillAmount + tip
            binding.tipAmount.text = String.format("%.2f", tip) // outputs 2 decimal places
            binding.totalAmount.text = String.format("%.2f", total)
        }
    }

}