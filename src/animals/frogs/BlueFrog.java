package animals.frogs;

import animals.Frog;
import base.SnakeGame;

import java.awt.*;

/**
 * Created by Sergei_Kuranov on 11/13/2015.
 */
public class BlueFrog extends Frog {
    @Override
    protected int getLifeCycle() {
        return 100;
    }

    @Override
    public Color getColor() {
        return new Color(78, 147, 202);
    }

    public BlueFrog(SnakeGame snakeGame) {
        super(snakeGame);
    }
}
