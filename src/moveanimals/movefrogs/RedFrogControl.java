package moveanimals.movefrogs;

import moveanimals.FrogControl;
import main.SnakeGame;

import java.awt.*;


public class RedFrogControl extends FrogControl {
    @Override
    public int getLifeCycle() {
        return 0;
    }

    @Override
    public Color getColor() {
        return new Color(157, 25, 46);
    }

    public RedFrogControl(SnakeGame snakeGame) {
        super(snakeGame);
    }
}
