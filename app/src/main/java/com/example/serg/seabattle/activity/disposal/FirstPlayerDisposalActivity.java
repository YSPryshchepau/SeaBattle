package com.example.serg.seabattle.activity.disposal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.serg.seabattle.R;


public class FirstPlayerDisposalActivity extends PlayerDisposalActivity {
    public static final String INTENT_KEY_1 = "battlefield_1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView playerNumber = findViewById(R.id.playerNumberLabel);
        playerNumber.setText("Игрок 1");

        nextBtn.setOnClickListener(onNextBtnClickListener);
    }

    private Button.OnClickListener onNextBtnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            {
                if (isAvailableNextBtnClick()) {
                    Intent intent = new Intent(getApplicationContext(), SecondPlayerDisposalActivity.class);
                    intent.putExtra(INTENT_KEY_1, getCells());
                    FirstPlayerDisposalActivity.super.finish();
                    startActivity(intent);
                } else {
                    warningService.showWarning(getApplicationContext(), getString(R.string.ships_not_disposed));
                }
            }
        }
    };
}