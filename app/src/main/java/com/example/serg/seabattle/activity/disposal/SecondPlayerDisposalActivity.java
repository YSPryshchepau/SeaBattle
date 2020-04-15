package com.example.serg.seabattle.activity.disposal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.serg.seabattle.R;
import com.example.serg.seabattle.activity.battle.TwoPlayersBattleActivity;
import com.example.serg.seabattle.gameplay.entity.Cell;

public class SecondPlayerDisposalActivity extends FirstPlayerDisposalActivity {
    public static final String INTENT_KEY_2 = "battlefield_2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView playerNumber = findViewById(R.id.playerNumberLabel);
        playerNumber.setText("Игрок 2");

        nextBtn.setOnClickListener(onNextBtnClickListener);
    }


    public Button.OnClickListener onNextBtnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isAvailableNextBtnClick()) {
                Parcelable[] parcelables = getIntent().getParcelableArrayExtra(INTENT_KEY_1);
                Cell[] battlefield1 = new Cell[parcelables.length];
                for (int i = 0; i < parcelables.length; i++) {
                    battlefield1[i] = (Cell) parcelables[i];
                }

                Intent intent = new Intent(getApplicationContext(), TwoPlayersBattleActivity.class);
                intent.putExtra(INTENT_KEY_1, battlefield1);
                intent.putExtra(INTENT_KEY_2, getCells());
                SecondPlayerDisposalActivity.super.finish();
                startActivity(intent);
            } else {
                warningService.showWarning(getApplicationContext(), "Не все корабли расставлены!");
            }
        }
    };
}
