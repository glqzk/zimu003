package com.example.minesweeper

import kotlin.random.Random

class GameEngine(private val difficulty: Int) {
    companion object {
        const val EASY_ROWS = 9
        const val EASY_COLS = 9
        const val EASY_MINES = 10

        const val MEDIUM_ROWS = 16
        const val MEDIUM_COLS = 16
        const val MEDIUM_MINES = 40

        const val HARD_ROWS = 16
        const val HARD_COLS = 30
        const val HARD_MINES = 99
    }

    interface GameListener {
        fun onCellRevealed(row: Int, col: Int)
        fun onGameOver(won: Boolean)
        fun onMinesCountChanged(remainingMines: Int)
    }

    private val grid: Array<Array<Cell>>
    private val rows: Int
    private val cols: Int
    private val totalMines: Int
    private var revealedCells = 0
    private var flaggedMines = 0
    private var gameOver = false
    private var gameWon = false
    private var firstClick = true
    private val random = Random.Default

    private var listener: GameListener? = null

    init {
        val config = when (difficulty) {
            0 -> Triple(EASY_ROWS, EASY_COLS, EASY_MINES)
            1 -> Triple(MEDIUM_ROWS, MEDIUM_COLS, MEDIUM_MINES)
            2 -> Triple(HARD_ROWS, HARD_COLS, HARD_MINES)
            else -> Triple(EASY_ROWS, EASY_COLS, EASY_MINES)
        }
        rows = config.first
        cols = config.second
        totalMines = config.third

        grid = Array(rows) { Array(cols) { Cell() } }
    }

    fun setListener(listener: GameListener?) {
        this.listener = listener
    }

    fun generateMines(excludeRow: Int, excludeCol: Int) {
        var minesPlaced = 0
        while (minesPlaced < totalMines) {
            val row = random.nextInt(rows)
            val col = random.nextInt(cols)

            // 确保第一次点击的位置不是雷
            if (row == excludeRow && col == excludeCol) {
                continue
            }

            if (!grid[row][col].isMine) {
                grid[row][col].isMine = true
                minesPlaced++
            }
        }

        // 计算每个格子周围的雷数
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (!grid[i][j].isMine) {
                    grid[i][j].adjacentMines = countAdjacentMines(i, j)
                }
            }
        }
    }

    private fun countAdjacentMines(row: Int, col: Int): Int {
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = row + i
                val newCol = col + j
                if (newRow in 0 until rows && newCol in 0 until cols) {
                    if (grid[newRow][newCol].isMine) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun revealCell(row: Int, col: Int) {
        if (gameOver || gameWon) return

        val cell = grid[row][col]
        if (cell.isRevealed || cell.isFlagged) return

        if (firstClick) {
            generateMines(row, col)
            firstClick = false
        }

        cell.isRevealed = true
        revealedCells++

        if (cell.isMine) {
            gameOver = true
            listener?.onGameOver(false)
            return
        }

        listener?.onCellRevealed(row, col)

        // 如果周围没有雷，自动展开
        if (cell.adjacentMines == 0) {
            revealAdjacentCells(row, col)
        }

        // 检查是否获胜
        if (revealedCells == (rows * cols - totalMines)) {
            gameWon = true
            listener?.onGameOver(true)
        }
    }

    private fun revealAdjacentCells(row: Int, col: Int) {
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = row + i
                val newCol = col + j
                if (newRow in 0 until rows && newCol in 0 until cols) {
                    val cell = grid[newRow][newCol]
                    if (!cell.isRevealed && !cell.isFlagged && !cell.isMine) {
                        cell.isRevealed = true
                        revealedCells++
                        listener?.onCellRevealed(newRow, newCol)
                        if (cell.adjacentMines == 0) {
                            revealAdjacentCells(newRow, newCol)
                        }
                    }
                }
            }
        }
    }

    fun toggleFlag(row: Int, col: Int) {
        if (gameOver || gameWon) return

        val cell = grid[row][col]
        if (cell.isRevealed) return

        cell.isFlagged = !cell.isFlagged
        if (cell.isFlagged) {
            flaggedMines++
        } else {
            flaggedMines--
        }

        listener?.onMinesCountChanged(totalMines - flaggedMines)
    }

    fun getCell(row: Int, col: Int): Cell = grid[row][col]

    fun getRows(): Int = rows

    fun getCols(): Int = cols

    fun getTotalMines(): Int = totalMines

    fun isGameOver(): Boolean = gameOver

    fun isGameWon(): Boolean = gameWon

    fun revealAllMines() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (grid[i][j].isMine) {
                    grid[i][j].isRevealed = true
                    listener?.onCellRevealed(i, j)
                }
            }
        }
    }
}

