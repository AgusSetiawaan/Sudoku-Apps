package com.agus.project.sudokuapps.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.agus.project.sudokuapps.Cell;
import com.agus.project.sudokuapps.SudokuGame;

public class SudokuViewModel extends ViewModel {

    public SudokuGame sudokuGame = new SudokuGame();

    private MutableLiveData<Cell> cellData;

    public SudokuGame getSudokuGame() {
        return sudokuGame;
    }

    public void setSudokuGame(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }

    public MutableLiveData<Cell> getCellData() {
        return cellData;
    }

    public void setCellData(MutableLiveData<Cell> cellData) {
        this.cellData = cellData;
    }
}
