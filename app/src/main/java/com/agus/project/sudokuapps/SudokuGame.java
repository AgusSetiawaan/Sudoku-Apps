package com.agus.project.sudokuapps;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.util.Pair;

import com.agus.project.sudokuapps.View.SudokuBoxView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SudokuGame {

    public MutableLiveData<Pair<Integer,Integer>> selectedCellLiveData;
    public MutableLiveData<List<Cell>> cellsLiveData;
    public MutableLiveData<List<Integer>> sudokuLive;
    public MutableLiveData<List<Pair<Integer,Integer>>> errorCells;
//    public MutableLiveData<int[][]> sudoku;

    public int selectedRow = -1;
    public int selectedColumn = -1;

    private Board board = new Board();

    private FillError listener;

    private List<Cell2> visibleCell;

    List<Integer> Sudoku2;

    public SudokuGame() {
//        this.selectedCellLiveData = selectedCellLiveData;

        Sudoku2 = new ArrayList<>();

        int [][] Sudoku = SudokuGenerator.getInstance().generateGrid();

        for(int i = 0;i<Sudoku.length;i++){
            for(int j=0;j<Sudoku[0].length;j++){
                Sudoku2.add(Sudoku[i][j]);
            }
        }


        int[][]SudokuRemove = SudokuGenerator.getInstance().removeElements(Sudoku);

        List<Cell> cells= new ArrayList<>();
        visibleCell = new ArrayList<>();
        for(int i=0;i<SudokuRemove.length;i++){
            for(int j=0; j<SudokuRemove[0].length;j++){
                if(SudokuRemove[i][j]!=0){
                    cells.add(new Cell(i,j,SudokuRemove[i][j]));
                    visibleCell.add(new Cell2(i,j,SudokuRemove[i][j]));
                }
                else{
                    cells.add(new Cell(i,j,null));
                    visibleCell.add(new Cell2(i,j,null));
                    Log.e("ini value sudoku",String.valueOf(Sudoku2.get(9*i+j)));
//                    cellTemp.add(new Cell2(i,j,Sudoku2.get(9*i+j)));
                }



            }


        }

//        visibleCell = cells;


        board.setSize(9);
        board.setCells(cells);
        board.setSudoku(SudokuRemove);

        selectedCellLiveData = new MutableLiveData<>();
        cellsLiveData = new MutableLiveData<>();
        sudokuLive = new MutableLiveData<>();
        errorCells = new MutableLiveData<>();
//        sudoku = new MutableLiveData<>();
        selectedCellLiveData.postValue(Pair.create(selectedRow,selectedColumn));
        cellsLiveData.postValue(board.getCells());

        sudokuLive.postValue(Sudoku2);
        errorCells.postValue(null);
//        sudoku.postValue(SudokuRemove);






    }


    public List<Cell2> getVisibleCell() {
        return visibleCell;
    }

    public void handleInput(int number, SudokuBoxView sudokuBoxView, List<Cell> cells){

//        this.listener  = sudokuBoxView;

        if(selectedRow==-1 || selectedColumn==-1){
            return;
        }



        int j=0;
        for(Cell2 cell: visibleCell){
            if(cell.getValue()!=null){
                j++;
                if(cell.getRow()==selectedRow && cell.getCol()==selectedColumn){
                    return;
                }
            }
        }

        Log.e("ini size visible",String.valueOf(j));

        int[][] sudo = new int[9][9];
        int i=0;
        for(Cell cell: cells){
            if(cell.getValue()==null){
                sudo[cell.getRow()][cell.getCol()] = 0;
            }
            else{
                sudo[cell.getRow()][cell.getCol()] = cell.getValue();
                i++;
            }

        }

        Log.e("ini size nya",String.valueOf(i));

        if(number==0){
            board.getCell(selectedRow,selectedColumn).setValue(null);

//            cellsLiveData.setValue(board.getCells());
            cellsLiveData.postValue(board.getCells());
        }

        else{
            if(SudokuChecker.getInstance().checkHorizontalConflict(sudo,selectedRow,selectedColumn,number).size()!=0){
//                listener.horizontalError(SudokuChecker.getInstance().checkHorizontalConflict(sudo,selectedRow,selectedColumn,number));
//                errorCells.postValue();
            }
            else if(SudokuChecker.getInstance().checkVerticalConflict(sudo,selectedRow,selectedColumn,number)){
//            listener.verticalError(selectedRow,selectedColumn);
            }
            else if(SudokuChecker.getInstance().checkRegionConflict(sudo,selectedRow,selectedColumn,number)){
//            listener.regionError(selectedRow,selectedColumn);
            }
            else{
                board.getCell(selectedRow,selectedColumn).setValue(number);

//            cellsLiveData.setValue(board.getCells());
                cellsLiveData.postValue(board.getCells());



            }
        }




    }

    public void solveMe(List<Integer> listValue, List<Cell2> cells){

//        List<Cell> cells = new ArrayList<>();

        for(Cell2 cell:cells){
            if(cell.getValue()==null){
                board.getCell(cell.getRow(),cell.getCol()).setValue(listValue.get(cell.getRow()*9+cell.getCol()));
            }
        }




        cellsLiveData.postValue(board.getCells());



    }

    public void newGame(){
        int[][] newSudo = SudokuGenerator.getInstance().generateGrid();

        List<Integer> sudokuNew = new ArrayList<>();

        for(int i = 0;i<newSudo.length;i++){
            for(int j=0;j<newSudo[0].length;j++){
                sudokuNew.add(newSudo[i][j]);
            }
        }
        newSudo = SudokuGenerator.getInstance().removeElements(newSudo);

        visibleCell = new ArrayList<>();

        for(int i=0;i<newSudo.length;i++){
            for(int j=0;j<newSudo[0].length;j++){
                if(newSudo[i][j]!=0){
                    board.getCell(i,j).setValue(newSudo[i][j]);
                    visibleCell.add(new Cell2(i,j,newSudo[i][j]));
                }
                else{
                    board.getCell(i,j).setValue(null);
                    visibleCell.add(new Cell2(i,j,null));

                }

            }
        }



        cellsLiveData.postValue(board.getCells());
        sudokuLive.postValue(sudokuNew);
    }

    public void updateSelectedCell(int row, int col){
        selectedRow = row;
        selectedColumn = col;
        selectedCellLiveData.postValue(Pair.create(row,col));
    }


    public interface FillError{
        void horizontalError(List<Cell2> cell);
        void verticalError(int row, int column);
        void regionError(int row, int column);
    }

    public interface Visible{
        void colorVisible(List<Cell> visibleCells);
    }
}
