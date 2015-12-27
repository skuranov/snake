package control.moveanimals.movesnake;


 import bodies.Bodie;
 import control.moveanimals.AnimalControl;
 import control.moveanimals.FrogControl;
 import control.moveanimals.movefrogs.BlueFrogControl;
 import control.moveanimals.movefrogs.GreenFrogControl;
 import control.moveanimals.movefrogs.RedFrogControl;
 import control.direction.Direction;
 import control.SnakeGame;
 import javax.swing.*;
 import java.awt.event.InputEvent;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.util.ArrayList;

public class SnakeControl extends AnimalControl implements MouseListener {
    int snakeLenght;
    public Direction direction;
    private JFrame frame;
    private boolean flInc;
    public SnakeControl(SnakeGame snakeGame) {
        super(snakeGame);
        bodie = new Bodie();
        direction = new Direction();
        this.snakeLenght = snakeGame.getBaseParams().get("snakeLenth");
        this.frame = snakeGame.getDraw().getFrame();
        for(int i = 0; i < snakeGame.getBaseParams().get("snakeLenth"); i++){
            bodie.getCoordX().add(0);
            bodie.getCoordY().add(snakeGame.getBaseParams().get("snakeLenth") - i - 1);
        }
        frame.repaint(); //Repainting all canvas at newgame.
    }

     public int getSnakeLenght() {
         return snakeLenght;
     }

     @Override
    public Object call() {
        while (true) {
            try {
                Thread.sleep(1000 / snakeGame.getBaseParams().get("gameSpeed"));
                if (!snakeGame.getFlWait()){
                    move();}
            } catch (InterruptedException e) {
                break;
            }
        }
        return null;
    }

     public Bodie getBodie() {
         return bodie;
     }

     @Override
    public void move() {
        ArrayList<Integer> snakeBodyX = bodie.getCoordX();
        ArrayList<Integer> snakeBodyY = bodie.getCoordY();
        if (flInc){
            snakeLenght++;
            bodie.getCoordX().add(1);
            bodie.getCoordY().add(1);
            flInc = false;
        }
        int[] afterTailCell = {snakeBodyX.get(snakeLenght-1),bodie.getCoordY().get(snakeLenght-1)}; // Put last cell
        // into memory before move
        for (int i = snakeLenght-1; i >= 0; i--) {
            if (i >= 1){//Moving snake's body
                snakeBodyX.set(i, snakeBodyX.get(i - 1));
                snakeBodyY.set(i, snakeBodyY.get(i - 1));
                if ((i == snakeLenght - 1)||(i==1)){
                    repaintCell(i); // Repainting cell after head and tail
                }
            }
            else {
                snakeBodyX.set(i, snakeBodyX.get(i + 1) + direction.getCurDir()[0]);//Moving snake's head
                snakeBodyY.set(i,snakeBodyY.get(i + 1) + direction.getCurDir()[1]);
                if (snakeBodyX.get(0)<0){
                    snakeBodyX.set(0,snakeGame.getBaseParams().get("width")-1);
                } //"Transparent walls"
                if (snakeBodyY.get(0)<0){
                    snakeBodyY.set(0,snakeGame.getBaseParams().get("height")-1);
                }
                if (snakeBodyX.get(0)>snakeGame.getBaseParams().get("width")-1){
                    snakeBodyX.set(0,0);
                }

                if (snakeBodyY.get(0)>snakeGame.getBaseParams().get("height")-1){
                    snakeBodyY.set(0,0);
                }

                exitLabel: for (FrogControl frogControl : snakeGame.getFrogControls()) {//Eating movefrogs by snake

                    if (frogControl instanceof BlueFrogControl) {
                        frogControl.incCycleCount();
                        if (frogControl.getCycleCount() > frogControl.getLifeCycle()) {
                            int tempX = frogControl.getBodie().getCoordX().get(0);
                            int tempY = frogControl.getBodie().getCoordY().get(0);
                            frogControl.cancel();
                            snakeGame.getFrogControls().remove(frogControl);
                            frame.repaint(tempX * 32, (tempY * 32) + 60, 32, 32);
                        }
                    }
                    if ((snakeBodyX.get(i) == frogControl.getBodie().getCoordX().get(0)) && (snakeBodyY.get(i) ==
                            frogControl.getBodie().getCoordY().get(0))) {
                        if (frogControl instanceof GreenFrogControl) {
                            flInc = true;
                            snakeGame.getDraw().getScoreLabel().setText("  " +
                                    (Integer.parseInt((snakeGame.getDraw().getScoreLabel().getText()).trim()) + 1));
                        } else if (frogControl instanceof RedFrogControl) {
                            if (snakeLenght > snakeGame.getBaseParams().get("snakeLenth")) {
                                snakeLenght--;
                            }
                            snakeGame.getDraw().getScoreLabel().setText("  " +
                                    (Integer.parseInt((snakeGame.getDraw().getScoreLabel().getText()).trim()) + 2));
                        } else if (frogControl instanceof BlueFrogControl) {
                            snakeGame.stopGame();
                            break exitLabel;
                        }
                        frogControl.cancel();
                        snakeGame.getFrogControls().remove(frogControl);
                    }
                }

                while (snakeGame.getFrogControls().size() < snakeGame.getBaseParams().get("frogCount")) {//FrogControl respawning
                    FrogControl tempFrogControl = snakeGame.getNewFrog();
                    snakeGame.getFrogControls().add(tempFrogControl);
                    snakeGame.getExecutor().submit(tempFrogControl);
                }
                repaintCell(i); // Repainting head

                for(int j = 1; j < snakeLenght; j++) {//Exit by eating itself
                    if ((snakeBodyX.get(j) == snakeBodyX.get(0)) && (snakeBodyY.get(j) == snakeBodyY.get(0))){
                        snakeGame.stopGame();
                        break;
                    }
                }
            }
        }

        frame.repaint(afterTailCell[0] * 32, (afterTailCell[1] * 32) + 60, 32, 32);//Repaint after tail
    }



     public void repaintCell(int i){
        frame.repaint(bodie.getCoordX().get(i)*32,
                (bodie.getCoordY().get(i) * 32) + 60, 32, 32);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if((e.getModifiers()& InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK){
            direction.toLeft();
        }
        if((e.getModifiers()&InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK){
            direction.toRight();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
