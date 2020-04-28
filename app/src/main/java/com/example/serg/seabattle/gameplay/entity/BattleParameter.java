package com.example.serg.seabattle.gameplay.entity;

public class BattleParameter {
    private boolean attackSequence;
    private int firstPlayerHitCounter;
    private int secondPlayerHitCounter;

    public BattleParameter() {
        this.attackSequence = true;
        this.firstPlayerHitCounter = 0;
        this.secondPlayerHitCounter = 0;
    }

    public void toggleAttackSequence() {
        if (attackSequence) {
            attackSequence = false;
        } else {
            attackSequence = true;
        }
    }

    public boolean isAttackSequence() {
        return attackSequence;
    }

    public void increaseFirstPlayerHitCounter() {
        firstPlayerHitCounter++;
    }

    public int getFirstPlayerHitCounter() {
        return firstPlayerHitCounter;
    }

    public void increaseSecondPlayerHitCounter() {
        secondPlayerHitCounter++;
    }

    public int getSecondPlayerHitCounter() {
        return secondPlayerHitCounter;
    }
}
