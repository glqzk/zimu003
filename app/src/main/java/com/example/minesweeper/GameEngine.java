package com.example.minesweeper;

import java.util.Random;

public class GameEngine {
    public static final int EASY_ROWS = 9;
    public static final int EASY_COLS = 9;
    public static final int EASY_MINES = 10;

    public static final int MEDIUM_ROWS = 16;
    public static final int MEDIUM_COLS = 16;
    public static final int MEDIUM_MINES = 40;

    public static final int HARD_ROWS = 16;
    public static final int HARD_COLS = 30;
    public static final int HARD_MINES = 99;

    private Cell[][] grid;
    private int rows;
    private int cols;
    private int totalMines;
    private int revealedCells;
    private int flaggedMines;
    private boolean gameOver;
    private boolean gameWon;
    private boolean firstClick;
    private Random random;

    public interface GameListener {
        void onCellRevealed(int row, int col);
        void onGameOver(boolean won);
        void onMinesCountChanged(int remainingMines);
    }

    private GameListener listener;

    public GameEngine(int difficulty) {
        random = new Random();
        firstClick = true;
        gameOver = false;
        gameWon = false;
        revealedCells = 0;
        flaggedMines = 0;

        switch (difficulty) {
            case 0: // Easy
                rows = EASY_ROWS;
                cols = EASY_COLS;
                totalMines = EASY_MINES;
                break;
            case 1: // Medium
                rows = MEDIUM_ROWS;
                cols = MEDIUM_COLS;
                totalMines = MEDIUM_MINES;
                break;
            case 2: // Hard
                rows = HARD_ROWS;
                cols = HARD_COLS;
                totalMines = HARD_MINES;
                break;
            default:
                rows = EASY_ROWS;
                cols = EASY_COLS;
                totalMines = EASY_MINES;
        }

        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public void generateMines(int excludeRow, int excludeCol) {
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            
            // 确保第一次点击的位置不是雷
            if (row == excludeRow && col == excludeCol) {
                continue;
            }
            
            if (!grid[row][col].isMine()) {
                grid[row][col].setMine(true);
                minesPlaced++;
            }
        }
        
        // 计算每个格子周围的雷数
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].isMine()) {
                    int count = countAdjacentMines(i, j);
                    grid[i][j].setAdjacentMines(count);
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    if (grid[newRow][newCol].isMine()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void revealCell(int row, int col) {
        if (gameOver || gameWon) {
            return;
        }

        Cell cell = grid[row][col];
        if (cell.isRevealed() || cell.isFlagged()) {
            return;
        }

        if (firstClick) {
            generateMines(row, col);
            firstClick = false;
        }

        cell.setRevealed(true);
        revealedCells++;

        if (cell.isMine()) {
            gameOver = true;
            if (listener != null) {
                listener.onGameOver(false);
            }
            return;
        }

        if (listener != null) {
            listener.onCellRevealed(row, col);
        }

        // 如果周围没有雷，自动展开
        if (cell.getAdjacentMines() == 0) {
            revealAdjacentCells(row, col);
        }

        // 检查是否获胜
        if (revealedCells == (rows * cols - totalMines)) {
            gameWon = true;
            if (listener != null) {
                listener.onGameOver(true);
            }
        }
    }

    private void revealAdjacentCells(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    Cell cell = grid[newRow][newCol];
                    if (!cell.isRevealed() && !cell.isFlagged() && !cell.isMine()) {
                        cell.setRevealed(true);
                        revealedCells++;
                        if (listener != null) {
                            listener.onCellRevealed(newRow, newCol);
                        }
                        if (cell.getAdjacentMines() == 0) {
                            revealAdjacentCells(newRow, newCol);
                        }
                    }
                }
            }
        }
    }

    public void toggleFlag(int row, int col) {
        if (gameOver || gameWon) {
            return;
        }

        Cell cell = grid[row][col];
        if (cell.isRevealed()) {
            return;
        }

        cell.setFlagged(!cell.isFlagged());
        if (cell.isFlagged()) {
            flaggedMines++;
            if (cell.isMine()) {
                // 正确标记的雷
            }
        } else {
            flaggedMines--;
        }

        if (listener != null) {
            listener.onMinesCountChanged(totalMines - flaggedMines);
        }
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void revealAllMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].isMine()) {
                    grid[i][j].setRevealed(true);
                    if (listener != null) {
                        listener.onCellRevealed(i, j);
                    }
                }
            }
        }
    }
}

