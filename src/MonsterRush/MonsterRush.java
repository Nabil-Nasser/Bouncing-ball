package MonsterRush;


import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonsterRush extends JFrame{
    private GLCanvas glCanvas;
    static FPSAnimator animator = null;
    private MonsterRushEventListener listener = new MonsterRushEventListener();
    public static void main(String[] args) {
        new MonsterRush();
    }
    public MonsterRush(){
        super("Monster Rush");
        glCanvas = new GLCanvas();
        glCanvas.addGLEventListener(listener);
        glCanvas.addKeyListener(listener);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(glCanvas, BorderLayout.CENTER);

        JButton startButton = new JButton("START");
        getContentPane().add(startButton, BorderLayout.SOUTH);//button at bottom

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // here we gonna start the game
                listener.setGameRunning(true);
                animator.start();

                // what if we want to remove the button after starting
                getContentPane().remove(startButton);
                revalidate();
                repaint();

                glCanvas.requestFocus();
            }
        });

        animator = new FPSAnimator(glCanvas, 144);
        setSize(700, 700);
        setLocationRelativeTo(this);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listener.setGameRunning(false);
    }
}
