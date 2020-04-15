package com.example.serg.seabattle.gameplay.entity;

public class Fleet {
    private int fourDeckShipsAmount;
    private int threeDeckShipsAmount;
    private int doubleDeckShipsAmount;
    private int singleDeckShipsAmount;

    public Fleet() {
        this.fourDeckShipsAmount = 1;
        this.threeDeckShipsAmount = 2;
        this.doubleDeckShipsAmount = 3;
        this.singleDeckShipsAmount = 4;
    }

    public int getFourDeckShipsAmount() {
        return fourDeckShipsAmount;
    }

    public void increaseFourDeckShipsAmount() {
        this.fourDeckShipsAmount++;
    }

    public void decreaseFourDeckShipsAmount() {
        this.fourDeckShipsAmount--;
    }

    public int getThreeDeckShipsAmount() {
        return threeDeckShipsAmount;
    }

    public void increaseThreeDeckShipsAmount() {
        this.threeDeckShipsAmount++;
    }

    public void decreaseThreeDeckShipsAmount() {
        this.threeDeckShipsAmount--;
    }

    public int getDoubleDeckShipsAmount() {
        return doubleDeckShipsAmount;
    }

    public void increaseDoubleDeckShipsAmount() {
        this.doubleDeckShipsAmount++;
    }

    public void decreaseDoubleDeckShipsAmount() {
        this.doubleDeckShipsAmount--;
    }

    public int getSingleDeckShipsAmount() {
        return singleDeckShipsAmount;
    }

    public void increaseSingleDeckShipsAmount() {
        this.singleDeckShipsAmount++;
    }

    public void decreaseSingleDeckShipsAmount() {
        this.singleDeckShipsAmount--;
    }
}
