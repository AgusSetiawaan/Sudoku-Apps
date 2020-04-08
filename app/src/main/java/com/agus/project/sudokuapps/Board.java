package com.agus.project.sudokuapps;

import java.util.List;

public class Board {

    private int size;
    private List<Cell> cells;
    private int[][] sudoku;

    public int[][] getSudoku() {
        return sudoku;
    }

    public void setSudoku(int[][] sudoku) {
        this.sudoku = sudoku;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Cell getCell(int row, int col){
        return cells.get(row*size+col);
    }
    public int getPos(int row, int col){
        return (row*size+col);
    }
}
