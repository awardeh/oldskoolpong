public class Pong extends JFrame {
    
    public Pong() {
        Game b = new Game();
        getContentPane().add(b);
        b.setFocusable(true);
        add(b);
    }

    public static void main(String[] args) {
        Pong p = new Pong();
        p.setResizable(false);
        p.setVisible(true);
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setSize(700, 500);
        p.setLocationRelativeTo(null);
    }
}
