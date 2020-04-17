package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.gameplay.entity.Cell;

public class ShipService {
    private static ShipService shipService;


    private ShipService() {

    }

    public static ShipService getShipService() {
        if(shipService == null) {
            shipService = new ShipService();
        }
        return shipService;
    }

    public boolean isShipOrientation(Cell[][] placementCells, int x, int y){ //true - vertical, false - horizontal, single-deck ship will always vertical for this method
        if(x > 0 && placementCells[y][x].getShipSize() == placementCells[y][x - 1].getShipSize()){
            return false;
        }
        if(x < 9 && placementCells[y][x].getShipSize() == placementCells[y][x + 1].getShipSize()){
            return false;
        }
        else{
            return true;
        }
    }
}
