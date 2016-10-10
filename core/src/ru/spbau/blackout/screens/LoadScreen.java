package ru.spbau.blackout.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ru.spbau.blackout.BlackoutGame;
import ru.spbau.blackout.play.services.CorePlayServicesListener;
import ru.spbau.blackout.play.services.PlayServicesListenerInCore;
import ru.spbau.blackout.utils.ScreenManager;

public class LoadScreen extends StageScreen implements CorePlayServicesListener {

    private static final Color LABEL_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(0.2f, 0.2f, 0.2f, 1.0f);

    private static final float LABEL_SCALE = 1.5f;
    private static final float LABEL_BOTTOM_PADDING = 25.0f;

    private static final String STARTED_LOG_IN = "Logging in...";
    private static final String STARTED_LOADING = "Loading your game info...";

    private Table middleTable;

    public LoadScreen(BlackoutGame blackoutGame) {
        super(blackoutGame);
    }

    @Override
    public void show() {
        super.show();

        PlayServicesListenerInCore.getInstance().addListener(this);

        middleTable = new Table();
        middleTable.setFillParent(true);
        stage.addActor(middleTable);

        addLabel(STARTED_LOG_IN);

        game.playServices.signIn();
    }

    @Override
    public void onSignInFailed() {
        game.playServices.signIn();
    }

    @Override
    public void onSignInSucceeded() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                addLabel(STARTED_LOADING);
            }
        });
        game.playServices.startLoadingSnapshot();
    }

    @Override
    public void finishedLoadingSnapshot() {
        if (game.getSnapshot() == null) {
            Gdx.app.exit();
        }

        PlayServicesListenerInCore.getInstance().removeListener(this);
        middleTable.remove();
        middleTable = null;

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                ScreenManager.getInstance().setScreen(new MenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                BACKGROUND_COLOR.r, BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    private Label addLabel(CharSequence text) {
        final BitmapFont font = new BitmapFont();
        font.getData().setScale(LABEL_SCALE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        final Label.LabelStyle style = new Label.LabelStyle(font, LABEL_COLOR);
        final Label label = new Label(text , style);

        middleTable.add(label).pad(LABEL_BOTTOM_PADDING).row();

        return label;
    }

}
