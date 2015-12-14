package control.moveanimals;


import bodies.Bodie;
import control.direction.Direction;
import control.SnakeGame;

import javax.swing.*;
import java.awt.*;

public abstract class FrogControl extends AnimalControl {
    protected Direction direction;

    protected int cycleCounter;
    private boolean isCancelled;
    protected JFrame frame;

    public abstract int getLifeCycle();

    abstract public Color getColor();


    public void incCycleCount(){
        cycleCounter++;
    }

    public int getCycleCount(){
        return cycleCounter;
    }

    public Bodie getBodie() {
        return bodie;
    }


    public FrogControl(SnakeGame snakeGame) {
        super(snakeGame);
        bodie = new Bodie();
        frame = snakeGame.getDraw().getFrame();
        direction = new Direction();
        cycleCounter = 0;
        bodie.getCoordX().add(snakeGame.getRandom().nextInt(snakeGame.getBaseParams().get("width") - 1) + 1);
        bodie.getCoordY().add(snakeGame.getRandom().nextInt(snakeGame.getBaseParams().get("height") - 1) + 1);
        frame.repaint(bodie.getCoordX().get(0) * 32, (bodie.getCoordY().get(0) * 32) + 60, 32, 32);
        //Repaint frog after creating
    }

    @Override
    public void move() {
        int frogBodyXPast = bodie.getCoordX().get(0);
        int frogBodyYPast = bodie.getCoordY().get(0);
        int changeDIrection = snakeGame.getRandom().nextInt(10);
        if (changeDIrection <= 7 ){//Escaping from snake's head with 0.8 chance
            int distanceToSnakeHeadX = snakeGame.getSnakeControl().getBodie().getCoordX().get(0) -
                    bodie.getCoordX().get(0);
            int distanceToSnakeHeadY = snakeGame.getSnakeControl().getBodie().getCoordY().get(0) -
                    bodie.getCoordY().get(0);
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
        else{changeDIrection = snakeGame.getRandom().nextInt(3);//Random move in other cases
            if (changeDIrection == 0){
                direction.toLeft();}
            else if (changeDIrection == 1){
                direction.toRight();
            }
        }

        if(bodie.getCoordX().get(0)+direction.getCurDir()[0]>0&&
                bodie.getCoordX().get(0)+direction.getCurDir()[0]<
                        snakeGame.getBaseParams().get("width") - 1) {
            bodie.getCoordX().set(0,bodie.getCoordX().get(0)+direction.getCurDir()[0]);
        }


        if(bodie.getCoordY().get(0)+direction.getCurDir()[1]>0&&
                bodie.getCoordY().get(0)+direction.getCurDir()[1]<
                        snakeGame.getBaseParams().get("height") - 1) {
            bodie.getCoordY().set(0,bodie.getCoordY().get(0)+direction.getCurDir()[1]);
        }


        frame.repaint(frogBodyXPast * 32, (frogBodyYPast * 32) + 60, 32, 32);
        frame.repaint(bodie.getCoordX().get(0)*32,
                (bodie.getCoordY().get(0)*32)+60, 32, 32);//Repaint frog after control
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



    public void cancel(){isCancelled=true;}
}
