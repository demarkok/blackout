package ru.spbau.blackout.utils;

import com.badlogic.gdx.utils.Array;

import ru.spbau.blackout.BlackoutGame;
import ru.spbau.blackout.screens.BlackoutScreen;

import static ru.spbau.blackout.java8features.Functional.foreach;

/**
 * Used to put a new getScreen instead of last one but also saves references to screens for reusing.
 */
public class ScreenManager {

    private final Array<BlackoutScreen> screens = new Array<>();

    public void setScreen(BlackoutScreen screen) {
        screens.add(screen);
        BlackoutGame.get().setScreen(screen);
    }

    public void disposeScreen() {
        disposeScreens(1);
    }

    public void disposeScreens(int number) {
        for (int i = 0; i < number; i++) {
            if (screens.size == 1) {
                break;
            }
            screens.pop().dispose();
        }
        BlackoutGame.get().setScreen(screens.peek());
    }

    public void dispose() {
        foreach(screens, BlackoutScreen::dispose);
        screens.clear();
    }
}
