package ru.spbau.blackout.abilities.fireball;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

import ru.spbau.blackout.GameContext;
import ru.spbau.blackout.abilities.InstantAbility;
import ru.spbau.blackout.entities.GameUnit;
import ru.spbau.blackout.entities.AbilityObject;
import ru.spbau.blackout.java8features.Optional;
import ru.spbau.blackout.shapescreators.CircleCreator;
import ru.spbau.blackout.utils.Particles;


public class FireballAbility extends InstantAbility {
    public static final String ICON_PATH = "abilities/fireball/icon.png";
    public static final float MAX_CHARGE_TIME = 1f;

    public static final float SHELL_START_SPEED = 30f;
    public static final float CAST_DISTANCE = 1f;
    public static final float SHELL_MASS = 5f;
    /** The estimated distance of the ability in case of no external force. */
    public static final float DISTANCE = 12f;
    public static final float TIME_TO_LIVE = DISTANCE / SHELL_START_SPEED;


    private final AbilityObject.Definition shellDef;


    public FireballAbility(int level) {
        super(level);
        this.shellDef = new FireballObject.Definition(null, new CircleCreator(1), SHELL_MASS, TIME_TO_LIVE);
        this.shellDef.pivotHeight = 1.5f;
    }


    @Override
    public void cast() {
        Vector2 direction = new Vector2(1, 0).rotateRad(getUnit().getRotation());
        Vector2 position = new Vector2(getUnit().getPosition());
        position.mulAdd(direction, CAST_DISTANCE);
        AbilityObject shell = (AbilityObject) shellDef.makeInstance(position);

        shell.velocity.mulAdd(direction, SHELL_START_SPEED);
    }

    @Override
    public void load(GameContext context) {
        super.load(context);
        this.shellDef.load(context);

    }

    @Override
    public void doneLoading(GameContext context, GameUnit unit) {
        super.doneLoading(context, unit);
        this.shellDef.doneLoading(context);
    }

    @Override
    public String iconPath() { return ICON_PATH; }
    @Override
    public float getMaxChargeTime() { return MAX_CHARGE_TIME; }
}
