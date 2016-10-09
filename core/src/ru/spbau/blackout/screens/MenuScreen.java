package ru.spbau.blackout.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import ru.spbau.blackout.BlackoutGame;
import ru.spbau.blackout.utils.ScreenManager;

class MenuScreen extends StageScreen {

    private static final Color  MENU_BACKGROUND_COLOR = new Color(0.2f, 0.2f, 0.2f, 1.0f);
    static final String MENU_BUTTON_UP_TEXTURE_PATH = "images/menuscreen/button_up.png";
    static final String MENU_BUTTON_DOWN_TEXTURE_PATH = "images/menuscreen/button_down.png";
    private static final String MENU_SETTINGS_TEXTURE_PATH = "images/menuscreen/settings.png";
    private static final String MENU_GAME_SERVICES_TEXTURE_PATH = "images/menuscreen/games_controller_grey.png";

    private static final float TEXT_SCALE = 1.5f;
    private static final float MENU_BUTTON_PADDING = 10.0f;
    private static final float CORNER_LABEL_MARGIN = 20.0f;
    private static final float MENU_SETTINGS_ICON_SIZE = 128.0f;
    private static final float MENU_SETTINGS_ICON_PADDING = 12.0f;

    private Label goldLabel;
    private Table middleTable;

    MenuScreen(BlackoutGame blackoutGame) {
        super(blackoutGame);
        addLeftPaneElements();
        addRightPaneElements();
        changeMiddleTable(MainMenuTable.getTable(blackoutGame, this));
    }

    void changeMiddleTable(Table table) {
        if (middleTable != null) {
            middleTable.remove();
        }
        middleTable = table;
        stage.addActor(middleTable);
    }

    private void addLeftPaneElements() {
        addLabelWithTextAt(
                "Hello, " + blackoutGame.playServices.getPlayerName(),
                CORNER_LABEL_MARGIN,
                stage.getViewport().getWorldHeight() - CORNER_LABEL_MARGIN,
                Align.topLeft);
        final Actor playServicesIcon = addGooglePlayGamesServicesIcon();
        addSettingsIcon(playServicesIcon);
    }

    private void addRightPaneElements() {
        goldLabel = addLabelWithTextAt("", 0, 0, 0);
    }

    static TextButton addButton(Table table, String text, Drawable upImage, Drawable downImage, EventListener listener) {
        final BitmapFont font = new BitmapFont();
        font.getData().setScale(TEXT_SCALE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        final TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = upImage;
        style.down = downImage;

        final TextButton button = new TextButton(text, style);
        if (listener != null) {
            button.addListener(listener);
        }

        table.add(button).pad(MENU_BUTTON_PADDING).row();

        return button;
    }

    private Label addLabelWithTextAt(CharSequence text, float x, float y, int align) {
        final Label.LabelStyle style = new Label.LabelStyle();
        final BitmapFont font = new BitmapFont();
        font.getData().scale(TEXT_SCALE);
        style.font = font;

        final Label label = new Label(text, style);
        label.setPosition(x, y, align);
        stage.addActor(label);

        return label;
    }

    private Image addSettingsIcon(final Actor controlledImage) {
        final Texture settingsTexture = new Texture(MENU_SETTINGS_TEXTURE_PATH);
        final Image settingsImage = new Image(settingsTexture);

        settingsImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controlledImage.setVisible(!controlledImage.isVisible());
            }
        });

        final Container<Image> settingsContainer = new Container<Image>(settingsImage);
        settingsContainer.setWidth(MENU_SETTINGS_ICON_SIZE);
        settingsContainer.setHeight(MENU_SETTINGS_ICON_SIZE);
        settingsContainer.pad(MENU_SETTINGS_ICON_PADDING);
        stage.addActor(settingsContainer);

        return settingsImage;
    }

    private Image addGooglePlayGamesServicesIcon() {
        final Texture gamesServices = new Texture(MENU_GAME_SERVICES_TEXTURE_PATH);
        Image gamesServicesImage = new Image(gamesServices);
        gamesServicesImage.setVisible(false);

        gamesServicesImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blackoutGame.playServices.signOut();
                ScreenManager.getInstance().disposeScreen();
            }
        });

        final Container<Image> playContainer = new Container<Image>(gamesServicesImage);
        playContainer.setWidth(MENU_SETTINGS_ICON_SIZE);
        playContainer.setHeight(MENU_SETTINGS_ICON_SIZE);
        playContainer.pad(MENU_SETTINGS_ICON_PADDING);
        playContainer.setY(MENU_SETTINGS_ICON_SIZE);
        stage.addActor(playContainer);

        return gamesServicesImage;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                MENU_BACKGROUND_COLOR.r, MENU_BACKGROUND_COLOR.g,
                MENU_BACKGROUND_COLOR.b, MENU_BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        refreshGoldLabel();

        super.render(delta);
    }

    private void refreshGoldLabel() {
        goldLabel.setText("Gold: " + blackoutGame.getSnapshot().getGold());
        goldLabel.setSize(goldLabel.getPrefWidth(), goldLabel.getPrefHeight());
        goldLabel.setPosition(
                stage.getViewport().getWorldWidth() - CORNER_LABEL_MARGIN,
                stage.getViewport().getWorldHeight() - CORNER_LABEL_MARGIN,
                Align.topRight);
    }

}
