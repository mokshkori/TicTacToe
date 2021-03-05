package com.example.user.tictactoe;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Stack;

public class MainActivity extends Activity {
    private int activePlayer = 1;
    private int  savedProgress[] = {0,0,0,0,0,0,0,0,0};
    int winningPositions [][] = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    private boolean activeState = true;
    private int moveIndex;
    private TextView turnView;
    private Stack<Integer> undo = new Stack<>();

    private void updateTurn(){
        ImageView zero = (ImageView) findViewById(R.id.zero);
        ImageView cross = (ImageView) findViewById(R.id.cross);
        if (activePlayer == 1) {
            zero.animate().alpha(1f).setDuration(300);
            cross.animate().alpha(0.2f).setDuration(300);
        } else {
            zero.animate().alpha(0.2f).setDuration(300);
            cross.animate().alpha(1f).setDuration(300);
        }
    }
    public void undo(View view){
        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        if(!undo.isEmpty() && activeState)
        {
            int lastIndex = undo.pop();
            ((ImageView)gridLayout.getChildAt(lastIndex)).animate().alpha(0f).setDuration(500);
            savedProgress[lastIndex] = 0;
            if(activePlayer == 1)
            {
                activePlayer = 2;
                updateTurn();
            }
            else
            {
                activePlayer = 1;
                updateTurn();
            }

        }
        if(undo.isEmpty())
        {
            ((ImageView)view).setAlpha(0.5f);
        }
    }
    public void reset(View view){
        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        for(int i = 0;i<9;i++)
        {
            ((ImageView)gridLayout.getChildAt(i)).animate().alpha(0f).setDuration(500);
            savedProgress[i] = 0;
        }
        activePlayer = 1;
        updateTurn();
        while(!undo.isEmpty())
            undo.pop();
        ((ImageView) findViewById(R.id.goBack)).setAlpha(0.2f);

        activeState = true;
    }
    private boolean checkForWinner()
    {
        for( int x[] : winningPositions)
        {
            if(savedProgress[x[0]] == savedProgress[x[1]] &&  savedProgress[x[1]] == savedProgress[x[2]] && savedProgress[x[0]] != 0)
            return true;
        }
        return false;
    }
    public void dropOn(View view) {
        ImageView move = (ImageView) view;
        ImageView goBack = (ImageView) findViewById(R.id.goBack);

        moveIndex = Integer.parseInt(move.getTag().toString());
        if(savedProgress[moveIndex] == 0 && activeState) {
            if (activePlayer == 1) {
                move.setImageResource(R.drawable.zero);
                move.setAlpha(0f);

                savedProgress[moveIndex] = activePlayer;
                if (checkForWinner()) {
                    Toast.makeText(this, "PLAYER 1 WON", Toast.LENGTH_SHORT).show();
                    activeState = false;
                }
                activePlayer = 2;

            } else {
                move.setImageResource(R.drawable.cross);
                move.setAlpha(0f);

                savedProgress[moveIndex] = activePlayer;
                if (checkForWinner()) {
                    Toast.makeText(this, "PLAYER 1 WON", Toast.LENGTH_SHORT).show();
                    activeState = false;
                }
                activePlayer = 1;
            }
            undo.push(moveIndex);
            goBack.setAlpha(1f);
            move.animate().alpha(1f).setDuration(1000);
            updateTurn();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
