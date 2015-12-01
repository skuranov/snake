package main;

import moveanimals.FrogControl;
import moveanimals.movesnake.SnakeControl;
import moveanimals.movefrogs.BlueFrogControl;
import moveanimals.movefrogs.GreenFrogControl;
import moveanimals.movefrogs.RedFrogControl;
import drawing.Draw;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnakeGame {
    private Draw draw;
    private Random random;
    private static SnakeGame snakeGame;
    private HashMap<String,Integer> baseParams;

    public Draw getDraw() {
        return draw;
    }

    private boolean flWait;
    private SnakeControl snakeControl;
    private CopyOnWriteArraySet<FrogControl> frogControls;
    private ExecutorService executor;

    public boolean getFlWait(){return flWait;
    }

    public void setBaseParams(String width,
                              String height,
                              String snakeLenth,
                              String frogCount,
                              String gameSpeed) {
        baseParams = new HashMap<>();
        baseParams.put("width",Integer.parseInt(width));
        baseParams.put("height",Integer.parseInt(height));
        baseParams.put("snakeLenth",Integer.parseInt(snakeLenth));
        baseParams.put("frogCount",Integer.parseInt(frogCount));
        baseParams.put("gameSpeed",Integer.parseInt(gameSpeed));
    }

    public HashMap<String,Integer> getBaseParams(){
        return baseParams;
    }

    public SnakeControl getSnakeControl(){
        return snakeControl;
    }

    public CopyOnWriteArraySet<FrogControl> getFrogControls(){return frogControls;
    }

    public ExecutorService getExecutor(){return executor;
    }

    public static void main(String[] args) {
        snakeGame = new SnakeGame();
        snakeGame.setBaseParams(args[0], args[1], args[2], args[3], args[4]);
        snakeGame.draw = new Draw(snakeGame);
    }

    public void startGame(){
        if(snakeGame.flWait){
            snakeGame.flWait = false;
        }
        else{
            snakeGame.flWait = false;
            draw.resetScore();
            snakeGame.snakeControl = new SnakeControl(snakeGame);
            draw.getFrame().addMouseListener(snakeGame.snakeControl);
            snakeGame.executor = Executors.newFixedThreadPool(snakeGame.baseParams.get("frogCount") + 1);
            snakeGame.executor.submit(snakeGame.snakeControl);
            snakeGame.frogControls = new CopyOnWriteArraySet<>();
            for (int i = 0; i < snakeGame.baseParams.get("frogCount"); i++) {
                FrogControl tempFrogControl = getNewFrog();
                snakeGame.frogControls.add(tempFrogControl);
                snakeGame.executor.submit(tempFrogControl);
            }
        }
    }

    public void stopGame(){
        if(snakeGame.flWait){
            snakeGame.flWait = false;
        }
        else{
            snakeGame.executor.shutdownNow();
        }
        Draw.stopButton.setEnabled(false);
        Draw.startButton.setEnabled(true);
        Draw.pauseButton.setEnabled(false);
    }

    public void pauseGame() throws InterruptedException {
        flWait = true;
        Draw.stopButton.setEnabled(true);
        Draw.startButton.setEnabled(true);
        Draw.pauseButton.setEnabled(false);
    }


    public FrogControl getNewFrog(){
        FrogControl frogControl = null;
        int changeType = getRandom().nextInt(10);
        if (changeType < 6){
            frogControl = new GreenFrogControl(snakeGame);
        }
        else if ((changeType >= 6)&&(changeType < 9)){
            frogControl = new RedFrogControl(snakeGame);
        }
        else if(changeType >= 9){
            frogControl = new BlueFrogControl(snakeGame);
        }
        return frogControl;
    }

    public Random getRandom(){
        if (random == null){
            random = new Random();
        }
        return random;
    }

}
