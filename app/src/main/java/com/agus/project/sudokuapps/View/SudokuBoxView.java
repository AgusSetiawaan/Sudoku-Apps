package com.agus.project.sudokuapps.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.agus.project.sudokuapps.Cell;
import com.agus.project.sudokuapps.Cell2;
import com.agus.project.sudokuapps.SudokuGame;

import java.util.List;

public class SudokuBoxView extends View  {

    private Paint garisTebalPaint =new Paint();
    private Paint garisTipisPaint =new Paint();
    private int size = 9;
    private int akarSize = 3;

    private float cellSizePixels = 0;
    private int selectedRow = 0;
    private int selectedColumn = 0;

    private OnTouchListener listener;

    private List<Cell> cells;
    private List<Cell2> visibleCells;

    private List<Pair<Integer,Integer>> cellError;

    private Canvas canvas;

    private Paint thickPaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FF5733"));
        paint.setStrokeWidth(4f);

        return paint;
    }

    private Paint thinPaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FF5733"));
        paint.setStrokeWidth(2f);

        return paint;
    }

    private Paint selectedPaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#FF7A33"));
        return paint;
    }

    private Paint conflictedPaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#FFD2A2"));
        return paint;
    }

    private Paint textPaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        return paint;
    }

    private Paint textErrorPaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setTextSize(48);
        return paint;
    }

    private Paint visiblePaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#34DC12"));
        return paint;
    }



    public SudokuBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixels = Math.min(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(sizePixels,sizePixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cellSizePixels = (getWidth()/size);
        fillVisibleCells(canvas);
        fillCells(canvas);
        drawLines(canvas);
        drawTexts(canvas);

        fillCellError(canvas);

        this.canvas = canvas;
    }

    private void fillVisibleCells(Canvas canvas){
        for(Cell2 cell:visibleCells){
            if(cell.getValue()!=null){
                fillCell(canvas, cell.getRow(), cell.getCol(),visiblePaint());
            }

        }
    }

    private void fillCellError(Canvas canvas){
        if(cellError!=null){
            for(Pair<Integer,Integer> cell:cellError){
                int col = cell.second;
                int row = cell.first;

                String valueString = "";

                if(cells.get(row*9+col)!=null){
                    valueString = cells.get(row*9+col).getValue().toString();
                }

                Rect textBounds = new Rect();

                textPaint().getTextBounds(valueString,0,valueString.length(),textBounds);
                float textWidth = textPaint().measureText(valueString);
                float textHeight = textBounds.height();

                canvas.drawText(valueString,(col*cellSizePixels)+cellSizePixels/2-textWidth/2,
                        (row*cellSizePixels)+cellSizePixels/2+textHeight/2,textErrorPaint());

            }
        }

    }



    private void fillCells(Canvas canvas){
        if(selectedRow==-1 || selectedColumn==-1){
            return;
        }

        for(Cell cell:cells){
            int row = cell.getRow();
            int col = cell.getCol();

            if(row==selectedRow && col ==selectedColumn){
                fillCell(canvas,row,col,selectedPaint());
            }
            else if(row==selectedRow || col==selectedColumn){
                fillCell(canvas,row,col,conflictedPaint());
            }
            else if(row/akarSize == selectedRow/akarSize && col/akarSize == selectedColumn/akarSize){
                fillCell(canvas,row,col,conflictedPaint());
            }

        }
    }




    private void fillCell(Canvas canvas, int row, int col, Paint paint){
        canvas.drawRect(col*cellSizePixels,row*cellSizePixels,(col+1)*cellSizePixels,(row+1)*cellSizePixels,paint);
    }

    private void drawLines(Canvas canvas){
        canvas.drawRect(0f,0f,getWidth(),getHeight(),thickPaint());


        for(int i=1;i<size;i++){
            Paint linesType;
            if(i%akarSize==0){
                linesType = thickPaint();
            }
            else{
                linesType = thinPaint();
            }

            canvas.drawLine(i*cellSizePixels,0f,i*cellSizePixels,getHeight(),linesType);
            canvas.drawLine(0f,i*cellSizePixels,getWidth(),i*cellSizePixels,linesType);
        }
    }

    private void drawTexts(Canvas canvas){
        for(Cell cell:cells){

            int col = cell.getCol();
            int row = cell.getRow();

            String valueString = "";

            if(cell.getValue()!=null){
                valueString = cell.getValue().toString();
            }

            Rect textBounds = new Rect();

            textPaint().getTextBounds(valueString,0,valueString.length(),textBounds);
            float textWidth = textPaint().measureText(valueString);
            float textHeight = textBounds.height();

            canvas.drawText(valueString,(col*cellSizePixels)+cellSizePixels/2-textWidth/2,
                    (row*cellSizePixels)+cellSizePixels/2+textHeight/2,textPaint());

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            handleTouchEvent(event.getX(),event.getY());
            return true;
        }
        else{
            return false;
        }
    }

    private void handleTouchEvent(float x, float y){
        int possibleSelectedRow = (int)(y/cellSizePixels);
        int possibleSelectedCol = (int) (x/cellSizePixels);
        listener.onCellTouched(possibleSelectedRow,possibleSelectedCol);
    }

    public void updateSelectedUI(int row,int col){
        selectedRow = row;
        selectedColumn = col;
        invalidate();
    }

    public void updateCells(List<Cell> cells){
        this.cells = cells;
        invalidate();
    }

    public void colorVisible(List<Cell2> visibleCells){
        this.visibleCells = visibleCells;
        invalidate();
    }

    public void errorColor(List<Pair<Integer,Integer>> cells){
        cellError = cells;
        invalidate();;
    }

    public void registerListener(OnTouchListener listener){
        this.listener = listener;
    }


    public interface OnTouchListener{
        void onCellTouched(int row, int col);
    }
}
