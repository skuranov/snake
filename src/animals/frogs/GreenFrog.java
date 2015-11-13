package animals.frogs;

import animals.Frog;
import base.SnakeGame;

import java.awt.*;

/**
 * Created by Sergei_Kuranov on 11/13/2015.
 */
public class GreenFrog extends Frog {
    @Override
    protected int getLifeCycle() {
        return 0;
    }

    @Override
    public Color getColor() {
        return new Color(65, 200, 67);
    }

    public GreenFrog(SnakeGame snakeGame) {
        super(snakeGame);
    }
}
