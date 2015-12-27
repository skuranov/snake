package control.moveanimals.movefrogs;

import control.moveanimals.FrogControl;
import control.SnakeGame;

import java.awt.*;


public class GreenFrogControl extends FrogControl {
    @Override
    public int getLifeCycle() {
        return 0;
    }

    @Override
    public Color getColor() {
        return new Color(65, 200, 67);
    }

    public GreenFrogControl(SnakeGame snakeGame) {
        super(snakeGame);
    }
}
