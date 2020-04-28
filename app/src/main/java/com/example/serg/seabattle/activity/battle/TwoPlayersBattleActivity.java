package com.example.serg.seabattle.activity.battle;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.serg.seabattle.R;
import com.example.serg.seabattle.activity.FinalActivity;
import com.example.serg.seabattle.activity.MainMenuActivity;
import com.example.serg.seabattle.activity.disposal.FirstPlayerDisposalActivity;
import com.example.serg.seabattle.activity.disposal.SecondPlayerDisposalActivity;
import com.example.serg.seabattle.adapter.CellArrayAdapter;
import com.example.serg.seabattle.common.enums.ColorCellType;
import com.example.serg.seabattle.common.service.WarningService;
import com.example.serg.seabattle.gameplay.entity.BattleParameter;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.service.AttackService;

public class TwoPlayersBattleActivity extends Activity {
    public static final String INTENT_KEY_PLAYER_NUMBER = "playerNumber";
    public static final String INTENT_KEY_GAME_MODE = "gameMode";
    public static final String GAME_MODE = "Multiplayer";

    private ImageView playerStepImg;

    private CellArrayAdapter firstGridAdapter;
    private CellArrayAdapter secondGridAdapter;

    private BattleParameter battleParameter;

    private WarningService warningService;
    private AttackService attackService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_two_players_field);

        battleParameter = new BattleParameter();

        configureAdapters();
        configureUIElements();

        attackService = AttackService.getAttackService();
        warningService = WarningService.getWarningService();
    }

    private void configureAdapters() {
        Cell[] firstGridDisposal = getDisposal(FirstPlayerDisposalActivity.INTENT_KEY_1);
        firstGridAdapter = new CellArrayAdapter(this, firstGridDisposal);

        Cell[] secondGridDisposal = getDisposal(SecondPlayerDisposalActivity.INTENT_KEY_2);
        secondGridAdapter = new CellArrayAdapter(this, secondGridDisposal);
    }

    private void configureUIElements() {
        playerStepImg = findViewById(R.id.playerStepImg);
        playerStepImg.setImageResource(R.drawable.first_player_step);

        GridView firstGridView = findViewById(R.id.battlefieldOne);
        firstGridView.setAdapter(firstGridAdapter);
        firstGridView.setOnItemClickListener(onFirstGridViewItemClick);

        GridView secondGridView = findViewById(R.id.battlefieldTwo);
        secondGridView.setAdapter(secondGridAdapter);
        secondGridView.setOnItemClickListener(onSecondGridViewItemClick);
    }

    private Cell[] getDisposal(String key) {
        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(key);
        Cell[] disposal = new Cell[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            disposal[i] = (Cell) parcelables[i];
        }
        return disposal;
    }

    private GridView.OnItemClickListener onFirstGridViewItemClick = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int cellColor = firstGridAdapter.getCell(position).getPictureAddress();
            if (!battleParameter.isAttackSequence() && cellColor == ColorCellType.WHITE_CELL.colorID) {
                onGridViewCellClick(firstGridAdapter, position);

                if (battleParameter.getFirstPlayerHitCounter() == 20) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        warningService.showWarning(getApplicationContext(), interruptedException.getMessage());
                    } finally {
                        goToFinalActivity(2);
                    }
                }
            }
        }
    };

    private GridView.OnItemClickListener onSecondGridViewItemClick = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int cellColor = secondGridAdapter.getCell(position).getPictureAddress();
            if (battleParameter.isAttackSequence() && cellColor == ColorCellType.WHITE_CELL.colorID) {
                onGridViewCellClick(secondGridAdapter, position);
                if (battleParameter.getSecondPlayerHitCounter() == 20) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        warningService.showWarning(getApplicationContext(), interruptedException.getMessage());
                    } finally {
                        goToFinalActivity(1);
                    }
                }
            }

        }
    };

    private void goToFinalActivity(int playerNumber) {
        Intent intent = new Intent(getApplicationContext(), FinalActivity.class);
        intent.putExtra(INTENT_KEY_PLAYER_NUMBER, String.valueOf(playerNumber));
        intent.putExtra(INTENT_KEY_GAME_MODE, GAME_MODE);
        this.finish();
        startActivity(intent);
    }

    private void onGridViewCellClick(CellArrayAdapter cellArrayAdapter, int position) {
        Cell[] reformedCells = attackService.attackCell(cellArrayAdapter.getCells(), position);
        cellArrayAdapter.setCells(reformedCells);
        if (cellArrayAdapter.getCell(position).getPictureAddress() == ColorCellType.RED_CELL.colorID) {
            if (!battleParameter.isAttackSequence()) {
                battleParameter.increaseFirstPlayerHitCounter();
            } else {
                battleParameter.increaseSecondPlayerHitCounter();
            }

            if (attackService.isKillShip(cellArrayAdapter.getCells(), position)) {
                reformedCells = attackService.markKilledShip(cellArrayAdapter.getCells(), position);
                cellArrayAdapter.setCells(reformedCells);
            }
        } else {
            battleParameter.toggleAttackSequence();
            togglePlayerStepImage();
        }
        cellArrayAdapter.notifyDataSetChanged();
    }

    private void togglePlayerStepImage() {
        if (battleParameter.isAttackSequence()) {
            playerStepImg.setImageResource(R.drawable.first_player_step);
        } else {
            playerStepImg.setImageResource(R.drawable.second_player_step);
        }
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(TwoPlayersBattleActivity.this);
        quitDialog.setTitle("Вернуться в главное меню?");
        quitDialog.setPositiveButton("Да", dialogPositiveClickListener);
        quitDialog.setNegativeButton("Нет", dialogNegativeClickListener);

        quitDialog.show();
    }

    private final DialogInterface.OnClickListener dialogPositiveClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            TwoPlayersBattleActivity.super.finish();
            startActivity(intent);
        }
    };

    private final DialogInterface.OnClickListener dialogNegativeClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
}

