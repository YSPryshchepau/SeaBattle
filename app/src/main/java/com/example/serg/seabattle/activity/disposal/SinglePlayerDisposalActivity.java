package com.example.serg.seabattle.activity.disposal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.serg.seabattle.activity.battle.SinglePlayerBattleActivity;
import com.example.serg.seabattle.gameplay.entity.AutoPlayer;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.service.AutoPlayerService;

public class SinglePlayerDisposalActivity extends PlayerDisposalActivity{
    public static final String INTENT_KEY_1 = "playerBattlefield";
    public static final String INTENT_KEY_2 = "computerBattlefield";

    private AutoPlayerService autoPlayerService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextBtn.setOnClickListener(onNextBtnClickListener);
        autoPlayerService = AutoPlayerService.getAutoPlayerService();
    }

    private Button.OnClickListener onNextBtnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            {
                if (isAvailableNextBtnClick()) {
                    Cell[] autoPlayerDisposal = autoPlayerService.setComputerFleetDisposal();

                    Intent intent = new Intent(getApplicationContext(), SinglePlayerBattleActivity.class);
                    intent.putExtra(INTENT_KEY_1, getCells());
                    intent.putExtra(INTENT_KEY_2, autoPlayerDisposal);
                    SinglePlayerDisposalActivity.super.finish();
                    startActivity(intent);
                } else {
                    warningService.showWarning(getApplicationContext(), "Не все корабли расставлены!");
                }
            }
        }
    };
}
