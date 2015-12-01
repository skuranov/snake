package moveanimals;

import bodies.Bodie;
import main.SnakeGame;
import java.util.concurrent.Callable;

public abstract class AnimalControl implements Callable{
    protected SnakeGame snakeGame;
    protected Bodie bodie;

    public AnimalControl(SnakeGame snakeGame){
        this.snakeGame = snakeGame;
    }

    public abstract void move();

}