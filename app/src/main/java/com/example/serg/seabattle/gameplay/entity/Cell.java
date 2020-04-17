package com.example.serg.seabattle.gameplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.serg.seabattle.common.enums.CellState;
import com.example.serg.seabattle.common.enums.ColorCellType;

public class Cell implements Parcelable {

    private CellState cellState;
    private int shipSize;
    private int nearShipCounter;

    private Integer pictureAddress;

    public Cell() {
        this.cellState = CellState.EMPTY;
        this.nearShipCounter = 0;
        this.shipSize = 0;
        this.pictureAddress = ColorCellType.WHITE_CELL.colorID;
    }

    public CellState getCellState() {
        return cellState;
    }

    public int getShipSize() {
        return shipSize;
    }

    public int getNearShipCounter() {
        return nearShipCounter;
    }

    public Integer getPictureAddress() {
        return pictureAddress;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public void setShipSize(int deckNumber) {
        shipSize = deckNumber;
    }

    public void setPictureAddress(Integer pictureAddress) {
        this.pictureAddress = pictureAddress;
    }

    public void increaseNearShipCounter() {
        nearShipCounter++;
    }

    public void decreaseNearShipCounter() {
        nearShipCounter--;
    }

    private Cell(Parcel in) {
        this.nearShipCounter = in.readInt();
        this.shipSize = in.readInt();
        this.cellState = CellState.valueOf(in.readString());
        this.pictureAddress = ColorCellType.WHITE_CELL.colorID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(nearShipCounter);
        parcel.writeInt(shipSize);
        parcel.writeString(cellState.name());
    }
}
