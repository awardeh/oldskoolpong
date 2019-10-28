import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class Pong extends JFrame {

    public Pong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Game b = new Game();
        getContentPane().add(b);
        b.setFocusable(true);
        add(b);
        System.out.println(b.getHeight());
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Pong p = new Pong();
        p.setResizable(false);
        p.setVisible(true);
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setSize(700, 500);
        p.setLocationRelativeTo(null);
    }
}
