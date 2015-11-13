package animals;

import base.SnakeGame;

import java.util.concurrent.Callable;

/**
 * Created by Sergei_Kuranov on 10/7/2015.
 */
public abstract class Animal implements Callable{
    protected SnakeGame snakeGame;

    public Animal(SnakeGame snakeGame){
        this.snakeGame = snakeGame;
    }

    public abstract void move();
}