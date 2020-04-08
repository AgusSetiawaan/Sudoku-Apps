package com.agus.project.sudokuapps;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.agus.project.sudokuapps.View.SudokuBoxView;
import com.agus.project.sudokuapps.ViewModel.SudokuViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SudokuBoxView.OnTouchListener, SudokuGame.FillError{

    private SudokuViewModel viewModel;

    private SudokuBoxView sudokuBoxView;
    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;
    private Button buttonFive;
    private Button buttonSix;
    private Button buttonSeven;
    private Button buttonEight;
    private Button buttonNine;
    private Button buttonDelete;

    Chronometer cmTimer;

    private Button[] listButton;
    private Button solveMe;
    private Button btnNewGame;

    private List<Cell> cells, cellTemp;

    private List<Integer> listValue;

    boolean resume = false;

    long elapsedTime;

    boolean firstClick = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cellTemp = new ArrayList<>();

        sudokuBoxView = findViewById(R.id.sudokuBoxView);
        buttonOne = findViewById(R.id.buttonOne);
        buttonTwo = findViewById(R.id.buttonTwo);
        buttonThree = findViewById(R.id.buttonThree);
        buttonFour = findViewById(R.id.buttonFour);
        buttonFive = findViewById(R.id.buttonFive);
        buttonSix = findViewById(R.id.buttonSix);
        buttonSeven = findViewById(R.id.buttonSeven);
        buttonEight = findViewById(R.id.buttonEight);
        buttonNine = findViewById(R.id.buttonNine);
        buttonDelete = findViewById(R.id.buttonDelete);
        solveMe = findViewById(R.id.buttonSolve);
        btnNewGame = findViewById(R.id.btnNewGame);

        cmTimer = findViewById(R.id.timer);

        listButton = new Button[]{buttonOne,buttonTwo, buttonThree, buttonFour,
                buttonFive,buttonSix,buttonSeven,buttonEight,buttonNine,buttonDelete};

        sudokuBoxView.registerListener(this);

        viewModel = ViewModelProviders.of(this).get(SudokuViewModel.class);

        colorBox(viewModel.sudokuGame.getVisibleCell());

//        cmTimer.start();

        viewModel.sudokuGame.selectedCellLiveData.observe(this, new Observer<Pair<Integer, Integer>>() {
            @Override
            public void onChanged(@Nullable Pair<Integer, Integer> integerIntegerPair) {
                updateSelectedCellUI(integerIntegerPair);
            }
        });
        viewModel.sudokuGame.cellsLiveData.observe(this, new Observer<List<Cell>>() {
            @Override
            public void onChanged(@Nullable List<Cell> cells) {
                updateCells(cells);

            }
        });

        viewModel.sudokuGame.sudokuLive.observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable List<Integer> integers) {
                updateNullCell(integers);
            }
        });

        viewModel.sudokuGame.errorCells.observe(this, new Observer<List<Pair<Integer, Integer>>>() {
            @Override
            public void onChanged(@Nullable List<Pair<Integer, Integer>> pairs) {
                errorCells(pairs);
            }
        });


        for(int i=0;i<listButton.length;i++){
            int finalI = i;
            listButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firstClick==true){
                        if (!resume) {
                            cmTimer.setBase(SystemClock.elapsedRealtime());
                            cmTimer.start();
                        } else {
                            cmTimer.start();
                        }
                        firstClick=false;
                    }
                    if(finalI!=9){
                        viewModel.sudokuGame.handleInput(finalI +1, sudokuBoxView, cells);
                    }
                    else{
                        viewModel.sudokuGame.handleInput(0, sudokuBoxView, cells);
                    }

                }
            });
        }


        solveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sudokuGame.solveMe(listValue,viewModel.sudokuGame.getVisibleCell());
                for(int i=0;i<listButton.length;i++){
                    listButton[i].setEnabled(false);
                }
//                buttonDelete.setEnabled( qfalse);
                cmTimer.stop();
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sudokuGame.newGame();
                colorBox(viewModel.sudokuGame.getVisibleCell());

                cmTimer.stop();
                cmTimer.setText("00:00");
                resume = false;
                firstClick = true;

                for(int i=0;i<listButton.length;i++){
                    listButton[i].setEnabled(true);
                }
            }
        });

        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (!resume) {
                    long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    Log.d("tes", "onChronometerTick: " + minutes + " : " + seconds);
                } else {
                    long minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                    Log.d("tes", "onChronometerTick: " + minutes + " : " + seconds);
                }
            }
        });

//        cmTimer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cmTimer.stop();
//                resume = true;
//            }
//        });





    }

    private void updateSelectedCellUI(Pair<Integer,Integer> cell){
        sudokuBoxView.updateSelectedUI(cell.first,cell.second);

    }

    private void updateCells(List<Cell> cells){
        sudokuBoxView.updateCells(cells);

        this.cells = cells;
    }


    private void updateNullCell(List<Integer> sudoku){
        listValue = sudoku;
    }

    private void colorBox(List<Cell2> cells){
        sudokuBoxView.colorVisible(cells);

    }

    private void errorCells(List<Pair<Integer,Integer>> cell){
        sudokuBoxView.errorColor(cell);
    }


    @Override
    public void onCellTouched(int row, int col) {
        viewModel.sudokuGame.updateSelectedCell(row,col);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public void horizontalError(List<Cell2> cell) {

    }

    @Override
    public void verticalError(int row, int column) {

    }

    @Override
    public void regionError(int row, int column) {

    }
}
