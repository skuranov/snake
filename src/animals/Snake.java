package animals;


 import animals.frogs.BlueFrog;
 import animals.frogs.GreenFrog;
 import animals.frogs.RedFrog;
 import base.Direction;
 import base.SnakeGame;

 import javax.swing.*;
 import java.awt.event.InputEvent;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.util.ArrayList;

 /**
 * Created by Sergei_Kuranov on 10/7/2015.
 */
public class Snake extends Animal implements MouseListener {
    int snakeLenght;
    public ArrayList<Integer> snakeBodyX = new ArrayList();
    public ArrayList<Integer> snakeBodyY = new ArrayList();
    public Direction direction;
    private JFrame frame;
    private boolean flInc;
    public Snake(SnakeGame snakeGame) {
        super(snakeGame);
        direction = new Direction();
        this.snakeLenght = snakeGame.getBaseParams().get("snakeLenth");
        this.frame = snakeGame.getFrame();
        for(int i = 0; i < snakeGame.getBaseParams().get("snakeLenth"); i++){
            snakeBodyX.add(0);
            snakeBodyY.add(snakeGame.getBaseParams().get("snakeLenth")-i-1);
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

    @Override
    public void move() {
        if (flInc){
            snakeLenght++;
            snakeBodyX.add(1);
            snakeBodyY.add(1);
            flInc = false;
        }
        int[] afterTailCell = {snakeBodyX.get(snakeLenght-1),snakeBodyY.get(snakeLenght-1)}; // Put last cell
        // into memory before move
        for (int i = snakeLenght-1; i >= 0; i--) {
            if (i >= 1){//Moving snake's body
                snakeBodyX.set(i,snakeBodyX.get(i - 1));
                snakeBodyY.set(i,snakeBodyY.get(i - 1));
                if ((i == snakeLenght - 1)||(i==1)){
                    repaintCell(i); // Repainting cell after head and tail
                }
            }
            else {
                snakeBodyX.set(i,snakeBodyX.get(i + 1) + direction.getCurDir()[0]);//Moving snake's head
                snakeBodyY.set(i,snakeBodyY.get(i + 1) + direction.getCurDir()[1]);
                if (snakeBodyX.get(0)<0){
                    snakeBodyX.set(0,snakeGame.getBaseParams().get("width") - 1);
                } //"Transparent walls"
                if (snakeBodyY.get(0)<0){
                    snakeBodyY.set(0,snakeGame.getBaseParams().get("height") - 1);
                }
                if (snakeBodyX.get(0)>snakeGame.getBaseParams().get("width")-1){
                    snakeBodyX.set(0,0);
                }
                if (snakeBodyY.get(0)>snakeGame.getBaseParams().get("height")-1){
                    snakeBodyY.set(0,0);
                }

                exitLabel: for (Frog frog : snakeGame.getFrogs()) {//Eating frogs by snake

                    if (frog instanceof BlueFrog) {
                        frog.incCycleCount();
                        if (frog.getCycleCount() > frog.getLifeCycle()) {
                            int tempX = frog.getX();
                            int tempY = frog.getY();
                            frog.cancel();
                            snakeGame.getFrogs().remove(frog);
                            frame.repaint(tempX * 32, (tempY * 32) + 60, 32, 32);
                        }
                    }
                    if ((snakeBodyX.get(i) == frog.getX()) && (snakeBodyY.get(i) ==
                            frog.getY())) {
                        if (frog instanceof GreenFrog) {
                            flInc = true;
                            snakeGame.getScoreLabel().setText("  " +
                                    (Integer.parseInt((snakeGame.getScoreLabel().getText()).trim()) + 1));
                        } else if (frog instanceof RedFrog) {
                            if (snakeLenght > snakeGame.getBaseParams().get("snakeLenth")) {
                                snakeLenght--;
                            }
                            snakeGame.getScoreLabel().setText("  " +
                                    (Integer.parseInt((snakeGame.getScoreLabel().getText()).trim()) + 2));
                        } else if (frog instanceof BlueFrog) {
                            snakeGame.stopGame();
                            break exitLabel;
                        }
                        frog.cancel();
                        snakeGame.getFrogs().remove(frog);
                    }
                }

                while (snakeGame.getFrogs().size() < snakeGame.getBaseParams().get("frogCount")) {//Frog respawning
                    Frog tempFrog = snakeGame.getNewFrog();
                    snakeGame.getFrogs().add(tempFrog);
                    snakeGame.getExecutor().submit(tempFrog);
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

    public void repaintCell(int i){frame.repaint(snakeBodyX.get(i)*32, (snakeBodyY.get(i) * 32) + 60, 32, 32);
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
