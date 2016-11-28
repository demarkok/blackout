package ru.spbau.blackout.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ru.spbau.blackout.GameContext;
import ru.spbau.blackout.utils.Creator;
import ru.spbau.blackout.utils.InplaceSerializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static ru.spbau.blackout.utils.Utils.fixTop;


public abstract class GameObject implements RenderableProvider, InplaceSerializable, Serializable {
    // physics:
    transient protected Body body;
    private float height;

    // appearance:
    transient protected ModelInstance model;


    protected GameObject(Definition def, float x, float y) {
        this.model = model == null ? null : new ModelInstance(model);

        body = def.registerObject(this);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = def.shapeCreator.create();
        fixtureDef.density = def.density;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

        setPosition(x, y);
    }


    @Override
    public void inplaceSerialize(ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject(this);
        //out.writeObject(this.getPosition());
        //out.writeFloat(this.getRotation());
    }

    @Override
    public Object inplaceDeserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
        GameObject other = (GameObject) in.readObject();
        //this.height = other.height;
        //Vector2 position = (Vector2) in.readObject();
        //float rotation = in.readFloat();
        //this.setTransform(position, rotation);
        return other;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        updateTransform();
        model.getRenderables(renderables, pool);
    }


    /**
     * Update things not connected with physics.
     */
    public void updateState(float delta) {}

    public void updateForFirstStep() {}
    public void updateForSecondStep() {}

    // Transform:

    public void setTransform(float x, float y, float angle) {
        body.setTransform(x, y, angle);
    }

    public void setTransform(Vector2 position, float angle) {
        body.setTransform(position, angle);
    }

    protected void updateTransform() {
        model.transform.setToRotationRad(Vector3.Z, body.getAngle());
        fixTop(model);
        Vector2 pos = body.getPosition();
        model.transform.setTranslation(pos.x, pos.y, height);
    }

    // Rotation

    /** Set rotation in radians. */
    public void setRotation(float angle) {
        Vector2 pos = getPosition();
        setTransform(pos.x, pos.y, angle);
    }

    /** Returns the current rotation in radians. */
    public float getRotation() {
        return body.getAngle();
    }

    /** Rotates object to the given direction. */
    public void setDirection(Vector2 direction) {
        setRotation(direction.angleRad());
    }


    // Position:

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void setPosition(Vector2 position) {
        setTransform(position, getRotation());
    }

    public void setPosition(float x, float y) {
        setTransform(x, y, getRotation());
    }


    public void setHeight(float height) { this.height = height; }
    public final float getHeight() { return height; }


    /**
     * Used to send via network a definition of an object to create.
     * Each kind of objects must have its own <code>Definition</code> subclass.
     *
     * <p>Life cycle:
     * <br>constructor (once)
     * <br>load (once)
     * <br>doneLoading (once)
     * <br>makeInstance (Any number of calls)
     */
    public static abstract class Definition implements Serializable {
        public static final float DEFAULT_HEIGHT = 0;
        public static final float DEFAULT_ROTATION = 0;

        public static final float DEFAULT_DENSITY = 1f;


        // physics
        public float rotation = DEFAULT_ROTATION;
        public float height = DEFAULT_HEIGHT;

        public float density = DEFAULT_DENSITY;
        /**
         * As far as Shape itself isn't serializable,
         * supplier will be sent instead.
         */
        public Creator<Shape> shapeCreator;
        public final Vector2 position = new Vector2();

        /** The loaded model object. Initialized by <code>doneLoading</code> method. */
        private transient Model model;
        /** Saving context is necessary in order to be able to make instances. */
        private transient GameContext context;

        // appearance:
        public String modelPath;


        /**
         * Warning: ShapeCreator must be serializable.
         */
        public Definition(String modelPath, Creator<Shape> shapeCreator, float initialX, float initialY) {
            this.modelPath = modelPath;
            this.shapeCreator = shapeCreator;
            this.position.set(initialX, initialY);
        }

        /** Load necessary assets. */
        public void load(GameContext context) {
            context.assets().ifPresent(assets ->
                assets.load(this.modelPath, Model.class)
            );
        }

        /** When assets are loaded. */
        public void doneLoading(GameContext context) {
            this.context = context;
            context.assets().ifPresent(assets ->
                this.model = assets.get(this.modelPath, Model.class)
            );
        }

        public abstract GameObject makeInstance(float x, float y);
        public GameObject makeInstance(Vector2 position) {
            return this.makeInstance(position.x, position.y);
        }
        public GameObject makeInstance() {
            return this.makeInstance(this.position);
        }

        /** Rotates object to the given direction. */
        public void setDirection(Vector2 direction) {
            this.rotation = direction.angleRad();
        }

        protected GameContext getContext() { return context; }


        /**
         * Must be called from <code>GameObject</code> constructor to add this
         * <code>GameObject</code> to the world and make <code>Body</code> for it.
         */
        private Body registerObject(GameObject object) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(this.position);
            bodyDef.type = getBodyType();

            return context.gameWorld().addObject(object, bodyDef);
        }

        public abstract BodyDef.BodyType getBodyType();
    }
}
