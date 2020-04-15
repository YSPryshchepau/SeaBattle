package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.common.enums.ColorCellType;
import com.example.serg.seabattle.common.enums.CellState;
import com.example.serg.seabattle.gameplay.entity.Cell;

public class AttackService {
    private static AttackService attackService;

    private CoordinatesConverterService converterService;
    private ShipService shipService;

    private AttackService() {
        converterService = CoordinatesConverterService.getCoordinatesConverterService();
        shipService = ShipService.getShipService();
    }

    public static AttackService getAttackService() {
        if(attackService == null) {
            attackService = new AttackService();
        }
        return attackService;
    }

    public boolean isCellForAttackAllowed(int position, Cell[] cells) {
        if(position >= 0 && position < 100) {
            Cell cell = cells[position];
            return cell.getPictureAddress() == ColorCellType.WHITE_CELL.colorID;
        }
        return false;
    }

    public Cell[] attackCell(Cell[] cells, int position){
        Cell[][] placementCells = converterService.toPlacementCells(cells);
        int x = converterService.setX(position);
        int y = converterService.setY(position);

        if(placementCells[y][x].getPictureAddress() == ColorCellType.WHITE_CELL.colorID){
            if(placementCells[y][x].getCellState() == CellState.OCCUPIED)
            {
                placementCells[y][x].setPictureAddress(ColorCellType.RED_CELL.colorID);
            }
            else
            {
                placementCells[y][x].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
            }
        }

        return converterService.toCells(placementCells);
    }
    public boolean isKillShip(Cell[] cells, int position){
        Cell[][] placementCells = converterService.toPlacementCells(cells);
        int x = converterService.setX(position);
        int y = converterService.setY(position);
        if(shipService.isShipOrientation(placementCells, x, y)){
            int i = 0;
            while(y - i >= 0 && placementCells[y][x].getShipSize() == placementCells[y - i][x].getShipSize()){
                if(placementCells[y][x].getPictureAddress().intValue() != placementCells[y - i][x].getPictureAddress().intValue()){
                    return false;
                }
                i++;
            }
            i = 1;
            while(y + i <= 9 && placementCells[y][x].getShipSize() == placementCells[y + i][x].getShipSize()){
                if(placementCells[y][x].getPictureAddress().intValue() != placementCells[y + i][x].getPictureAddress().intValue()){
                    return false;
                }
                i++;
            }
        }
        else {
            int i = 0;
            while (x - i >= 0 && placementCells[y][x].getShipSize() == placementCells[y][x - i].getShipSize()) {
                if (placementCells[y][x].getPictureAddress().intValue() != placementCells[y][x - i].getPictureAddress().intValue()) {
                    return false;
                }
                i++;
            }
            i = 1;
            while (x + i <= 9 && placementCells[y][x].getShipSize() == placementCells[y][x + i].getShipSize()) {
                if (placementCells[y][x].getPictureAddress().intValue() != placementCells[y][x + i].getPictureAddress().intValue()) {
                    return false;
                }
                i++;
            }
        }
        return true;
    }
    public Cell[] markKilledShip(Cell[] cells, int position){
        Cell[][] placementCells = converterService.toPlacementCells(cells);
        int x = converterService.setX(position);
        int y = converterService.setY(position);
        int deckNumber = cells[position].getShipSize();

        if(shipService.isShipOrientation(placementCells, x, y)) {
            while (y >= 1 && placementCells[y][x].getShipSize() == placementCells[y - 1][x].getShipSize()) {
                y -= 1;

            }
            markKilledVerticalShipCells(deckNumber, placementCells, x, y);
        }
        else{
            while (x >= 1 && placementCells[y][x].getShipSize() == placementCells[y][x - 1].getShipSize()) {
                x -= 1;
            }
            markKilledHorizontalShipCells(deckNumber, placementCells, x, y);
        }
        return converterService.toCells(placementCells);
    }


    private void markKilledHorizontalShipCells(int deckNumber, Cell[][] placementCells, int x, int y) {
        int deckCounter = 1;
        for (int i = x; i < x + deckNumber; i++, deckCounter++) {
            placementCells[y][i].setPictureAddress(ColorCellType.CRIMSON_CELL.colorID);
            setHorizontalCleanCells(deckNumber, deckCounter, i, placementCells, y);
        }
    }
    private void setHorizontalCleanCells(int deckNumber, int deckCounter, int i, Cell[][] placementCells, int y){
        if (deckCounter == 1) {
            if (i > 0) {
                placementCells[y][i - 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);

                if (y > 0) {
                    placementCells[y - 1][i - 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
                if (y < 9) {
                    placementCells[y + 1][i - 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
            }
        }
        if (deckCounter <= deckNumber) {
            if (y > 0) {
                placementCells[y - 1][i].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
            }
            if (y < 9) {
                placementCells[y + 1][i].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
            }
        }
        if (deckCounter == deckNumber) {
            if (i < 9) {
                placementCells[y][i + 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                if (y > 0) {
                    placementCells[y - 1][i + 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
                if (y < 9) {
                    placementCells[y + 1][i + 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
            }
        }
    }

    private void markKilledVerticalShipCells(int deckNumber, Cell[][] placementCells, int x, int y){
        int deckCounter = 1;
        for(int i = y; i < y + deckNumber; i++, deckCounter++){
            placementCells[i][x].setPictureAddress(ColorCellType.CRIMSON_CELL.colorID);
            setVerticalCleanCells(deckNumber, deckCounter, i, placementCells, x);
        }
    }
    private void setVerticalCleanCells(int deckNumber, int deckCounter, int i, Cell[][] placementCells, int x){// int i is cycle's iterator
        if (deckCounter == 1) {
            if (i > 0) {
                placementCells[i - 1][x].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                if (x > 0) {
                    placementCells[i - 1][x - 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
                if (x < 9) {
                    placementCells[i - 1][x + 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
            }
        }
        if (deckCounter <= deckNumber) {
            if (x > 0) {
                placementCells[i][x - 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
            }
            if (x < 9) {
                placementCells[i][x + 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
            }
        }
        if (deckCounter == deckNumber) {
            if (i < 9) {
                placementCells[i + 1][x].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                if (x > 0) {
                    placementCells[i + 1][x - 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
                if (x < 9) {
                    placementCells[i + 1][x + 1].setPictureAddress(ColorCellType.BLUE_CELL.colorID);
                }
            }
        }
    }
}
