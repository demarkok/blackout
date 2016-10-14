package ru.spbau.blackout.play.services;

import java.io.Serializable;

public class BlackoutSnapshot implements Serializable {

    private int gold;
    private int rating;

    public BlackoutSnapshot() {
        gold = 0;
        rating = 0;
        saveState();
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
        saveState();
    }

    public void changeGold(int delta) {
        setGold(getGold() + delta);
    }

    private void saveState() {
        PlayServicesInCore.getInstance().getPlayServices().saveSnapshot(this);
    }

}
