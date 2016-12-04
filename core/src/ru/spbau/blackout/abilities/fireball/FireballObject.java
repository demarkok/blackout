package ru.spbau.blackout.abilities.fireball;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Shape;

import ru.spbau.blackout.GameContext;
import ru.spbau.blackout.entities.Damageable;
import ru.spbau.blackout.graphic_effects.ParticleGraphicEffect;
import ru.spbau.blackout.entities.AbilityObject;
import ru.spbau.blackout.entities.GameObject;
import ru.spbau.blackout.entities.GameUnit;
import ru.spbau.blackout.java8features.Optional;
import ru.spbau.blackout.utils.Creator;
import ru.spbau.blackout.utils.Particles;

import static ru.spbau.blackout.abilities.fireball.FireballAbility.IMPULSE_FACTOR;


public final class FireballObject extends AbilityObject {
    private float timeRest;
    private float damage;


    protected FireballObject(FireballObject.Definition def, float x, float y) {
        super(def, x, y);
        this.timeRest = def.timeToLive;
        this.damage = def.damage;

        def.particleEffect.ifPresent(effect -> {
            this.graphicEffects.add(new ParticleGraphicEffect(this, effect.copy()));
        });
    }


    @Override
    public void beginContact(GameObject object) {
        super.beginContact(object);

        if (object instanceof GameUnit) {
            GameUnit unit = (GameUnit) object;
            // This object is going to dye. So, we don't care about changes of its velocity.
            this.velocity.add(this.body.getLinearVelocity()).scl(IMPULSE_FACTOR);
            unit.applyImpulse(this.velocity);
        }

        if (object instanceof Damageable) {
            ((Damageable) object).damage(this.damage);
        }

        this.kill();
    }

    @Override
    public void updateState(float deltaTime) {
        super.updateState(deltaTime);
        timeRest -= deltaTime;
        if (timeRest <= 0) {
            this.kill();
        }
    }

    @Override
    public void updateForSecondStep() {
        super.updateForSecondStep();
    }

    /**
     * Additionally defines timeToLive for an object.
     */
    public static class Definition extends AbilityObject.Definition {
        public static final String PARTICLES_PATH = "abilities/fireball/particles/fireball.pfx";


        public float timeToLive;
        public float damage;
        private /*final*/ Optional<ParticleEffect> particleEffect;


        public Definition(String modelPath, Creator<Shape> shapeCreator, float mass) {
            super(modelPath, shapeCreator, mass);
        }

        @Override
        public void load(GameContext context) {
            super.load(context);
            Particles.load(PARTICLES_PATH, context);
        }

        @Override
        public void doneLoading(GameContext context) {
            super.doneLoading(context);
            this.particleEffect = Particles.get(PARTICLES_PATH, context);
        }

        @Override
        public GameObject makeInstance(float x, float y) {
            return new FireballObject(this, x, y);
        }
    }
}
