package com.agus.project.sudokuapps;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuChecker {
    private static SudokuChecker instance;

    private SudokuChecker(){}

    public static SudokuChecker getInstance(){
        if( instance == null ){
            instance = new SudokuChecker();
        }
        return instance;
    }

//    public boolean checkConflict( int[][] Sudoku , int currentPos , final int number){
//        int xPos = currentPos % 9;
//        int yPos = currentPos / 9;
//
//        if( checkHorizontalConflict(Sudoku, xPos, yPos, number) || checkVerticalConflict(Sudoku, xPos, yPos, number) || checkRegionConflict(Sudoku, xPos, yPos, number) ){
//            return true;
//        }
//
//        return false;
//    }

    /**
     * Return true if there is a conflict
     * @param Sudoku
     * @param xPos
     * @param yPos
     * @param number
     * @return
     */
    public List<Pair<Integer,Integer>> checkHorizontalConflict(final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        List<Pair<Integer,Integer>> list = new ArrayList<>();
        for( int x = 0; x < 9 ; x++ ){
            if( number == Sudoku[x][yPos]){
                Pair<Integer,Integer> cell = new Pair<>(x,yPos);
                list.add(cell);
            }
        }

        return list;
    }

    public boolean checkHorizontalConflict(final List<Cell> cells , final Integer row , final Integer col , final Integer number ){
        for( Cell cell: cells ){
            if(cell.getRow()==row){
                if( cell.getValue() == number){
                    return true;
                }
            }

        }

        return false;
    }

    public boolean checkVerticalConflict( final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        for( int y = 0; y < 9 ; y++ ){
            if( number == Sudoku[xPos][y] ){
                return true;
            }
        }

        return false;
    }



    public boolean checkRegionConflict( final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        int xRegion = xPos / 3;
        int yRegion = yPos / 3;

        for( int x = xRegion * 3 ; x < xRegion * 3 + 3 ; x++ ){
            for( int y = yRegion * 3 ; y < yRegion * 3 + 3 ; y++ ){
                if( ( x != xPos || y != yPos ) && number == Sudoku[x][y] ){
                    return true;
                }
            }
        }

        return false;
    }
}
