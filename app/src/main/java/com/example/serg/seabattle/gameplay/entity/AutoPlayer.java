package com.example.serg.seabattle.gameplay.entity;

public class AutoPlayer {
    private int firstSuccessAttackPosition;
    private int lastSuccessAttackPosition;
    private Boolean orientation;

    public AutoPlayer() {
        this.lastSuccessAttackPosition = -1;
        this.firstSuccessAttackPosition = -1;
        this.orientation = null;
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
}
