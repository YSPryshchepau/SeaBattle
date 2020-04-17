package com.example.serg.seabattle.activity.disposal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.serg.seabattle.R;
import com.example.serg.seabattle.activity.MainMenuActivity;
import com.example.serg.seabattle.adapter.CellArrayAdapter;
import com.example.serg.seabattle.common.enums.ShipType;
import com.example.serg.seabattle.common.service.WarningService;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.entity.Fleet;
import com.example.serg.seabattle.gameplay.entity.Ship;
import com.example.serg.seabattle.gameplay.service.AutoDisposalService;
import com.example.serg.seabattle.gameplay.service.FleetService;
import com.example.serg.seabattle.gameplay.service.ManualDisposalService;

public abstract class PlayerDisposalActivity extends Activity {
    private static final String xSymbol = "x";

    private Ship ship = new Ship();
    private Cell[] cells = new Cell[100];
    private Fleet fleet = new Fleet();

    protected Button nextBtn;
    private CellArrayAdapter adapter;

    protected WarningService warningService;

    private ManualDisposalService manualDisposalService;
    private AutoDisposalService autoDisposalService;
    private FleetService fleetService;

    private ShipType clickedShipType;

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player_disposal);

        ship = new Ship();
        cells = new Cell[100];
        fleet = new Fleet();
        adapter = new CellArrayAdapter(this, cells);

        createEmptyCells();
        ship.setOrientation(true);
        clickedShipType = null;

        configureUIElements();

        warningService = WarningService.getWarningService();
        manualDisposalService = ManualDisposalService.getManualDisposalService();
        autoDisposalService = AutoDisposalService.getAutoDisposalService();
        fleetService = FleetService.getFleetService();
    }

    protected boolean isAvailableNextBtnClick() {
        return fleetService.isNotAllowedShips(fleet);
    }

    private void configureUIElements() {
        nextBtn = findViewById(R.id.nextBtn);

        GridView gvMain = findViewById(R.id.gvMain);
        gvMain.setAdapter(adapter);
        gvMain.setOnItemClickListener(gvOnItemClick);
        gvMain.setOnItemLongClickListener(gvOnItemLongClick);

        Button randomDisposalBtn = findViewById(R.id.randomDisposalBtn);
        randomDisposalBtn.setOnClickListener(onRandomDisposalClickListener);

        RadioButton horizontalRB = findViewById(R.id.horizontalRB);
        horizontalRB.setOnClickListener(radioButtonClickListener);

        RadioButton verticalRB = findViewById(R.id.verticalRB);
        verticalRB.setOnClickListener(radioButtonClickListener);

        ImageButton fourDeckShipIB = findViewById(R.id.fourDeckBtn);
        ImageButton threeDeckShipIB = findViewById(R.id.threeDeckBtn);
        ImageButton doubleDeckShipIB = findViewById(R.id.doubleDeckBtn);
        ImageButton singleDeckShipIB = findViewById(R.id.singleDeckBtn);

        fourDeckShipIB.setOnClickListener(onImgBtnClickListener);
        threeDeckShipIB.setOnClickListener(onImgBtnClickListener);
        doubleDeckShipIB.setOnClickListener(onImgBtnClickListener);
        singleDeckShipIB.setOnClickListener(onImgBtnClickListener);
    }

    private View.OnClickListener radioButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RadioButton radioButton = (RadioButton) view;
            switch (radioButton.getId()) {
                case R.id.horizontalRB: {
                    ship.setOrientation(false);
                    break;
                }
                case R.id.verticalRB: {
                    ship.setOrientation(true);
                    break;
                }
            }
        }
    };

    private GridView.OnItemClickListener gvOnItemClick = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int shipAmount;
            if (clickedShipType != null) {
                shipAmount = fleetService.getShipAmountByDeckNumber(fleet, clickedShipType.deckNumber);
                if (shipAmount > 0 && manualDisposalService.isSetShipAllowed(adapter.getCells(), position, clickedShipType.deckNumber, ship.isOrientation())) {
                    Cell[] reformedCells;
                    reformedCells = manualDisposalService.setShip(adapter.getCells(), position, clickedShipType.deckNumber, ship.isOrientation());

                    setCells(reformedCells);
                    adapter.setCells(reformedCells);
                    adapter.notifyDataSetChanged();

                    fleetService.decreaseShipTypeByDeckNumber(fleet, clickedShipType.deckNumber);
                    shipAmount = fleetService.getShipAmountByDeckNumber(fleet, clickedShipType.deckNumber);
                    changeTextView(shipAmount, clickedShipType);
                }
                tuneInactiveButton();
            }
        }
    };

    private GridView.OnItemLongClickListener gvOnItemLongClick = new GridView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (manualDisposalService.isRemoveShipAllowed(adapter.getCell(position))) {
                int deckNumber;
                deckNumber = adapter.getCell(position).getShipSize();
                clickedShipType = ShipType.findElementByDeckNumber(deckNumber);
                fleetService.increaseShipTypeByDeckNumber(fleet, deckNumber);
                int shipAmount = fleetService.getShipAmountByDeckNumber(fleet, deckNumber);
                changeTextView(shipAmount, clickedShipType);

                Cell[] reformedCells;
                reformedCells = manualDisposalService.removeShip(adapter.getCells(), position);

                setCells(reformedCells);
                adapter.setCells(reformedCells);
                adapter.notifyDataSetChanged();
            }
            tuneInactiveButton();
            return true;
        }
    };

    private Button.OnClickListener onRandomDisposalClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            try {
                cells = new Cell[100];
                createEmptyCells();
                fleet = new Fleet();
                cells = autoDisposalService.setAutoDisposal(fleet, cells);
                adapter.setCells(cells);
                adapter.notifyDataSetChanged();

                tuneInactiveAllButtons();
            } catch (Exception e) {
                warningService.showWarning(getApplicationContext(), e.getMessage());
            }
        }
    };

    private ImageButton.OnClickListener onImgBtnClickListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View view) {
            try {
                if (clickedShipType == null) {
                    chooseActiveButton(view);
                } else {
                    tuneInactiveButton();
                }
            } catch (Exception e) {
                warningService.showWarning(getApplicationContext(), e.getMessage());
            }
        }
    };

    private void createEmptyCells() {
        for (int i = 0; i < 100; i++) {
            cells[i] = new Cell();
        }
    }

    private void changeTextView(int shipAmount, ShipType shipType) {
        String changedText = String.valueOf(shipAmount) + xSymbol;
        tuneTextView(changedText, shipType);
    }

    private void tuneTextView(String changedText, ShipType shipType) {
        ((TextView) findViewById(shipType.textID)).setText(changedText);
    }

    private void chooseActiveButton(View view) {
        switch (view.getId()) {
            case R.id.fourDeckBtn: {
                clickedShipType = ShipType.FOUR_DECK;
                break;
            }
            case R.id.threeDeckBtn: {
                clickedShipType = ShipType.THREE_DECK;
                break;
            }
            case R.id.doubleDeckBtn: {
                clickedShipType = ShipType.DOUBLE_DECK;
                break;
            }
            case R.id.singleDeckBtn: {
                clickedShipType = ShipType.SINGLE_DECK;
                break;
            }
        }
        tuneActiveButton(clickedShipType);
    }

    private void tuneActiveButton(ShipType shipType) {
        int shipAmount = fleetService.getShipAmountByDeckNumber(fleet, shipType.deckNumber);
        if (shipAmount - 1 > 0) {
            ((ImageButton) findViewById(shipType.buttonID)).setImageResource(shipType.clickedPicture);
        } else if (shipAmount - 1 == 0) {
            ((ImageButton) findViewById(shipType.buttonID)).setImageResource(shipType.emptyPicture);
        }
    }

    private void tuneInactiveButton() {
        int shipAmount = fleetService.getShipAmountByDeckNumber(fleet, clickedShipType.deckNumber);
        if (shipAmount > 0) {
            ((ImageButton) findViewById(clickedShipType.buttonID)).setImageResource(clickedShipType.notClickedPicture);
        }
        clickedShipType = null;
    }

    private void tuneInactiveAllButtons() {
        for (ShipType shipType : ShipType.values()) {
            ((ImageButton) findViewById(shipType.buttonID)).setImageResource(shipType.emptyPicture);
            changeTextView(0, shipType);
        }
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(PlayerDisposalActivity.this);
        quitDialog.setTitle("Вернуться в главное меню?");
        quitDialog.setPositiveButton("Да", dialogPositiveClickListener);
        quitDialog.setNegativeButton("Нет", dialogNegativeClickListener);

        quitDialog.show();
    }

    private final DialogInterface.OnClickListener dialogPositiveClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            PlayerDisposalActivity.super.finish();
            startActivity(intent);
        }
    };

    private final DialogInterface.OnClickListener dialogNegativeClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
}
