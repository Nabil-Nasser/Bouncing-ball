package BouncingBall;

import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class BouncingBall extends JFrame {
    private GLCanvas glCanvas;
    static FPSAnimator animator = null;
    private BouncingBallEventListener listener = new BouncingBallEventListener();

    public static void main(String[] args) {
        new BouncingBall();
        animator.start();
    }

    public BouncingBall() {
        super("BounceIT");
        glCanvas = new GLCanvas();
        glCanvas.addGLEventListener(listener);
        glCanvas.addKeyListener(listener);
        getContentPane().add(glCanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(glCanvas, 60);
        setSize(600, 600);
        setLocationRelativeTo(this);
        setVisible(true);
        setFocusable(true);
        glCanvas.requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
