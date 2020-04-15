package com.example.serg.seabattle.gameplay.service;

import android.content.Context;

import com.example.serg.seabattle.common.enums.CellState;
import com.example.serg.seabattle.common.enums.ColorCellType;
import com.example.serg.seabattle.common.service.WarningService;
import com.example.serg.seabattle.gameplay.entity.AutoPlayer;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.entity.Fleet;

import java.util.Random;

public class AutoPlayerService {
    private static AutoPlayerService autoPlayerService;

    private AttackService attackService;
    private AutoDisposalService autoDisposalService;
    private WarningService warningService;
    private Random randomGenerator;

    private AutoPlayerService() {
        attackService = AttackService.getAttackService();
        autoDisposalService = AutoDisposalService.getAutoDisposalService();
        warningService = WarningService.getWarningService();
        randomGenerator = new Random();
    }

    public static AutoPlayerService getAutoPlayerService() {
        if(autoPlayerService == null) {
            autoPlayerService = new AutoPlayerService();
        }
        return autoPlayerService;
    }

    public Cell[] setComputerFleetDisposal() {
        Cell[] cells = new Cell[100];
        for (int i = 0; i < 100; i++) {
            cells[i] = new Cell();
        }

        Fleet fleet = new Fleet();
        cells = autoDisposalService.setAutoDisposal(fleet, cells);
        return cells;
    }

    public int attack(Context context, AutoPlayer autoPlayer, Cell[] cells) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException interruptedException) {
            warningService.showWarning(context, interruptedException.getMessage());
        }

        int position = setPosition(autoPlayer, cells);
        if(cells[position].getCellState() == CellState.OCCUPIED) {
            autoPlayer.setLastSuccessAttackPosition(position);
        }

        return position;
    }

    private int generatePosition(Cell[] cells) {
        int position;
        do {
            position = randomGenerator.nextInt(100);
        } while(cells[position].getPictureAddress() != ColorCellType.WHITE_CELL.colorID);
        return position;
    }

    private int setPosition(AutoPlayer autoPlayer, Cell[] cells) {
        int position = autoPlayer.getLastSuccessAttackPosition();

        if(position == -1) {
            position = generatePosition(cells);
        } else {
            position = setPositionNearAttackedCell(position, cells, autoPlayer);
        }

        return position;
    }

    private int setPositionNearAttackedCell(int position, Cell[] cells, AutoPlayer autoPlayer) {
        int lastSuccessAttackPosition = autoPlayer.getLastSuccessAttackPosition();
        if(position == lastSuccessAttackPosition && attackService.isCellForAttackAllowed(lastSuccessAttackPosition + 1, cells)) {
            position = lastSuccessAttackPosition + 1;
        }
        else if(position == lastSuccessAttackPosition && attackService.isCellForAttackAllowed(lastSuccessAttackPosition - 1, cells)) {
            position = lastSuccessAttackPosition - 1;
        }
        else if(position == lastSuccessAttackPosition && attackService.isCellForAttackAllowed(lastSuccessAttackPosition - 10, cells)) {
            position = lastSuccessAttackPosition - 10;
        }
        else if(position == lastSuccessAttackPosition && attackService.isCellForAttackAllowed(lastSuccessAttackPosition + 10, cells)) {
            position = lastSuccessAttackPosition + 10;
        } else {
            position = generatePosition(cells);
        }
        return position;
    }
}
