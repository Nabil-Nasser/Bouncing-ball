package BouncingBall;

public class HighScore {
    String userName;
    int score;

    public HighScore(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    @Override
    public String toString() {
        return userName + " " + score; // Save as "Name Score"
    }

    public static HighScore fromString(String line) {
        String[] parts = line.split(" ");
        return new HighScore(parts[0], Integer.parseInt(parts[1]));
    }
}
