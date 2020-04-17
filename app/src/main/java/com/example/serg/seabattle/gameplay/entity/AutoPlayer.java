package com.example.serg.seabattle.gameplay.entity;

import java.util.ArrayList;
import java.util.List;

public class AutoPlayer {
    private int firstSuccessAttackPosition;
    private int lastSuccessAttackPosition;
    private Boolean orientation;
    private List<Integer> availablePositions;

    public AutoPlayer() {
        this.lastSuccessAttackPosition = -1;
        this.firstSuccessAttackPosition = -1;
        this.orientation = null;
        availablePositions = new ArrayList<>();
    }

    public int getFirstSuccessAttackPosition() {
        return firstSuccessAttackPosition;
    }

    public void setFirstSuccessAttackPosition(int firstSuccessAttackPosition) {
        this.firstSuccessAttackPosition = firstSuccessAttackPosition;
    }

    public int getLastSuccessAttackPosition() {
        return lastSuccessAttackPosition;
    }

    public void setLastSuccessAttackPosition(int lastSuccessAttackPosition) {
        this.lastSuccessAttackPosition = lastSuccessAttackPosition;
    }

    public Boolean getOrientation() {
        return orientation;
    }

    public void setOrientation(Boolean orientation) {
        this.orientation = orientation;
    }

    public List<Integer> getAvailablePositions() {
        return availablePositions;
    }

    public void resetAvailablePositions() {
        this.availablePositions = new ArrayList<>();
    }
}
