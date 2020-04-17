package com.example.serg.seabattle.activity.battle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.serg.seabattle.activity.disposal.SinglePlayerDisposalActivity;
import com.example.serg.seabattle.adapter.CellArrayAdapter;
import com.example.serg.seabattle.common.enums.ColorCellType;
import com.example.serg.seabattle.common.service.WarningService;
import com.example.serg.seabattle.gameplay.entity.AutoPlayer;
import com.example.serg.seabattle.gameplay.entity.BattleParameter;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.service.AttackService;
import com.example.serg.seabattle.gameplay.service.AutoPlayerService;

public class SinglePlayerBattleActivity extends Activity {
    public static final String INTENT_KEY_PLAYER_NUMBER = "playerNumber";
    public static final String INTENT_KEY_GAME_MODE = "gameMode";
    public static final String GAME_MODE = "Singleplayer";

    private ImageView playerStepImg;

    private CellArrayAdapter firstGridAdapter;
    private CellArrayAdapter secondGridAdapter;

    private BattleParameter battleParameter;

    private AutoPlayer autoPlayer;
    private AutoPlayerService autoPlayerService;
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


        autoPlayer = new AutoPlayer();
        autoPlayerService = AutoPlayerService.getAutoPlayerService();
        attackService = AttackService.getAttackService();
        warningService = WarningService.getWarningService();
    }

    private void configureAdapters() {
        Cell[] firstGridDisposal = getDisposal(SinglePlayerDisposalActivity.INTENT_KEY_1);
        firstGridAdapter = new CellArrayAdapter(this, firstGridDisposal);

        Cell[] secondGridDisposal = getDisposal(SinglePlayerDisposalActivity.INTENT_KEY_2);
        secondGridAdapter = new CellArrayAdapter(this, secondGridDisposal);
    }

    private void configureUIElements() {
        playerStepImg = findViewById(R.id.playerStepImg);
        playerStepImg.setImageResource(R.drawable.first_player_step);

        GridView firstGridView = findViewById(R.id.battlefieldOne);
        firstGridView.setAdapter(firstGridAdapter);

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

    private GridView.OnItemClickListener onSecondGridViewItemClick = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(battleParameter.isAttackSequence()) {
                new PlayerClickTask().execute(position);
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
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(SinglePlayerBattleActivity.this);
        quitDialog.setTitle("Вернуться в главное меню?");
        quitDialog.setPositiveButton("Да", dialogPositiveClickListener);
        quitDialog.setNegativeButton("Нет", dialogNegativeClickListener);

        quitDialog.show();
    }

    private final DialogInterface.OnClickListener dialogPositiveClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            SinglePlayerBattleActivity.super.finish();
            startActivity(intent);
        }
    };

    private final DialogInterface.OnClickListener dialogNegativeClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private class PlayerClickTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... args) {
            int position = args[0];
            Cell[] secondGrid = secondGridAdapter.getCells();
            if (battleParameter.isAttackSequence() && attackService.isCellForAttackAllowed(position, secondGrid)) {
                onGridViewCellClick(secondGridAdapter, position);
                publishProgress();
                if (battleParameter.getSecondPlayerHitCounter() == 20) {
                    congratulateWinner(1);
                } else {
                    autoPlayerAttack();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... args) {
                firstGridAdapter.notifyDataSetChanged();
                secondGridAdapter.notifyDataSetChanged();
                togglePlayerStepImage();
        }

        private void autoPlayerAttack() {
            Cell[] firstGrid = firstGridAdapter.getCells();
            while (!battleParameter.isAttackSequence()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    warningService.showWarning(getApplicationContext(), interruptedException.getMessage());
                }
                int autoAttackPosition = autoPlayerService.getPositionForAttack(autoPlayer, firstGrid);
                onGridViewCellClick(firstGridAdapter, autoAttackPosition);
                if (battleParameter.getFirstPlayerHitCounter() == 20) {
                    congratulateWinner(2);
                }
            }
        }

        private void congratulateWinner(int winnerNumber) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                warningService.showWarning(getApplicationContext(), interruptedException.getMessage());
            } finally {
                goToFinalActivity(winnerNumber);
            }
        }

        private void onGridViewCellClick(CellArrayAdapter cellArrayAdapter, int position) {
            Cell[] reformedCells = attackService.attackCell(cellArrayAdapter.getCells(), position);
            cellArrayAdapter.setCells(reformedCells);
            publishProgress();
            if (cellArrayAdapter.getCell(position).getPictureAddress() == ColorCellType.RED_CELL.colorID) {
                if (!battleParameter.isAttackSequence()) {
                    battleParameter.increaseFirstPlayerHitCounter();
                } else {
                    battleParameter.increaseSecondPlayerHitCounter();
                }

                if (attackService.isKillShip(cellArrayAdapter.getCells(), position)) {
                    reformedCells = attackService.markKilledShip(cellArrayAdapter.getCells(), position);
                    cellArrayAdapter.setCells(reformedCells);
                    publishProgress();
                }
            } else {
                battleParameter.toggleAttackSequence();
                publishProgress();
            }
        }
    }
}
