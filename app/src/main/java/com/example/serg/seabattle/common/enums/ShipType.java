package com.example.serg.seabattle.common.enums;

import com.example.serg.seabattle.R;

public enum ShipType {
    FOUR_DECK(R.id.fourDeckBtn, R.id.fourDeckText, 4, 0,
            R.drawable.not_clicked_four_deck_ship, R.drawable.clicked_four_deck_ship, R.drawable.empty_four_deck_ship),
    THREE_DECK(R.id.threeDeckBtn, R.id.threeDeckText, 3, 1,
            R.drawable.not_clicked_three_deck_ship, R.drawable.clicked_three_deck_ship, R.drawable.empty_three_deck_ship),
    DOUBLE_DECK(R.id.doubleDeckBtn, R.id.doubleDeckText, 2, 2,
            R.drawable.not_clicked_double_deck_ship, R.drawable.clicked_double_deck_ship, R.drawable.empty_double_deck_ship),
    SINGLE_DECK(R.id.singleDeckBtn, R.id.singleDeckText, 1, 3,
            R.drawable.not_clicked_single_deck_ship, R.drawable.clicked_single_deck_ship, R.drawable.empty_single_deck_ship);

    public final int buttonID;
    public final int textID;
    public final int deckNumber;
    public final int inAmountsNumber;
    public final int notClickedPicture;
    public final int clickedPicture;
    public final int emptyPicture;

    ShipType(int buttonID, int textID, int deckNumber, int inAmountsNumber,
             int notClickedPicture, int clickedPicture, int emptyPicture) {
        this.buttonID = buttonID;
        this.textID = textID;
        this.deckNumber = deckNumber;
        this.inAmountsNumber = inAmountsNumber;
        this.notClickedPicture = notClickedPicture;
        this.clickedPicture = clickedPicture;
        this.emptyPicture = emptyPicture;
    }

    public static ShipType findElementByDeckNumber(int deckNumber) {
        for (ShipType shipType : ShipType.values()) {
            if (shipType.deckNumber == deckNumber) {
                return shipType;
            }
        }
        return null;
    }
}
