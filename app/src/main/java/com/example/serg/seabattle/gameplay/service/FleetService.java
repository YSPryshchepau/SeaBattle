package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.gameplay.entity.Fleet;

public class FleetService {
    private static FleetService fleetService;

    private FleetService() {

    }

    public static FleetService getFleetService() {
        if(fleetService == null) {
            fleetService = new FleetService();
        }
        return fleetService;
    }


    public void increaseShipTypeByDeckNumber(Fleet fleet, int deckNumber) {
        switch (deckNumber) {
            case 4: {
                fleet.increaseFourDeckShipsAmount();
                break;
            }
            case 3: {
                fleet.increaseThreeDeckShipsAmount();
                break;
            }
            case 2: {
                fleet.increaseDoubleDeckShipsAmount();
                break;
            }
            case 1: {
                fleet.increaseSingleDeckShipsAmount();
                break;
            }
        }
    }

    public void decreaseShipTypeByDeckNumber(Fleet fleet, int deckNumber) {
        switch (deckNumber) {
            case 4: {
                fleet.decreaseFourDeckShipsAmount();
                break;
            }
            case 3: {
                fleet.decreaseThreeDeckShipsAmount();
                break;
            }
            case 2: {
                fleet.decreaseDoubleDeckShipsAmount();
                break;
            }
            case 1: {
                fleet.decreaseSingleDeckShipsAmount();
                break;
            }
        }
    }

    public int getShipAmountByDeckNumber(Fleet fleet, int deckNumber) {
        switch (deckNumber) {
            case 4: {
                return fleet.getFourDeckShipsAmount();
            }
            case 3: {
                return fleet.getThreeDeckShipsAmount();
            }
            case 2: {
                return fleet.getDoubleDeckShipsAmount();
            }
            case 1: {
                return fleet.getSingleDeckShipsAmount();
            }
        }
        return 0;
    }

    public boolean isNotAllowedShips(Fleet fleet) {
        if(fleet.getFourDeckShipsAmount() != 0) {
            return false;
        }
        if(fleet.getThreeDeckShipsAmount() != 0) {
            return false;
        }
        if(fleet.getDoubleDeckShipsAmount() != 0) {
            return false;
        }
        if(fleet.getSingleDeckShipsAmount() != 0) {
            return false;
        }
        return true;
    }
}
