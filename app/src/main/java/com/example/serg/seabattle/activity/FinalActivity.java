package com.example.serg.seabattle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.serg.seabattle.R;
import com.example.serg.seabattle.activity.battle.SinglePlayerBattleActivity;
import com.example.serg.seabattle.activity.battle.TwoPlayersBattleActivity;
import com.example.serg.seabattle.activity.disposal.FirstPlayerDisposalActivity;
import com.example.serg.seabattle.activity.disposal.SinglePlayerDisposalActivity;

public class FinalActivity extends Activity {
    private TextView congratulatoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_final);

        congratulatoryTextView = findViewById(R.id.congritulatoryText);
        setCongratulatoryText();

        Button newGameBtn = findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(newGameBtnClickListener);

        Button menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(menuBtnClickListener);
    }

    private View.OnClickListener newGameBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String gameMode = getIntent().getExtras().getString(SinglePlayerBattleActivity.INTENT_KEY_GAME_MODE);
            if (gameMode.equals(SinglePlayerBattleActivity.GAME_MODE)) {
                Intent singleModeIntent = new Intent(getApplicationContext(), SinglePlayerDisposalActivity.class);
                FinalActivity.super.finish();
                startActivity(singleModeIntent);
            } else {
                Intent intent = new Intent(getApplicationContext(), FirstPlayerDisposalActivity.class);
                FinalActivity.super.finish();
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener menuBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            FinalActivity.super.finish();
            startActivity(intent);
        }
    };

    private void setCongratulatoryText() {
        String winnerNumber = getIntent().getExtras().getString(TwoPlayersBattleActivity.INTENT_KEY_PLAYER_NUMBER);
        String congratulatoryLine = getResources().getString(R.string.congratulatory_line);
        congratulatoryLine += " " + winnerNumber;
        congratulatoryTextView.setText(congratulatoryLine);
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(FinalActivity.this);
        quitDialog.setTitle("Вернуться в главное меню?");
        quitDialog.setPositiveButton("Да", dialogPositiveClickListener);
        quitDialog.setNegativeButton("Нет", dialogNegativeClickListener);

        quitDialog.show();
    }

    private final DialogInterface.OnClickListener dialogPositiveClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            FinalActivity.super.finish();
            startActivity(intent);
        }
    };

    private final DialogInterface.OnClickListener dialogNegativeClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
}
