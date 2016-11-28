package ru.spbau.blackout.entities;

import com.badlogic.gdx.physics.box2d.Shape;

import java.io.Serializable;

import ru.spbau.blackout.GameContext;
import ru.spbau.blackout.abilities.Ability;
import ru.spbau.blackout.utils.Creator;


public class Character extends GameUnit {
    public Character(GameUnit.Definition def, float x, float y, GameContext context) {
        super(def, x, y, context);
    }


    public static class Definition extends GameUnit.Definition implements Serializable {
        public Definition(String modelPath, Creator<Shape> shapeCreator, float initialX, float initialY,
                          Ability[] abilities) {
            super(modelPath, shapeCreator, initialX, initialY, abilities);
        }


        @Override
        public GameObject makeInstance(float x, float y) {
            return new Character(this, x, y, getContext());
        }
    }
}
