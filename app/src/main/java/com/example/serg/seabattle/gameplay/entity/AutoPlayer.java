package com.example.serg.seabattle.gameplay.entity;

public class AutoPlayer {
    private int firstSuccessAttackPosition;
    private int lastSuccessAttackPosition;

    public AutoPlayer() {
        this.lastSuccessAttackPosition = -1;
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
}
