package miguel.example.com.finalProject.TicTacToe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.Models.Score;
import miguel.example.com.finalProject.R;

/**
 * Created by 79812 on 02/12/2017.
 */

public class TicTacToeFragment extends Fragment implements View.OnClickListener, FirebaseServices.GamesScoreListener{

    public Button button1, button2, button3, button4, button5, button6,
            button7, button8, button9, start, reset;
    public RadioButton naught, cross;
    public TextView text, score;
    public ComputerLogic logic; // For when not using minimax
    public PerfectComputerLogic perfectLogic; // For when using minimax
    public MenuItem item;
    private AlertDialog.Builder dialog;
    public boolean playerTurn = false;
    public boolean playerWon = false;
    public boolean computerWon = false;
    public boolean draw = false;
    public boolean winnable = true;
    private View rootView;
    public static final String USER_WON = "Has ganado el juego :)";
    public static final String MACHINE_WON = "La maquina ha ganado el juego :(";
    public static final String TIE = "Es un empate";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        initViews();
        logic = new ComputerLogic();
        perfectLogic = new PerfectComputerLogic();
        dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.app_name));
        FirebaseServices.getInstance(getActivity()).getScore("GamesScore", this);
        return rootView;
    }

    public void saveScore(String whoWon){
        Log.d("some","popo");
        switch (whoWon){
            case USER_WON:
                dialog.setMessage(USER_WON);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        resetGame();
                    }
                });
                dialog.show();
                FirebaseServices.getInstance(getActivity()).saveScore("GamesScore", true);
                break;
            case MACHINE_WON:
                dialog.setMessage(MACHINE_WON);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        resetGame();
                    }
                });
                dialog.show();
                FirebaseServices.getInstance(getActivity()).saveScore("GamesScore", false);
                break;
            case TIE:
                dialog.setMessage(TIE);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        resetGame();
                    }
                });
                dialog.show();
                break;
        }
    }

    private void initViews() {
        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button3 = rootView.findViewById(R.id.button3);
        button4 = rootView.findViewById(R.id.button4);
        button5 = rootView.findViewById(R.id.button5);
        button6 = rootView.findViewById(R.id.button6);
        button7 = rootView.findViewById(R.id.button7);
        button8 = rootView.findViewById(R.id.button8);
        button9 = rootView.findViewById(R.id.button9);
        start = rootView.findViewById(R.id.start);
        reset = rootView.findViewById(R.id.reset);
        score = rootView.findViewById(R.id.fragment_tic_tac_toe_score);
        naught = rootView.findViewById(R.id.naught);
        cross = rootView.findViewById(R.id.cross);

        text = rootView.findViewById(R.id.player_info_text);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        start.setOnClickListener(this);
        reset.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                // Make the menu item and views required to start the game invisible
                //item = menu.findItem(R.id.winnable);
                //item.setVisible(false);
                start.setVisibility(View.INVISIBLE);
                naught.setVisibility(View.INVISIBLE);
                cross.setVisibility(View.INVISIBLE);
                FirebaseServices.getInstance(getActivity()).addToRoutine("routine","Jugaste al gato", "Jugar");
                if(cross.isChecked()) {
                    playerTurn = true;
                    text.setText("Player's turn");
                } else if(naught.isChecked()) {
                    playerTurn = false;
                    text.setText("Computer's turn");
                    logic.nextMove(this); // Place the first move in the center
                }

                break;
            case R.id.reset:
                resetGame();
                break;
            default:
                if(start.getVisibility() == View.VISIBLE)
                    break; // If a button is pressed without starting the game
                placeObject(v.getId());
                if(winnable)
                    logic.nextMove(this); // Don't use minimax
                else
                    perfectLogic.nextMove(this); // Use minimax
        }
    }
    private void resetGame(){
        // Make all the invisible views visible again
        //item = menu.findItem(R.id.winnable);
        //item.setVisible(true);
        start.setVisibility(View.VISIBLE);
        naught.setVisibility(View.VISIBLE);
        cross.setVisibility(View.VISIBLE);
        text.setText("");
        playerTurn = false;
        playerWon = false;
        computerWon = false;
        draw = false;

        button1.setText("");
        button2.setText("");
        button3.setText("");
        button4.setText("");
        button5.setText("");
        button6.setText("");
        button7.setText("");
        button8.setText("");
        button9.setText("");

        FirebaseServices.getInstance(getActivity()).getScore("GamesScore", this);
    }
    public void placeObject(int id) {
        Button button = rootView.findViewById(id);
        if(!button.getText().equals(""))
            return;

        if(cross.isChecked() && playerTurn)
            button.setText("X");
        else if(naught.isChecked() && playerTurn)
            button.setText("O");

        check(); // check if the game is over
        if(playerWon && !draw) {
            text.setText("Player Won!");
            saveScore(USER_WON);
        }
        else if(draw) {
            text.setText("It's a draw!");
            saveScore(TIE);
        }
        else {
            changeTurn();
        }
    }

    public void changeTurn() {
        playerTurn = !playerTurn;
        if(playerTurn)
            text.setText("Player's Turn");
        else
            text.setText("Computer's Turn");
    }

    public void check() {
        if (crossHorizontal() || crossVertical() || crossDiagonal()) {
            if(cross.isChecked())
                playerWon = true;
            else
                computerWon = true;
        } else if (naughtsHorizontal() || naughtsVertical()
                || naughtsDiagonal()) {
            if(naught.isChecked())
                playerWon = true;
            else
                computerWon = true;
        } else if(full() && !playerWon && !computerWon)
            draw = true;
    }

    public boolean full(){
        return !button1.getText().equals("")
                && !button2.getText().equals("")
                && !button3.getText().equals("")
                && !button4.getText().equals("")
                && !button5.getText().equals("")
                && !button6.getText().equals("")
                && !button7.getText().equals("")
                && !button8.getText().equals("")
                && !button9.getText().equals("");
    }

    public boolean isEmpty() {
        return button1.getText().equals("")
                && button2.getText().equals("")
                && button3.getText().equals("")
                && button4.getText().equals("")
                && button5.getText().equals("")
                && button6.getText().equals("")
                && button7.getText().equals("")
                && button8.getText().equals("")
                && button9.getText().equals("");
    }

    public boolean crossHorizontal() {
        return (button1.getText().equals("X") && button2.getText().equals("X") && button3.getText().equals("X")) ||
                (button4.getText().equals("X") && button5.getText().equals("X") && button6.getText().equals("X")) ||
                (button7.getText().equals("X") && button8.getText().equals("X") && button9.getText().equals("X"));
    }

    public boolean naughtsHorizontal() {
        return (button1.getText().equals("O") && button2.getText().equals("O") && button3.getText().equals("O")) ||
                (button4.getText().equals("O") && button5.getText().equals("O") && button6.getText().equals("O")) ||
                (button7.getText().equals("O") && button8.getText().equals("O") && button9.getText().equals("O"));
    }

    public boolean crossVertical() {
        return (button1.getText().equals("X") && button4.getText().equals("X") && button7.getText().equals("X")) ||
                (button2.getText().equals("X") && button5.getText().equals("X") && button8.getText().equals("X")) ||
                (button3.getText().equals("X") && button6.getText().equals("X") && button9.getText().equals("X"));
    }

    public boolean naughtsVertical() {
        return (button1.getText().equals("O") && button4.getText().equals("O") && button7.getText().equals("O")) ||
                (button2.getText().equals("O") && button5.getText().equals("O") && button8.getText().equals("O")) ||
                (button3.getText().equals("O") && button6.getText().equals("O") && button9.getText().equals("O"));
    }

    public boolean crossDiagonal() {
        return (button1.getText().equals("X") && button5.getText().equals("X") && button9.getText().equals("X")) ||
                (button3.getText().equals("X") && button5.getText().equals("X") && button7.getText().equals("X"));
    }

    public boolean naughtsDiagonal() {
        return (button1.getText().equals("O") && button5.getText().equals("O") && button9.getText().equals("O")) ||
                (button3.getText().equals("O") && button5.getText().equals("O") && button7.getText().equals("O"));
    }

    @Override
    public void onScoreReady(Score scores) {
        if (scores != null){
            score.setText(scores.getWonGames() +"-" + scores.getLostGames());
        }

    }

    @Override
    public void onScoreError(String error) {
        Log.d("Score error", error);
    }
}
