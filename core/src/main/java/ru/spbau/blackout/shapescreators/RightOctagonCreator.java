package ru.spbau.blackout.shapescreators;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;

import ru.spbau.blackout.utils.Creator;

public class RightOctagonCreator implements Creator<Shape> {

    private final static int EDGES = 8;

    public float radius;

    public RightOctagonCreator(float radius) {
        this.radius = radius;
    }

    @Override
    public Shape create() {
        float radius = this.radius / MathUtils.cos(MathUtils.PI / 8f);

        final Vector2[] vertices = new Vector2[EDGES + 1];
        for (int i = 0; i <= EDGES; i++) {
            final float x = radius * MathUtils.cos((2 * i + 1) * MathUtils.PI / EDGES);
            final float y = radius * MathUtils.sin((2 * i + 1) * MathUtils.PI / EDGES);
            vertices[i] = new Vector2(x, y);
        }

        final ChainShape shape = new ChainShape();
        shape.createChain(vertices);
        return shape;
    }
}
