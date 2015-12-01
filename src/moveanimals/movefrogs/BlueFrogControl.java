package moveanimals.movefrogs;

import moveanimals.FrogControl;
import main.SnakeGame;

import java.awt.*;


public class BlueFrogControl extends FrogControl {
    @Override
    public int getLifeCycle() {
        return 100;
    }

    @Override
    public Color getColor() {
        return new Color(78, 147, 202);
    }

    public BlueFrogControl(SnakeGame snakeGame) {
        super(snakeGame);
    }
}
