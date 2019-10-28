import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.*;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

public class Game extends JPanel implements ActionListener, KeyListener {

    int x = 350;
    int y = 250;
    int xa = 1;
    int ya = 1;
    int xPaddle1, xPaddle2, yPaddle1, yPaddle2;
    Rectangle rPaddle1, rPaddle2, rBall;
    Timer t;
    Scorecard score;
    int paddleSpeed;
    GameSpeed gs;
    int delay = 15;
    boolean[] keyArray = new boolean[5];
    boolean gameOver = false;
    Clip clip;

    public Game() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        gs = new GameSpeed(this);
        gs.setVisible(true);

        t = new Timer(delay, this);
        //t.start();
        xPaddle1 = 20;
        yPaddle1 = 250;
        xPaddle2 = 670;
        yPaddle2 = 250;
        addKeyListener(this);
        score = new Scorecard();
        paddleSpeed = 5;
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("/home/aaa/sound.wav").getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }



    private void drawPaddle() {
        rPaddle1 = new Rectangle(xPaddle1, yPaddle1, 5, 30);
        rPaddle2 = new Rectangle(xPaddle2, yPaddle2, 5, 30);
        rBall = new Rectangle(x, y, 10, 10);
        if (keyArray[0]) {
            if (checkP1()) {
                yPaddle1 += paddleSpeed;
            }
        }
        if (keyArray[1]) {
            if (checkP1()) {
                yPaddle1 -= paddleSpeed;
            }
        }
        if (keyArray[2]) {
            if (checkP2()) {
                yPaddle2 -= paddleSpeed;
            }
        }
        if (keyArray[3]) {
            if (checkP2()) {
                yPaddle2 += paddleSpeed;
            }
        }
        if (score.sc1 >= 10 || score.sc2 >= 10) {
            if (keyArray[4]) {
                t.start();
                score.sc1 = 0;
                score.sc2 = 0;
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (checkP2()) {
                keyArray[0] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (checkP1()) {
                keyArray[1] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (checkP2()) {
                keyArray[2] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (checkP2()) {
                keyArray[3] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keyArray[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keyArray[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keyArray[2] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keyArray[3] = false;
        }
    }


    boolean checkP1() {
        if (yPaddle1 >= getHeight()) {
            yPaddle1 = getHeight() - 40;
            return false;
        } else if (yPaddle1 <= 5) {
            yPaddle1 = 40;
            return false;
        } else return true;
    }

    boolean checkP2() {
        if (yPaddle2 >= getHeight()) {
            yPaddle2 = getHeight() - 40;
            return false;
        } else if (yPaddle2 <= 5) {
            yPaddle2 = 40;
            return false;
        } else return true;
    }


    void restartGame() {
        if (gameOver) {
            gameOver = false;
            t.start();
            score.sc1 = 0;
            score.sc2 = 0;
        }

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Comic Sans", Font.BOLD, 20));
        g.setColor(Color.red);

        x += xa;
        y += ya;
        drawPaddle();

        if (x >= 680) {
            x = 350;
            y = 250;
            xa = -xa;
            score.increaseScore('1');
        }
        if (x <= 0) {
            x = 350;
            y = 250;
            xa = -xa;
            score.increaseScore('2');
        }

        if (y >= getHeight() - 10 || y == -10) {
            ya = -ya;
            playSound();
        }
        if (rBall.intersects(rPaddle1) || rBall.intersects(rPaddle2)) {
            if (yPaddle1 == y + 6 || yPaddle1 + 24 == y || yPaddle2 == y + 6 || yPaddle2 + 24 == y) {
                xa = -xa;
            }
            ya = -ya;
            playSound();
        }

        g.fillOval(x - 5, y, 10, 10);
        g.fillRect(xPaddle1, yPaddle1, 5, 30);
        g.fillRect(xPaddle2, yPaddle2, 5, 30);
        g.setColor(Color.CYAN);
        g.drawString(Integer.toString(score.sc1), 50, 30);
        g.drawString(Integer.toString(score.sc2), 650, 30);
        g.setColor(Color.WHITE);
        g.drawLine(350, 0, 350, 500);
        this.setBackground(Color.BLACK);

        if (score.sc1 >= 10) {
            g.drawString("PLAYER 1 WINS", 80, 120);
            t.stop();
            g.drawString("Press R to restart", 80, 200);
        } else if (score.sc2 >= 10) {
            g.drawString("PLAYER 2 WINS", 500, 120);
            t.stop();
            g.drawString("Press R to restart", 500, 200);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    void playSound() {
        this.clip.start();
    }
}
