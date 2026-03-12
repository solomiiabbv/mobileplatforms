package com.example.pr1

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private val colors = listOf(Color.RED, Color.YELLOW, Color.GREEN)
    private val buttons = mutableListOf<Button>()

    private var currentLevel = 1
    private val columns = 3
    private val rows = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(gridLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createGrid()
    }

    private fun createGrid() {
        gridLayout.removeAllViews()
        buttons.clear()

        for (i in 0 until rows * columns) {
            val button = Button(this)

            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = 0
            params.columnSpec = GridLayout.spec(i % columns, 1f)
            params.rowSpec = GridLayout.spec(i / columns, 1f)
            params.setMargins(8, 8, 8, 8)

            button.layoutParams = params
            button.setBackgroundColor(randomColor())

            button.setOnClickListener {
                changeColor(button)
                checkLevel()
            }

            buttons.add(button)
            gridLayout.addView(button)
        }
    }

    private fun randomColor(): Int {
        return colors[Random.nextInt(colors.size)]
    }

    private fun changeColor(button: Button) {
        val currentColor =
            (button.background as android.graphics.drawable.ColorDrawable).color

        val nextColor = when (currentColor) {
            Color.RED -> Color.YELLOW
            Color.YELLOW -> Color.GREEN
            else -> Color.RED
        }

        button.setBackgroundColor(nextColor)
    }



    private fun checkLevel() {
        val passed = when (currentLevel) {
            1 -> checkAllSame()
            2 -> checkColumnsPattern()
            3 -> checkOneRedPerRow()
            4 -> checkNoSameNeighbors()
            else -> false
        }

        if (passed) {
            showLevelDialog()
        }
    }

    // 1 рівень
    private fun checkAllSame(): Boolean {
        val firstColor = getColor(buttons[0])
        return buttons.all { getColor(it) == firstColor }
    }

    // 2 рівень
    private fun checkColumnsPattern(): Boolean {
        for (i in buttons.indices) {
            val column = i % columns
            val expectedColor = when (column) {
                0 -> Color.YELLOW
                1 -> Color.RED
                else -> Color.GREEN
            }
            if (getColor(buttons[i]) != expectedColor) return false
        }
        return true
    }

    // 3 рівень
    private fun checkOneRedPerRow(): Boolean {
        for (row in 0 until rows) {
            var redCount = 0
            for (col in 0 until columns) {
                val index = row * columns + col
                if (getColor(buttons[index]) == Color.RED) {
                    redCount++
                }
            }
            if (redCount != 1) return false
        }
        return true
    }

    // 4 рівень
    private fun checkNoSameNeighbors(): Boolean {
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val index = row * columns + col
                val currentColor = getColor(buttons[index])
                if (col < columns - 1) {
                    val right = getColor(buttons[index + 1])
                    if (currentColor == right) return false
                }


                if (row < rows - 1) {
                    val bottom = getColor(buttons[index + columns])
                    if (currentColor == bottom) return false
                }
            }
        }
        return true
    }

    private fun getColor(button: Button): Int {
        return (button.background as android.graphics.drawable.ColorDrawable).color
    }

    private fun showLevelDialog() {
        AlertDialog.Builder(this)
            .setTitle("Рівень $currentLevel пройдено!")
            .setMessage("Що хочеш зробити далі?")
            .setPositiveButton("Наступний рівень") { _, _ ->
                if (currentLevel < 4) {
                    currentLevel++
                    createGrid()
                } else {
                    currentLevel = 1
                    createGrid()
                }
            }
            .setNeutralButton("Почати спочатку") { _, _ ->
                currentLevel = 1
                createGrid()
            }
            .setNegativeButton("Закрити") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}