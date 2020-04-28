package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.common.enums.ShipType;
import com.example.serg.seabattle.gameplay.entity.Cell;
import com.example.serg.seabattle.gameplay.entity.Fleet;

import java.util.Random;

public class AutoDisposalService {
    private static AutoDisposalService autoDisposalService;

    private ManualDisposalService manualDisposalService;
    private Random randomGenerator;

    private AutoDisposalService() {
        manualDisposalService = ManualDisposalService.getManualDisposalService();
        randomGenerator = new Random();
    }

    public static AutoDisposalService getAutoDisposalService() {
        if (autoDisposalService == null) {
            autoDisposalService = new AutoDisposalService();
        }
        return autoDisposalService;
    }

    public Cell[] setAutoDisposal(Fleet fleet, Cell[] cells) {
        boolean orientation;
        int position;
        while (fleet.getFourDeckShipsAmount() != 0) {
            orientation = generateRandomOrientation();
            position = generateRandomPosition();
            if (manualDisposalService.isSetShipAllowed(cells, position, ShipType.FOUR_DECK.deckNumber, orientation)) {
                cells = manualDisposalService.setShip(cells, position, ShipType.FOUR_DECK.deckNumber, orientation);
                fleet.decreaseFourDeckShipsAmount();
            }
        }

        while (fleet.getThreeDeckShipsAmount() != 0) {
            orientation = generateRandomOrientation();
            position = generateRandomPosition();
            if (manualDisposalService.isSetShipAllowed(cells, position, ShipType.THREE_DECK.deckNumber, orientation)) {
                cells = manualDisposalService.setShip(cells, position, ShipType.THREE_DECK.deckNumber, orientation);
                fleet.decreaseThreeDeckShipsAmount();
            }
        }
        while (fleet.getDoubleDeckShipsAmount() != 0) {
            orientation = generateRandomOrientation();
            position = generateRandomPosition();
            if (manualDisposalService.isSetShipAllowed(cells, position, ShipType.DOUBLE_DECK.deckNumber, orientation)) {
                cells = manualDisposalService.setShip(cells, position, ShipType.DOUBLE_DECK.deckNumber, orientation);
                fleet.decreaseDoubleDeckShipsAmount();
            }
        }
        while (fleet.getSingleDeckShipsAmount() != 0) {
            orientation = generateRandomOrientation();
            position = generateRandomPosition();
            if (manualDisposalService.isSetShipAllowed(cells, position, ShipType.SINGLE_DECK.deckNumber, orientation)) {
                cells = manualDisposalService.setShip(cells, position, ShipType.SINGLE_DECK.deckNumber, orientation);
                fleet.decreaseSingleDeckShipsAmount();
            }
        }
        return cells;
    }

    private boolean generateRandomOrientation() {
        return this.randomGenerator.nextInt(2) == 1;
    }

    private int generateRandomPosition() {
        return this.randomGenerator.nextInt(100);
    }
}
