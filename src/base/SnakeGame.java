package base;

import animals.Frog;
import animals.Snake;
import animals.frogs.BlueFrog;
import animals.frogs.GreenFrog;
import animals.frogs.RedFrog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnakeGame extends JPanel {
    public final static int SCALE = 32;
    public final static int BREAKLINE = 1;
    private final static JButton startButton = new JButton("Start");
    private final static JButton stopButton = new JButton("Stop");
    private final static JButton pauseButton = new JButton("Pause");
    private static Random random;
    private static SnakeGame snakeGame;
    private HashMap<String,Integer> baseParams;
    private boolean flWait;
    private JLabel scoreLabel;
    private Snake snake;
    private JFrame frame;
    private CopyOnWriteArraySet<Frog> frogs;
    private ExecutorService executor;

    public boolean getFlWait(){
        return flWait;
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

    public Snake getSnake(){
        return snake;
    }

    public JFrame getFrame(){
        return frame;
    }

    public CopyOnWriteArraySet<Frog> getFrogs(){
        return frogs;
    }

    public JLabel getScoreLabel(){return scoreLabel;
    }

    public ExecutorService getExecutor(){
        return executor;
    }

    public static void main(String[] args) {
        snakeGame = new SnakeGame();
        snakeGame.setBaseParams(args[0],args[1],args[2],args[3],args[4]);
        snakeGame.frame = new JFrame();
        snakeGame.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        snakeGame.frame.setResizable(false);
        snakeGame.frame.setSize((snakeGame.baseParams.get("width")*SCALE)+5,
                ((snakeGame.baseParams.get("height")+BREAKLINE)*SCALE)+30);
        snakeGame.frame.setLocationRelativeTo(null);
        snakeGame.frame.getContentPane().setLayout(new BorderLayout());
        snakeGame.frame.add(snakeGame.makeButtons(),BorderLayout.NORTH);
        snakeGame.frame.add(snakeGame);
        snakeGame.frame.setVisible(true);
    }

    public void paint(Graphics graphics){
        graphics.setColor(new Color(50,50,50));
        graphics.fillRect(0,0,(snakeGame.baseParams.get("width") + BREAKLINE) * SCALE,
                ((snakeGame.baseParams.get("height") + BREAKLINE) * SCALE));
        graphics.setColor(new Color(10,10,10));
        for (int i = 0; i < (snakeGame.baseParams.get("width") * (SCALE + BREAKLINE)) + BREAKLINE; i += SCALE){
            graphics.drawLine(i,0,i,snakeGame.baseParams.get("width") * SCALE);
        }
        for (int i = 0; i < (snakeGame.baseParams.get("height") * (SCALE+BREAKLINE)) + BREAKLINE; i += SCALE){
            graphics.drawLine(0,i,snakeGame.baseParams.get("height") * SCALE,i);
        }
        if(!(snake==null)){
            graphics.setColor(new Color(200, 200, 75));
            for (int j = 0; j < snake.getSnakeLenght(); j++) {
                if (j == 0) {
                    graphics.fillOval((snake.snakeBodyX.get(j) * SCALE) + 6,
                            (snake.snakeBodyY.get(j) * SCALE) + 6, SCALE - 12, SCALE - 12);
                } else if ((j < snake.getSnakeLenght() - 1) && (j >= 0)) {
                    graphics.fillOval((snake.snakeBodyX.get(j) * SCALE) + 9,
                            (snake.snakeBodyY.get(j) * SCALE) + 9, SCALE - 17, SCALE - 17);
                } else {
                    graphics.fillOval((snake.snakeBodyX.get(j) * SCALE) + 11,
                            (snake.snakeBodyY.get(j) * SCALE) + 11, SCALE - 21, SCALE - 21);
                }
            }
        }
        if(!(frogs==null)){

            for (Frog frog: frogs){
                graphics.setColor(frog.getColor());
                graphics.fillOval((frog.getX() * SCALE) + 9,
                        (frog.getY() * SCALE) + 9, SCALE - 17, SCALE - 17);
            }

        }
    }

    public JPanel makeButtons(){
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("StartGame".equals(e.getActionCommand())) {
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    startGame();
                }
                else if ("StopGame".equals(e.getActionCommand())){
                    startButton.setEnabled(true);
                    stopGame();
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);
                }
                else if ("PauseGame".equals(e.getActionCommand())){
                    startButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    try {
                        pauseGame();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        startButton.setActionCommand("StartGame");
        startButton.addActionListener(actionListener);
        stopButton.setActionCommand("StopGame");
        stopButton.addActionListener(actionListener);
        pauseButton.setActionCommand("PauseGame");
        pauseButton.addActionListener(actionListener);
        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(130,130,130));
        scoreLabel = new JLabel("  " + 0);
        jPanel.add(startButton);
        jPanel.add(scoreLabel);
        jPanel.add(pauseButton);
        jPanel.add(stopButton);
        return jPanel;
    }

    public void startGame(){
        if(snakeGame.flWait){
            snakeGame.flWait = false;
        }
        else{
            snakeGame.flWait = false;
            snakeGame.scoreLabel.setText("  " + 0);
            snakeGame.snake = new Snake(snakeGame);
            snakeGame.frame.addMouseListener(snakeGame.snake);
            snakeGame.executor = Executors.newFixedThreadPool(snakeGame.baseParams.get("frogCount") + 1);
            snakeGame.executor.submit(snakeGame.snake);
            snakeGame.frogs = new CopyOnWriteArraySet<>();
            for (int i = 0; i < snakeGame.baseParams.get("frogCount"); i++) {
                Frog tempFrog = getNewFrog();
                snakeGame.frogs.add(tempFrog);
                snakeGame.executor.submit(tempFrog);
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
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }

    public void pauseGame() throws InterruptedException {
        flWait = true;
        stopButton.setEnabled(true);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }


    public Frog getNewFrog(){
        Frog frog = null;
        int changeType = getRandom().nextInt(10);
        if (changeType < 6){
            frog = new GreenFrog(snakeGame);
        }
        else if ((changeType >= 6)&&(changeType < 9)){
            frog = new RedFrog(snakeGame);
        }
        else if(changeType >= 9){
            frog = new BlueFrog(snakeGame);
        }
        return frog;
    }

    public Random getRandom(){
        if (random == null){
            return new Random();
        }
        else {return random;}
    }
/*Все поля должны быть private.
* Убрать непонятные имена переменных и методов
* Сделать толковую синхронизацию
* Нужна более глубокая разветвленность
* Переделать Direction enum.
*/
}
