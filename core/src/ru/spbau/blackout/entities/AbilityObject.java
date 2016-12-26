package ru.spbau.blackout.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Shape;

import org.jetbrains.annotations.Nullable;

import ru.spbau.blackout.GameContext;
import ru.spbau.blackout.utils.Creator;

import static ru.spbau.blackout.settings.GameSettings.SOUND_MAX_VOLUME;


/**
 * Class for objects created by abilities.
 */
public abstract class AbilityObject extends DynamicObject {

    protected AbilityObject(Definition def, long uid, float x, float y) {
        super(def, uid, x, y);

        if (def.getCastSound() != null) {
            def.getCastSound().play(getDef().getContext().getSettings().soundVolume * SOUND_MAX_VOLUME);
        }
    }

    /** Calls when the object contacts with another object. */
    public void beginContact(GameObject object) {}

    @Override
    public void updateForSecondStep() {
        super.updateForSecondStep();
        body.setLinearVelocity(0, 0);
    }


    public static abstract class Definition extends DynamicObject.Definition {

        private static final long serialVersionUID = 1000000000L;

        @Nullable
        private final String castSoundPath;
        @Nullable
        private  /*final*/ transient Sound castSound;

        public Definition(@Nullable String modelPath, Creator<Shape> shapeCreator,
                          @Nullable String deathEffectPath, @Nullable String castSoundPath) {
            super(modelPath, shapeCreator, deathEffectPath);
            this.castSoundPath = castSoundPath;
        }

        @Override
        public void load(GameContext context) {
            super.load(context);
            System.out.println(castSound);
            if (castSoundPath != null) {
                context.getAssets().load(castSoundPath, Sound.class);
            }
        }

        @Override
        public void doneLoading() {
            super.doneLoading();
            if (castSoundPath != null) {
                castSound = getContext().getAssets().get(castSoundPath, Sound.class);
            }
        }

        @Nullable
        public final Sound getCastSound() {
            return castSound;
        }
    }
}
