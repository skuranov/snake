package control.moveanimals;

import bodies.Bodie;
import control.SnakeGame;
import java.util.concurrent.Callable;

public abstract class AnimalControl implements Callable{
    protected SnakeGame snakeGame;
    protected Bodie bodie;

    public AnimalControl(SnakeGame snakeGame){
        this.snakeGame = snakeGame;
    }

    public abstract void move();

}