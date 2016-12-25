package ru.spbau.blackout.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import ru.spbau.blackout.GameContext;
import ru.spbau.blackout.entities.Character;
import ru.spbau.blackout.network.UIServer;
import ru.spbau.blackout.utils.HasState;


/**
 * Abstract class for ability in-game representation.
 * Each character has its own instance of each its ability.
 */
public abstract class Ability implements Serializable, HasState {

    private static final long serialVersionUID = 1000000000L;

    protected int level;
    private transient float chargeTime;

    private transient /*final*/ Character character;


    public Ability(int level) {
        this.level = level;
    }


    @Override
    public void getState(ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeFloat(chargeTime);
    }

    @Override
    public void setState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        setChargeTime(in.readFloat());
    }

    /**
     * Load necessary assets.
     * Must be called once from <code>GameUnit.Definition::updateState</code>
     */
    public void load(GameContext context) {
        // loading of icon has to be here because it isn't accessible from `AbilityIcon` class in the loading stage
        context.getAssets().load(iconPath(), Texture.class);
    }

    public void doneLoading(GameContext context) {}

    /**
     * Must be called exactly once from <code>GameUnit::new</code>.
     */
    public void initialize(Character character) {
        this.character = character;
    }


    /** Called once when a touch goes down on the icon. */
    public abstract void onCastStart(UIServer server);
    /** Called each frame when the icon is pressed. */
    public abstract void onCastEnd(UIServer server);
    /** Called once when a touch goes up. */
    public abstract void inCast(UIServer server, float delta);

    public void cast(Vector2 targetOffset) {
        chargeStart();
    }

    /** Path to the icon in assets directory. */
    public abstract String iconPath();

    public abstract float getMaxChargeTime();

    public Character getCharacter() { return character; }
    public float getChargeTime() { return chargeTime; }
    public int getLevel() { return level; }
    public void setChargeTime(float chargeTime) { this.chargeTime = chargeTime; }
    abstract public void increaseLevel();
    public void charge(float deltaTime) {
        chargeTime -= Math.min(deltaTime, chargeTime);
    }

    /** Sets current charge time as <code>maxChargeTime</code>. */
    public void chargeStart() { setChargeTime(getMaxChargeTime()); }
}
