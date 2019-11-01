import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;
//TO DO
//        make size soft coded
//        make paddles non flickering
//        make it paddles more accurate

public class Game extends JPanel implements ActionListener, KeyListener {
    private final static int MIN_HEIGHT = 450;
    private final static int MAX_WIDTH = 700;
    private final static int MIN_WIDTH = 0;
    private final static int MAX_HEIGHT = 50;
    private final static int PADDLE_HEIGHT = 40;
    private final static String SONG = ".//res/nokia.wav";
    private final static String SOUND = ".//res/sound.wav";


    private int x = 350;
    private int y = 250;
    int xBall = 1;
    int yBall = 1;
    private int xPaddle1, xPaddle2, yPaddle1, yPaddle2;
    private Rectangle rPaddle1, rPaddle2, rBall;
    Timer t;
    private Scorecard score;
    int paddleSpeed;
    private boolean[] keyArray = new boolean[5];
    private boolean gameOver = true;

    Game() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        GameSpeed gs = new GameSpeed(this);
        gs.setVisible(true);
        int delay = 15;
        t = new Timer(delay, this);
        if (!gameOver)
            t.start();
        xPaddle1 = 30;
        yPaddle1 = 250;
        xPaddle2 = 670;
        yPaddle2 = 250;
        addKeyListener(this);
        score = new Scorecard();
        paddleSpeed = 5;
        backgroundSong();
    }


    private void drawPaddle() {
        rPaddle1 = new Rectangle(xPaddle1, yPaddle1, 10, PADDLE_HEIGHT);
        rPaddle2 = new Rectangle(xPaddle2, yPaddle2, 10, PADDLE_HEIGHT);
        rBall = new Rectangle(x, y, 10, 10);
        if (keyArray[0]) {
            if (checkP1D())
                yPaddle1 += paddleSpeed;
        }
        if (keyArray[1]) {
            if (checkP1U())
                yPaddle1 -= paddleSpeed;
        }
        if (keyArray[2]) {
            if (checkP2D())
                yPaddle2 += paddleSpeed;
        }
        if (keyArray[3]) {
            if (checkP2U())
                yPaddle2 -= paddleSpeed;
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
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (checkP1D()) {
                keyArray[0] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (checkP1U()) {
                keyArray[1] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (checkP2D()) {
                keyArray[2] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (checkP2U()) {
                keyArray[3] = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keyArray[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keyArray[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keyArray[2] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keyArray[3] = false;
        }
    }


    private boolean checkP1U() {
        if (yPaddle1 <= MAX_HEIGHT) {
            yPaddle1 = MAX_HEIGHT;
            return false;
        }
        return true;
    }


    private boolean checkP1D() {
        if (yPaddle1 >= MIN_HEIGHT - PADDLE_HEIGHT) {
            yPaddle1 = MIN_HEIGHT - PADDLE_HEIGHT;
            return false;
        }
        return true;
    }

    private boolean checkP2U() {
        if (yPaddle2 <= MAX_HEIGHT) {
            yPaddle2 = MAX_HEIGHT;
            return false;
        }
        return true;
    }

    private boolean checkP2D() {
        if (yPaddle2 >= MIN_HEIGHT - PADDLE_HEIGHT) {
            yPaddle2 = MIN_HEIGHT - PADDLE_HEIGHT;
            return false;
        }
        return true;
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

        x += xBall;
        y += yBall;
        drawPaddle();

        if (x >= MAX_WIDTH - 35) {
            x = 350;
            y = 250;
            xBall = -xBall;
            score.increaseScore('1');
        }
        if (x <= MIN_WIDTH + 35) {
            x = 350;
            y = 250;
            xBall = -xBall;
            score.increaseScore('2');
        }

        if (y >= MIN_HEIGHT - 5 || y <= MAX_HEIGHT + 3) {
            yBall = -yBall;
            try {
                playSound();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }

        }
        if (rBall.intersects(rPaddle1) || rBall.intersects(rPaddle2)) {
            xBall = -xBall;
            yBall = -yBall;
            try {
                playSound();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }

        g.fillOval(x - 5, y, 10, 10);
        g.fillRect(xPaddle1, yPaddle1, 10, PADDLE_HEIGHT);
        g.fillRect(xPaddle2, yPaddle2, 10, PADDLE_HEIGHT);
        g.setColor(Color.CYAN);
        g.drawString(Integer.toString(score.sc1), 50, 30);
        g.drawString(Integer.toString(score.sc2), 650, 30);
        g.setColor(Color.WHITE);
        g.drawLine(MAX_WIDTH / 2, MAX_HEIGHT, MAX_WIDTH / 2, MIN_HEIGHT);
        g.drawLine(MIN_WIDTH, MIN_HEIGHT, MAX_WIDTH, MIN_HEIGHT);
        g.drawLine(MIN_WIDTH, MAX_HEIGHT, MAX_WIDTH, MAX_HEIGHT);


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
        Toolkit.getDefaultToolkit().sync();
    }

    private void playSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(SOUND));
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        clip.setFramePosition(0);
        clip.start();
    }

    private void backgroundSong() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(SONG));
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
