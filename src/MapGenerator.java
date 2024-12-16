import java.awt.*;

public class MapGenerator {
    public int[][] map;
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int rows, int cols) {
        map = new int[rows][cols];

        // Layout: leave a gap in the center for rows 3 and 4
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i >= 2 && i <= 3 && j >= 5 && j <= 8) {
                    map[i][j] = 0;
                } else {
                    map[i][j] = 1 + (i % 3); // Brick strength
                }
            }
        }

        brickWidth = 540 / cols;
        brickHeight = 150 / rows;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    switch (map[i][j]) {
                        case 1: g.setColor(Color.BLUE); break;
                        case 2: g.setColor(Color.GREEN); break;
                        case 3: g.setColor(Color.YELLOW); break;
                    }
                    int brickX = j * brickWidth + 80;
                    int brickY = i * brickHeight + 50;

                    g.fillRect(brickX, brickY, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(brickX, brickY, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}

