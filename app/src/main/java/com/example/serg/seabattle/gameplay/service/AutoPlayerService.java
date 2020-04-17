package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.common.enums.CellState;
import com.example.serg.seabattle.common.enums.ColorCellType;
import com.example.serg.seabattle.common.service.WarningService;
import com.example.serg.seabattle.gameplay.entity.AutoPlayer;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.entity.Fleet;

import java.util.List;
import java.util.Random;

public class AutoPlayerService {
    private static AutoPlayerService autoPlayerService;

    private CoordinatesConverterService converterService;
    private AttackService attackService;
    private AutoDisposalService autoDisposalService;
    private WarningService warningService;
    private Random randomGenerator;

    private AutoPlayerService() {
        converterService = CoordinatesConverterService.getCoordinatesConverterService();
        attackService = AttackService.getAttackService();
        autoDisposalService = AutoDisposalService.getAutoDisposalService();
        warningService = WarningService.getWarningService();
        randomGenerator = new Random();
    }

    public static AutoPlayerService getAutoPlayerService() {
        if (autoPlayerService == null) {
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

    public int getPositionForAttack(AutoPlayer autoPlayer, Cell[] cells) {

        int oldPosition = autoPlayer.getLastSuccessAttackPosition();
        if (oldPosition != -1 && cells[oldPosition].getPictureAddress() == ColorCellType.CRIMSON_CELL.colorID) {
            autoPlayer.setFirstSuccessAttackPosition(-1);
            autoPlayer.setLastSuccessAttackPosition(-1);
            autoPlayer.setOrientation(null);
            autoPlayer.resetAvailablePositions();
        }
        int position = setPosition(autoPlayer, cells);
        if (cells[position].getCellState() == CellState.OCCUPIED) {
            if (autoPlayer.getFirstSuccessAttackPosition() == -1) {
                autoPlayer.setFirstSuccessAttackPosition(position);
            }
            autoPlayer.setLastSuccessAttackPosition(position);
        }

        return position;
    }

    private int setPosition(AutoPlayer autoPlayer, Cell[] cells) {
        int position = autoPlayer.getLastSuccessAttackPosition();

        if (position == -1) {
            position = generatePosition(cells);
        } else {
            position = setPositionNearAttackedCell(cells, autoPlayer);
        }

        return position;
    }

    private int generatePosition(Cell[] cells) {
        int position;
        do {
            position = randomGenerator.nextInt(100);
        } while (cells[position].getPictureAddress() != ColorCellType.WHITE_CELL.colorID);
        return position;
    }

    private int setPositionNearAttackedCell(Cell[] cells, AutoPlayer autoPlayer) {
        int position;
        int firstPos = autoPlayer.getFirstSuccessAttackPosition();
        int lastPos = autoPlayer.getLastSuccessAttackPosition();
        if (firstPos == lastPos) {
            position = setPositionAfterFirstSuccessAttack(cells, autoPlayer);
        } else {
            if (autoPlayer.getOrientation() == null) {
                boolean orientation = calculateOrientation(autoPlayer);
                autoPlayer.setOrientation(orientation);
            }
            position = setPositionByOrientation(cells, autoPlayer);
        }

        return position;
    }

    private int setPositionByOrientation(Cell[] cells, AutoPlayer autoPlayer) {
        int position;
        if (autoPlayer.getOrientation()) {
            position = setPositionForVerticalOrientation(cells, autoPlayer);
        } else {
            position = setPositionForHorizontalOrientation(cells, autoPlayer);
        }

        return position;
    }

    private int setPositionForVerticalOrientation(Cell[] cells, AutoPlayer autoPlayer) {
        int lastPos = autoPlayer.getLastSuccessAttackPosition();
        int firstPos = autoPlayer.getFirstSuccessAttackPosition();
        int position;
        if (attackService.isCellForAttackAllowed(lastPos - 10, cells)) {
            position = lastPos - 10;
        } else if (attackService.isCellForAttackAllowed(lastPos + 10, cells)) {
            position = lastPos + 10;
        } else if (attackService.isCellForAttackAllowed(firstPos + 10, cells)) {
            position = firstPos + 10;
        } else {
            position = firstPos - 10;
        }

        return position;
    }

    private int setPositionForHorizontalOrientation(Cell[] cells, AutoPlayer autoPlayer) {
        int lastPos = autoPlayer.getLastSuccessAttackPosition();
        int firstPos = autoPlayer.getFirstSuccessAttackPosition();
        int position;
        int x = converterService.setX(lastPos);
        if (x != 9 && attackService.isCellForAttackAllowed(lastPos + 1, cells)) {
            position = lastPos + 1;
        } else if (attackService.isCellForAttackAllowed(lastPos - 1, cells)) {
            position = lastPos - 1;
        } else if (attackService.isCellForAttackAllowed(firstPos - 1, cells)) {
            position = firstPos - 1;
        } else {
            position = firstPos + 1;
        }

        return position;
    }

    private int setPositionAfterFirstSuccessAttack(Cell[] cells, AutoPlayer autoPlayer) {
        List<Integer> positions = autoPlayer.getAvailablePositions();
        if (positions.size() == 0) {
            addAvailablePositionsForAttack(cells, autoPlayer);
        }
        int index = generateIndex(autoPlayer);
        int position = positions.get(index);
        positions.remove(index);
        return position;
    }

    private void addAvailablePositionsForAttack(Cell[] cells, AutoPlayer autoPlayer) {
        int lastSuccessPos = autoPlayer.getLastSuccessAttackPosition();
        int x = converterService.setX(lastSuccessPos);
        List<Integer> positions = autoPlayer.getAvailablePositions();
        if (x != 9 && attackService.isCellForAttackAllowed(lastSuccessPos + 1, cells)) {
            positions.add(lastSuccessPos + 1);
        }
        if (attackService.isCellForAttackAllowed(lastSuccessPos - 1, cells)) {
            positions.add(lastSuccessPos - 1);
        }
        if (attackService.isCellForAttackAllowed(lastSuccessPos - 10, cells)) {
            positions.add(lastSuccessPos - 10);
        }
        if (attackService.isCellForAttackAllowed(lastSuccessPos + 10, cells)) {
            positions.add(lastSuccessPos + 10);
        }
    }

    private int generateIndex(AutoPlayer autoPlayer) {
        int size = autoPlayer.getAvailablePositions().size();
        return randomGenerator.nextInt(size);
    }

    /*
     * orientation == true - vertical disposal
     * orientation == false - horizontal disposal
     * */
    private boolean calculateOrientation(AutoPlayer autoPlayer) {
        int firstPos = autoPlayer.getFirstSuccessAttackPosition();
        int firstY = converterService.setY(firstPos);

        int lastPos = autoPlayer.getLastSuccessAttackPosition();
        int lastY = converterService.setY(lastPos);

        return lastY - 1 == firstY || lastY + 1 == firstY;
    }
}
