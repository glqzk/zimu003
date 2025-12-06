package com.example.minesweeper

data class Cell(
    var isRevealed: Boolean = false,
    var isMine: Boolean = false,
    var isFlagged: Boolean = false,
    var adjacentMines: Int = 0
)

