package ru.spbau.blackout.play.services;

import com.badlogic.gdx.utils.Array;

public class PlayServicesInCore implements PlayServicesListener {

    private PlayServices playServices;
    private final Array<CorePlayServicesListener> listeners = new Array<>();

    public PlayServicesInCore(PlayServices playServices) {
        this.playServices = playServices;
    }

    public PlayServices getPlayServices() {
        return playServices;
    }

    @Override
    public void onSignInSucceeded() {
        for (CorePlayServicesListener listener : listeners) {
            listener.onSignInSucceeded();
        }
    }

    public void addListener(CorePlayServicesListener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(CorePlayServicesListener listener) {
        return listeners.removeValue(listener, true);
    }
}
