package com.example.minesweeper;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements GameEngine.GameListener {
    private GridLayout gridLayout;
    private GameEngine gameEngine;
    private Button[][] cellButtons;
    private TextView minesCountText;
    private TextView statusText;
    private Chronometer timer;
    private Button restartButton;
    private Button difficultyButton;
    private int currentDifficulty = 0; // 0: Easy, 1: Medium, 2: Hard
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        minesCountText = findViewById(R.id.minesCountText);
        statusText = findViewById(R.id.statusText);
        timer = findViewById(R.id.timer);
        restartButton = findViewById(R.id.restartButton);
        difficultyButton = findViewById(R.id.difficultyButton);

        restartButton.setOnClickListener(v -> startNewGame());
        difficultyButton.setOnClickListener(v -> showDifficultyDialog());

        startNewGame();
    }

    private void startNewGame() {
        gameEngine = new GameEngine(currentDifficulty);
        gameEngine.setListener(this);
        createGrid();
        updateMinesCount();
        statusText.setText(getString(R.string.game_status_playing));
        timerRunning = false;
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
    }

    private void createGrid() {
        gridLayout.removeAllViews();
        int rows = gameEngine.getRows();
        int cols = gameEngine.getCols();
        
        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(cols);
        
        cellButtons = new Button[rows][cols];
        
        int cellSize = getResources().getDisplayMetrics().widthPixels / cols - 10;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(i, 1f),
                    GridLayout.spec(j, 1f)
                ));
                button.setMinWidth(cellSize);
                button.setMinHeight(cellSize);
                button.setTextSize(12);
                button.setPadding(0, 0, 0, 0);
                
                final int row = i;
                final int col = j;
                
                button.setOnClickListener(v -> {
                    if (!timerRunning) {
                        timer.setBase(SystemClock.elapsedRealtime());
                        timer.start();
                        timerRunning = true;
                    }
                    gameEngine.revealCell(row, col);
                });
                
                button.setOnLongClickListener(v -> {
                    gameEngine.toggleFlag(row, col);
                    updateCell(row, col);
                    return true;
                });
                
                cellButtons[i][j] = button;
                gridLayout.addView(button);
                updateCell(i, j);
            }
        }
    }

    private void updateCell(int row, int col) {
        Button button = cellButtons[row][col];
        Cell cell = gameEngine.getCell(row, col);
        
        if (cell.isFlagged()) {
            button.setText("🚩");
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
        } else if (cell.isRevealed()) {
            if (cell.isMine()) {
                button.setText("💣");
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            } else {
                int adjacentMines = cell.getAdjacentMines();
                if (adjacentMines > 0) {
                    button.setText(String.valueOf(adjacentMines));
                    button.setTextColor(getNumberColor(adjacentMines));
                } else {
                    button.setText("");
                }
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            }
        } else {
            button.setText("");
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.background_light));
        }
    }

    private int getNumberColor(int number) {
        switch (number) {
            case 1: return ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            case 2: return ContextCompat.getColor(this, android.R.color.holo_green_dark);
            case 3: return ContextCompat.getColor(this, android.R.color.holo_red_dark);
            case 4: return ContextCompat.getColor(this, android.R.color.holo_purple);
            case 5: return ContextCompat.getColor(this, android.R.color.holo_orange_dark);
            case 6: return ContextCompat.getColor(this, android.R.color.holo_red_light);
            case 7: return ContextCompat.getColor(this, android.R.color.black);
            case 8: return ContextCompat.getColor(this, android.R.color.darker_gray);
            default: return ContextCompat.getColor(this, android.R.color.black);
        }
    }

    private void updateMinesCount() {
        int remaining = gameEngine.getTotalMines() - getFlaggedCount();
        minesCountText.setText(String.format("雷: %d", remaining));
    }

    private int getFlaggedCount() {
        int count = 0;
        for (int i = 0; i < gameEngine.getRows(); i++) {
            for (int j = 0; j < gameEngine.getCols(); j++) {
                if (gameEngine.getCell(i, j).isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void showDifficultyDialog() {
        String[] difficulties = {"简单 (9x9, 10雷)", "中等 (16x16, 40雷)", "困难 (16x30, 99雷)"};
        new AlertDialog.Builder(this)
            .setTitle("选择难度")
            .setItems(difficulties, (dialog, which) -> {
                currentDifficulty = which;
                startNewGame();
            })
            .show();
    }

    @Override
    public void onCellRevealed(int row, int col) {
        runOnUiThread(() -> {
            updateCell(row, col);
            if (gameEngine.isGameOver() || gameEngine.isGameWon()) {
                updateAllCells();
            }
        });
    }

    @Override
    public void onGameOver(boolean won) {
        runOnUiThread(() -> {
            timer.stop();
            timerRunning = false;
            if (won) {
                statusText.setText(getString(R.string.game_status_won));
                Toast.makeText(this, "恭喜！你赢了！", Toast.LENGTH_LONG).show();
            } else {
                statusText.setText(getString(R.string.game_status_lost));
                gameEngine.revealAllMines();
                updateAllCells();
                Toast.makeText(this, "游戏结束！", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMinesCountChanged(int remainingMines) {
        runOnUiThread(() -> updateMinesCount());
    }

    private void updateAllCells() {
        for (int i = 0; i < gameEngine.getRows(); i++) {
            for (int j = 0; j < gameEngine.getCols(); j++) {
                updateCell(i, j);
            }
        }
    }
}

