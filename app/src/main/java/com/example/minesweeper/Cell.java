package com.example.minesweeper;

public class Cell {
    private boolean isRevealed;
    private boolean isMine;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell() {
        this.isRevealed = false;
        this.isMine = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}

