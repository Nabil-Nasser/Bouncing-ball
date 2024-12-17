package BouncingBall;

import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class BouncingBall extends JFrame {
    private GLCanvas glCanvas;
    static FPSAnimator animator = null;
    private BouncingBallEventListener listener;

    public static void main(String[] args) {
        String playerName = JOptionPane.showInputDialog("Enter your name to start the game:");
        if (playerName == null || playerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name is required to start the game!");
            System.exit(0);
        }
        new BouncingBall(playerName);
        animator.start();
    }

    public BouncingBall(String playerName) {
        super("BounceIT");
        listener = new BouncingBallEventListener(playerName);
        glCanvas = new GLCanvas();
        glCanvas.addGLEventListener(listener);
        glCanvas.addKeyListener(listener);
        getContentPane().add(glCanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(glCanvas, 144);
        setSize(600, 600);
        setLocationRelativeTo(this);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
