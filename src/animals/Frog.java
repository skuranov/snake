package animals;


import base.Direction;
import base.SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Sergei_Kuranov on 10/7/2015.
 */
public abstract class Frog extends Animal {
    protected int frogBodyX;
    protected int frogBodyY;
    protected Direction direction;
    private static Random random;
    protected int cycleCounter;
    private boolean isCancelled;
    protected JFrame frame;

    abstract protected int getLifeCycle();

    abstract public Color getColor();

    public int getX(){
        return frogBodyX;
    }

    public int getY(){
        return frogBodyY;
    }


    public void incCycleCount(){
        cycleCounter++;
    }

    public int getCycleCount(){
        return cycleCounter;
    }


    public Frog(SnakeGame snakeGame) {
        super(snakeGame);
        frame = snakeGame.getFrame();
        direction = new Direction();
        cycleCounter = 0;
        frogBodyX = getRandom().nextInt(snakeGame.getBaseParams().get("width") - 1) + 1;
        frogBodyY = getRandom().nextInt(snakeGame.getBaseParams().get("height") - 1) + 1;
        frame.repaint(frogBodyX * 32, (frogBodyY * 32) + 60, 32, 32);//Repaint frog after creating
    }

    @Override
    public void move() {
        int frogBodyXPast = frogBodyX;
        int frogBodyYPast = frogBodyY;
        int changeDIrection = getRandom().nextInt(10);
        if (changeDIrection <= 7 ){//Escaping from snake's head with 0.8 chance
            int distanceToSnakeHeadX = snakeGame.getSnake().snakeBodyX.get(0) - frogBodyX;
            int distanceToSnakeHeadY = snakeGame.getSnake().snakeBodyY.get(0) - frogBodyY;
            if (Math.abs(distanceToSnakeHeadX) > Math.abs(distanceToSnakeHeadY)){
                if (distanceToSnakeHeadX > 0){
                    direction.setLeft();
                }
                else {
                    direction.setRight();
                }
            }
            else{
                if (distanceToSnakeHeadX < 0){
                    direction.setUp();
                }
                else {
                    direction.setDown();
                }
            }
        }
        else{changeDIrection = getRandom().nextInt(3);//Random move in other cases
            if (changeDIrection == 0){
                direction.toLeft();}
            else if (changeDIrection == 1){
                direction.toRight();
            }
        }


        frogBodyX += direction.getCurDir()[0];
        if (frogBodyX < 0){
            frogBodyX = snakeGame.getBaseParams().get("width") - 1;
        }
        else if(frogBodyX > snakeGame.getBaseParams().get("width") - 1){
            frogBodyX = 0;
        }
        frogBodyY += direction.getCurDir()[1];
        if (frogBodyY < 0){
            frogBodyY = snakeGame.getBaseParams().get("height") - 1;
        }
        else if(frogBodyY > snakeGame.getBaseParams().get("height") - 1){
            frogBodyY = 0;
        }
        frame.repaint(frogBodyXPast * 32, (frogBodyYPast * 32) + 60, 32, 32);
        frame.repaint(frogBodyX*32,(frogBodyY*32)+60, 32, 32);//Repaint frog after moving
    }

    @Override
    public Object call(){
        while (true){
            try {
                Thread.sleep(5000/snakeGame.getBaseParams().get("gameSpeed"));
                if (!snakeGame.getFlWait()){
                    if(isCancelled) {
                        break;}
                    move();}
            } catch (InterruptedException e) {
                break;
            }
        }
        return null;
    }

    public Random getRandom(){
        if (random == null){
            return new Random();
        }
        else {return random;}
    }

    public void cancel(){isCancelled=true;}
}
