package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.gameplay.entity.Cell;

public class CoordinatesConverterService {
    private static CoordinatesConverterService coordinatesConverterService;

    private CoordinatesConverterService() {

    }

    public static CoordinatesConverterService getCoordinatesConverterService() {
        if (coordinatesConverterService == null) {
            coordinatesConverterService = new CoordinatesConverterService();
        }
        return coordinatesConverterService;
    }

    public Cell[][] toPlacementCells(Cell[] cells) {
        Cell[][] placementCells = new Cell[10][10];
        for (int k = 0; k < 100; k++) {
            placementCells[setY(k)][setX(k)] = cells[k];
        }
        return placementCells;
    }

    public int setY(int position) {
        return position / 10;
    }

    public int setX(int position) {
        return position % 10;
    }

    public Cell[] toCells(Cell[][] placementCells) {
        Cell[] cells = new Cell[100];
        int k = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++, k++) {
                cells[k] = placementCells[i][j];
            }
        }
        return cells;
    }
}
