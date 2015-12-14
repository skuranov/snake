package drawing;

import control.moveanimals.FrogControl;
import control.SnakeGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Draw extends JPanel{
    public final static int SCALE = 32;
    public final static int BREAKLINE = 1;
    public final static JButton startButton = new JButton("Start");
    public final static JButton stopButton = new JButton("Stop");
    public final static JButton pauseButton = new JButton("Pause");
    private JLabel scoreLabel;
    private JFrame frame;
    private SnakeGame snakeGame;
    public Draw(SnakeGame snakeGame){
        this.snakeGame = snakeGame;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(((snakeGame.getBaseParams().get("width")*SCALE)+5),
                (snakeGame.getBaseParams().get("height")*SCALE)+65);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.add(makeButtons(),BorderLayout.NORTH);
        frame.add(this);
        frame.setVisible(true);
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
                    snakeGame.startGame();
                }
                else if ("StopGame".equals(e.getActionCommand())){
                    startButton.setEnabled(true);
                    snakeGame.stopGame();
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);
                }
                else if ("PauseGame".equals(e.getActionCommand())){
                    startButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    try {
                        snakeGame.pauseGame();
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

    public void paint(Graphics graphics){
        graphics.setColor(new Color(50,50,50));
        graphics.fillRect(0,0,(snakeGame.getBaseParams().get("width") + BREAKLINE) * SCALE,
                ((snakeGame.getBaseParams().get("height") + BREAKLINE) * SCALE));
        graphics.setColor(new Color(10,10,10));
        for (int i = 0; i < (snakeGame.getBaseParams().get("width") * (SCALE + BREAKLINE)) + BREAKLINE; i += SCALE){
            graphics.drawLine(i,0,i,snakeGame.getBaseParams().get("width") * SCALE);
        }
        for (int i = 0; i < (snakeGame.getBaseParams().get("height") * (SCALE+BREAKLINE)) + BREAKLINE; i += SCALE){
            graphics.drawLine(0,i,snakeGame.getBaseParams().get("height") * SCALE,i);
        }
        if(!(snakeGame.getSnakeControl()==null)){
            graphics.setColor(new Color(200, 200, 75));
            for (int j = 0; j < snakeGame.getSnakeControl().getSnakeLenght(); j++) {
                if (j == 0) {
                    graphics.fillOval((snakeGame.getSnakeControl().getBodie().getCoordX().get(j) * SCALE) + 6,
                            (snakeGame.getSnakeControl().getBodie().getCoordY().get(j) * SCALE) +
                                    6, SCALE - 12, SCALE - 12);
                } else if ((j < snakeGame.getSnakeControl().getSnakeLenght() - 1) && (j >= 0)) {
                    graphics.fillOval((snakeGame.getSnakeControl().getBodie().getCoordX().get(j) * SCALE) + 9,
                            (snakeGame.getSnakeControl().getBodie().getCoordY().get(j) * SCALE)
                                    + 9, SCALE - 17, SCALE - 17);
                } else {
                    graphics.fillOval((snakeGame.getSnakeControl().getBodie().getCoordX().get(j) * SCALE) + 11,
                            (snakeGame.getSnakeControl().getBodie().getCoordY().get(j) * SCALE) +
                                    11, SCALE - 21, SCALE - 21);
                }
            }
        }
        if(!(snakeGame.getFrogControls()==null)){
            for (FrogControl frogControl : snakeGame.getFrogControls()){
                graphics.setColor(frogControl.getColor());
                graphics.fillOval((frogControl.getBodie().getCoordX().get(0) * SCALE) + 9,
                        (frogControl.getBodie().getCoordY().get(0) * SCALE) + 9, SCALE - 17, SCALE - 17);
            }

        }
    }

    public JLabel getScoreLabel(){return scoreLabel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void resetScore(){
        scoreLabel.setText("  " + 0);
    }

}
