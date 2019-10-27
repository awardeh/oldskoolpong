import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

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

    public Game() {
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Comic Sans", Font.BOLD, 20));
        g.setColor(Color.red);

        x += xa;
        y += ya;
        drawPaddle();

        if (x >= 690) {
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

        if (y >= getHeight() - 10 || y <= 0) {
            ya = -ya;
            try {
                playSound();
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (rBall.intersects(rPaddle1) || rBall.intersects(rPaddle2)) {
            if (yPaddle1 == y + 6 || yPaddle1 + 24 == y || yPaddle2 == y + 6 || yPaddle2 + 24 == y) {
                //xa = -xa;
                ya = -ya;
            }
            xa = -xa;
            try {
                playSound();
            } catch (IOException ex) {
                System.out.println("nope");
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
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

    private void playSound() throws IOException {

        InputStream in = new FileInputStream("sound.wav");
        AudioStream as = new AudioStream(in);
        AudioPlayer.player.start(as);
    }

    void drawPaddle() {
        rPaddle1 = new Rectangle(xPaddle1, yPaddle1, 5, 30);
        rPaddle2 = new Rectangle(xPaddle2, yPaddle2, 5, 30);
        rBall = new Rectangle(x, y, 10, 10);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (check()) {
                yPaddle2 += paddleSpeed;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (check()) {
                yPaddle2 -= paddleSpeed;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (check()) {
                yPaddle1 -= paddleSpeed;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (check()) {
                yPaddle1 += paddleSpeed;
            }
        }
        if (score.sc1 >= 10 || score.sc2 >= 10) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                t.start();
                score.sc1 = 0;
                score.sc2 = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    boolean check() {
        if (yPaddle1 + 37 >= getHeight()) {
            yPaddle1 = yPaddle1 - 2;
            return false;
        } else if (yPaddle1 <= 5) {
            yPaddle1 = yPaddle1 + 2;
            return false;
        } else if (yPaddle2 + 37 >= getHeight()) {
            yPaddle2 = yPaddle2 - 2;
            return false;
        } else if (yPaddle2 <= 5) {
            yPaddle2 = yPaddle2 + 2;
            return false;
        }
        return true;
    }
}
