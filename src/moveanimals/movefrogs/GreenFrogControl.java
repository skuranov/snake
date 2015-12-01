package moveanimals.movefrogs;

import moveanimals.FrogControl;
import main.SnakeGame;

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
