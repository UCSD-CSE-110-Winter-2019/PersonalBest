package com.team2.team2_personalbest;

import java.io.Serializable;
import java.util.Observable;

public class ObservableTime extends Observable implements Serializable {

    private long time;

    public long getTime() {
        return time;
    }

    public void setValue(int time) {
        this.time = time;
        this.setChanged();
        this.notifyObservers(time);
    }
}
