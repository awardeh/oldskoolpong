import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {

    private int x = 350;
    private int y = 250;
    int xa = 1;
    int ya = 1;
    private int xPaddle1, xPaddle2, yPaddle1, yPaddle2;
    private Rectangle rPaddle1, rPaddle2, rBall;
    Timer t;
    private Scorecard score;
    int paddleSpeed;
    private boolean[] keyArray = new boolean[5];
    private boolean gameOver = false;

    Game() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        GameSpeed gs = new GameSpeed(this);
        gs.setVisible(true);
        int delay = 15;
        t = new Timer(delay, this);
        t.start();
        xPaddle1 = 20;
        yPaddle1 = 250;
        xPaddle2 = 670;
        yPaddle2 = 250;
        addKeyListener(this);
        score = new Scorecard();
        paddleSpeed = 5;
        backgroundSong();
    }


    private void drawPaddle() {
        rPaddle1 = new Rectangle(xPaddle1, yPaddle1, 10, 30);
        rPaddle2 = new Rectangle(xPaddle2, yPaddle2, 10, 30);
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


    private boolean checkP1() {
        if (yPaddle1 >= getHeight() - 20) {
            yPaddle1 = getHeight() - 20;
            return false;
        } else if (yPaddle1 <= -10) {
            yPaddle1 = 40;
            return false;
        } else return true;
    }

    private boolean checkP2() {
        if (yPaddle2 >= getHeight()) {
            yPaddle2 = getHeight() - 20;
            return false;
        } else if (yPaddle2 <= -10) {
            yPaddle2 = 40;
            return false;
        } else return true;
    }


    private void restartGame() {
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

        if (x >= getWidth() - 10) {
            x = 350;
            y = 250;
            xa = -xa;
            score.increaseScore('1');
        }
        if (x <= 10) {
            x = 350;
            y = 250;
            xa = -xa;
            score.increaseScore('2');
        }

        if (y >= getHeight() - 50 || y <= 50) {
            ya = -ya;
            try {
                playSound();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }

        }
        if (rBall.intersects(rPaddle1) || rBall.intersects(rPaddle2)) {
            xa = -ya;
            ya = -ya;
            try {
                playSound();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
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

    private void playSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        Clip clip;
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sound.wav").getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

    }
    private void backgroundSong() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("nokia.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
