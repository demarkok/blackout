package ru.spbau.blackout.screens;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ru.spbau.blackout.BlackoutGame;
import ru.spbau.blackout.database.PlayerEntity;
import ru.spbau.blackout.entities.Character;

public class UpgradesTable {

    private static final float BUTTON_WIDTH = 300.0f;
    private static final float BUTTON_HEIGHT = 50.0f;
    private static final float BUTTON_PADDING = 10.0f;

    private static final String HEALTH_UPGRADE = "Increase health";
    private static final String HEALTH_CURRENT = "max health";
    private static final String ABILITY_UPGRADE = "Upgrade";
    private static final String ABILITY_CURRENT = "level";

    private static final String BACK_TEXT = "Back";

    public static Table getTable(final MenuScreen screen) {
        final Table middleTable = new Table();

        addRowWithButtonAndLabel(
                middleTable,
                HEALTH_UPGRADE,
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        BlackoutGame.get().getPlayerEntity().upgradeHealth();
                        super.clicked(event, x, y);
                    }
                },
                new Label("", BlackoutGame.get().assets().getDefaultSkin()) {
                    @Override
                    public void act(float delta) {
                        setText(HEALTH_CURRENT + " " +
                                BlackoutGame.get().getPlayerEntity().getDeserializedCharacterDefinition().maxHealth);
                        super.act(delta);
                    }
                });

        final PlayerEntity entity = BlackoutGame.get().getPlayerEntity();
        final Character.Definition characterDefinition = entity.getDeserializedCharacterDefinition();
        for (int abilityIndex = 0; abilityIndex < characterDefinition.abilities.length; abilityIndex++) {
            final int currentAbilityIndex = abilityIndex;
            addRowWithButtonAndLabel(
                    middleTable,
                    ABILITY_UPGRADE + " " +
                            characterDefinition.abilities[currentAbilityIndex].getClass().getSimpleName(),
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            screen.changeMiddleTable(MainMenuTable.getTable(screen));
                            super.clicked(event, x, y);
                        }
                    },
                    new Label("", BlackoutGame.get().assets().getDefaultSkin()) {
                        @Override
                        public void act(float delta) {
                            setText(ABILITY_CURRENT + " " +
                                    BlackoutGame.get().getPlayerEntity().getDeserializedCharacterDefinition()
                                            .abilities[currentAbilityIndex].getLevel());
                            super.act(delta);
                        }
                    });
        }

        final TextButton button = new TextButton(BACK_TEXT, BlackoutGame.get().assets().getDefaultSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeMiddleTable(MainMenuTable.getTable(screen));
            }
        });
        middleTable.add(button).colspan(2).fill(true, false).pad(BUTTON_PADDING).height(BUTTON_HEIGHT);

        middleTable.setFillParent(true);
        return middleTable;
    }

    private static void addRowWithButtonAndLabel(Table table, String buttonText,
                                                 EventListener buttonListener, Label labelAfter) {
        final TextButton button = new TextButton(buttonText, BlackoutGame.get().assets().getDefaultSkin());
        button.addListener(buttonListener);
        table.add(button).pad(BUTTON_PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        table.add(labelAfter).row();
    }
}