package com.example.minesweeper

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), GameEngine.GameListener {
    private lateinit var gridLayout: GridLayout
    private lateinit var gameEngine: GameEngine
    private lateinit var cellButtons: Array<Array<Button>>
    private lateinit var minesCountText: TextView
    private lateinit var statusText: TextView
    private lateinit var timer: Chronometer
    private lateinit var restartButton: Button
    private lateinit var difficultyButton: Button
    private var currentDifficulty = 0 // 0: Easy, 1: Medium, 2: Hard
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        minesCountText = findViewById(R.id.minesCountText)
        statusText = findViewById(R.id.statusText)
        timer = findViewById(R.id.timer)
        restartButton = findViewById(R.id.restartButton)
        difficultyButton = findViewById(R.id.difficultyButton)

        restartButton.setOnClickListener { startNewGame() }
        difficultyButton.setOnClickListener { showDifficultyDialog() }

        startNewGame()
    }

    private fun startNewGame() {
        gameEngine = GameEngine(currentDifficulty)
        gameEngine.setListener(this)
        createGrid()
        updateMinesCount()
        statusText.setText(R.string.game_status_playing)
        timerRunning = false
        timer.stop()
        timer.base = SystemClock.elapsedRealtime()
    }

    private fun createGrid() {
        gridLayout.removeAllViews()
        val rows = gameEngine.getRows()
        val cols = gameEngine.getCols()

        gridLayout.rowCount = rows
        gridLayout.columnCount = cols

        cellButtons = Array(rows) { Array(cols) { Button(this) } }

        val cellSize = resources.displayMetrics.widthPixels / cols - 10

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val button = cellButtons[i][j]
                button.layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(i, 1f),
                    GridLayout.spec(j, 1f)
                )
                button.minWidth = cellSize
                button.minHeight = cellSize
                button.textSize = 12f
                button.setPadding(0, 0, 0, 0)

                val row = i
                val col = j

                button.setOnClickListener {
                    if (!timerRunning) {
                        timer.base = SystemClock.elapsedRealtime()
                        timer.start()
                        timerRunning = true
                    }
                    gameEngine.revealCell(row, col)
                }

                button.setOnLongClickListener {
                    gameEngine.toggleFlag(row, col)
                    updateCell(row, col)
                    true
                }

                gridLayout.addView(button)
                updateCell(i, j)
            }
        }
    }

    private fun updateCell(row: Int, col: Int) {
        val button = cellButtons[row][col]
        val cell = gameEngine.getCell(row, col)

        when {
            cell.isFlagged -> {
                button.text = "🚩"
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
            }
            cell.isRevealed -> {
                if (cell.isMine) {
                    button.text = "💣"
                    button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
                } else {
                    if (cell.adjacentMines > 0) {
                        button.text = cell.adjacentMines.toString()
                        button.setTextColor(getNumberColor(cell.adjacentMines))
                    } else {
                        button.text = ""
                    }
                    button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                }
            }
            else -> {
                button.text = ""
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.background_light))
            }
        }
    }

    private fun getNumberColor(number: Int): Int {
        return when (number) {
            1 -> ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            2 -> ContextCompat.getColor(this, android.R.color.holo_green_dark)
            3 -> ContextCompat.getColor(this, android.R.color.holo_red_dark)
            4 -> ContextCompat.getColor(this, android.R.color.holo_purple)
            5 -> ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            6 -> ContextCompat.getColor(this, android.R.color.holo_red_light)
            7 -> ContextCompat.getColor(this, android.R.color.black)
            8 -> ContextCompat.getColor(this, android.R.color.darker_gray)
            else -> ContextCompat.getColor(this, android.R.color.black)
        }
    }

    private fun updateMinesCount() {
        val remaining = gameEngine.getTotalMines() - getFlaggedCount()
        minesCountText.text = "雷: $remaining"
    }

    private fun getFlaggedCount(): Int {
        var count = 0
        for (i in 0 until gameEngine.getRows()) {
            for (j in 0 until gameEngine.getCols()) {
                if (gameEngine.getCell(i, j).isFlagged) {
                    count++
                }
            }
        }
        return count
    }

    private fun showDifficultyDialog() {
        val difficulties = arrayOf("简单 (9x9, 10雷)", "中等 (16x16, 40雷)", "困难 (16x30, 99雷)")
        AlertDialog.Builder(this)
            .setTitle("选择难度")
            .setItems(difficulties) { _, which ->
                currentDifficulty = which
                startNewGame()
            }
            .show()
    }

    override fun onCellRevealed(row: Int, col: Int) {
        runOnUiThread {
            updateCell(row, col)
            if (gameEngine.isGameOver() || gameEngine.isGameWon()) {
                updateAllCells()
            }
        }
    }

    override fun onGameOver(won: Boolean) {
        runOnUiThread {
            timer.stop()
            timerRunning = false
            if (won) {
                statusText.setText(R.string.game_status_won)
                Toast.makeText(this, "恭喜！你赢了！", Toast.LENGTH_LONG).show()
            } else {
                statusText.setText(R.string.game_status_lost)
                gameEngine.revealAllMines()
                updateAllCells()
                Toast.makeText(this, "游戏结束！", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMinesCountChanged(remainingMines: Int) {
        runOnUiThread { updateMinesCount() }
    }

    private fun updateAllCells() {
        for (i in 0 until gameEngine.getRows()) {
            for (j in 0 until gameEngine.getCols()) {
                updateCell(i, j)
            }
        }
    }
}

