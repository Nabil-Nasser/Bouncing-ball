import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        GamePlay gameplay = new GamePlay();

        obj.setBounds(10, 10, 700, 700);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gameplay);
        obj.setVisible(true);
    }
}
