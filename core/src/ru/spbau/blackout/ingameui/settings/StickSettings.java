package ru.spbau.blackout.ingameui.settings;

import com.badlogic.gdx.math.Vector2;

import ru.spbau.blackout.units.Rpx;

/**
 * Contains some settings for displaying of this UI unit.
 * All getters and setters work with RPX.
 */
public class StickSettings {
    private Vector2 start = new Vector2(100, 100);  // FIXME
    public void setStart(Vector2 start) { this.start = start; }
    public Vector2 getStart() { return start; }
}