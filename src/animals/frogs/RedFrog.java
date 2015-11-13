package animals.frogs;

import animals.Frog;
import base.SnakeGame;

import java.awt.*;

/**
 * Created by Sergei_Kuranov on 11/13/2015.
 */
public class RedFrog extends Frog {
    @Override
    protected int getLifeCycle() {
        return 0;
    }

    @Override
    public Color getColor() {
        return new Color(157, 25, 46);
    }

    public RedFrog(SnakeGame snakeGame) {
        super(snakeGame);
    }
}
