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

import com.example.serg.seabattle.R;
import com.example.serg.seabattle.activity.disposal.FirstPlayerDisposalActivity;
import com.example.serg.seabattle.activity.disposal.SinglePlayerDisposalActivity;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button singlePlayerBtn = findViewById(R.id.singlePlayerBtn);
        singlePlayerBtn.setOnClickListener(onSinglePlayerClickListener);

        Button twoPlayerBtn = findViewById(R.id.twoPlayersBtn);
        twoPlayerBtn.setOnClickListener(onTwoPlayerClickListener);

        Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(onExitClickListener);
    }

    private Button.OnClickListener onSinglePlayerClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SinglePlayerDisposalActivity.class);
            startActivity(intent);
        }
    };

    private Button.OnClickListener onTwoPlayerClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), FirstPlayerDisposalActivity.class);
            startActivity(intent);
        }
    };

    private Button.OnClickListener onExitClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            openQuitDialog();
        }
    };

    private void exitHandler() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(MainMenuActivity.this);
        quitDialog.setTitle("Вы хотите выйти?");
        quitDialog.setPositiveButton("Да", dialogPositiveClickListener);
        quitDialog.setNegativeButton("Нет", dialogNegativeClickListener);

        quitDialog.show();
    }

    private final DialogInterface.OnClickListener dialogPositiveClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            exitHandler();
        }
    };

    private final DialogInterface.OnClickListener dialogNegativeClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
}
