package com.agus.project.sudokuapps;

public class Cell {

    private Integer row;
    private Integer col;
    private Integer value;

    public Cell(Integer row, Integer col, Integer value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
