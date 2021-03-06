package ru.spbau.blackout.ingameui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ru.spbau.blackout.ingameui.objects.AbilityIcon;
import ru.spbau.blackout.ingameui.objects.HealthBar;
import ru.spbau.blackout.ingameui.objects.UnitControlStick;
import ru.spbau.blackout.network.UIServer;


/**
 * Main class for in-game user interface.
 */
public class PlayerUI extends IngameUI {

    // FIXME: calculate positions with regard to aspect ratio
    private static final Vector2[] ABILITY_ICONS_POS = {
            new Vector2(1100, 450),
            new Vector2(1100, 300),
            new Vector2(1100, 150)
    };

    public PlayerUI(Stage stage, UIServer server) {
        super(stage);

        addUiObject(new UnitControlStick(getStage(), server));
        addUiObject(new HealthBar(getStage()));

        for (int i = 0; i < ABILITY_ICONS_POS.length; i++) {
            addUiObject(new AbilityIcon(getStage(), server, i, ABILITY_ICONS_POS[i]));
        }
    }
}
