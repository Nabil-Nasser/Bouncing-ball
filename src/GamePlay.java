import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private boolean paused = false;  // Track if the game is paused
    private int score = 0;
    private int totalBricks = 50;
    private int lives = 5;  // Set initial lives to 5

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballposX = 120, ballposY = 350;
    private int ballXdir = -1, ballYdir = -2;

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(6, 12);
        addKeyListener(this);
        setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
    }


    public void paint(Graphics g) {
        // Set background color
        g.setColor(Color.RED);
        g.fillRect(0, 0, 692, 592);  // Changed the starting position to make room for the info section

        // Draw game info section at the top
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 692, 50);  // Add a black bar for the game info at the top

        // Set the font for the info section
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));

        // Display the score, level, and lives in the dedicated section
        g.drawString("Score: " + score, 20, 30);
        g.drawLine(20, 40, 200, 40);  // Line below score

        g.drawString("Level: 2", 220, 30);
        g.drawLine(220, 40, 400, 40);  // Line below level

        g.drawString("Lives: " + lives, 420, 30);
        g.drawLine(420, 40, 600, 40);  // Line below lives

        // Draw the bricks (adjust position of bricks map so it's below the info section)
        map.draw((Graphics2D) g);

        // Draw the paddle
        g.setColor(Color.GRAY);
        g.fillRect(playerX, 550, 100, 8);

        // Draw the ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballposX, ballposY, 20, 20);

        // If the game is paused, show the "Paused" message
        if (paused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("Paused", 300, 300);
        }

        // End game message if ball goes below screen or all bricks are cleared
        if (lives == 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("Game Over!", 260, 300);
            timer.stop();
        }

        // Display the instruction message at the bottom of the screen
        g.setFont(new Font("serif", Font.BOLD, 18));  // Smaller font for the message
        g.drawString("To start or pause the game, press ESC", 220, 570);

        g.dispose();
    }




    public void actionPerformed(ActionEvent e) {
        if (play && !paused) {
            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0 || ballposX > 670) ballXdir = -ballXdir;
            if (ballposY < 0) ballYdir = -ballYdir;

            // Check if the ball goes below the screen (losing a life)
            if (ballposY > 570) {
                lives--;  // Decrease lives by 1
                if (lives > 0) {
                    // Reset ball and paddle positions for next life
                    ballposX = 120;
                    ballposY = 350;
                    ballXdir = -1;
                    ballYdir = -2;
                    playerX = 310;
                }
            }

            Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
            Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);

            if (ballRect.intersects(paddleRect)) ballYdir = -ballYdir;

            mapCollision(ballRect);
        }
        repaint();
    }

    private void mapCollision(Rectangle ballRect) {
        for (int i = 0; i < map.map.length; i++) {
            for (int j = 0; j < map.map[0].length; j++) {
                if (map.map[i][j] > 0) {
                    int brickX = j * map.brickWidth + 80;
                    int brickY = i * map.brickHeight + 50;
                    Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);

                    if (ballRect.intersects(brickRect)) {
                        map.setBrickValue(0, i, j);
                        totalBricks--;
                        score += 5;
                        ballYdir = -ballYdir;
                    }
                }
            }
        }
    }

    // Handle keyboard input for movement and pausing
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_RIGHT && playerX < 600) {
            playerX += 20;
        }
        if (keyCode == KeyEvent.VK_LEFT && playerX > 10) {
            playerX -= 20;
        }

        // If the Esc key is pressed, toggle the pause state
        if (keyCode == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            if (!paused) {
                play = true;  // Resume the game when unpaused
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}


