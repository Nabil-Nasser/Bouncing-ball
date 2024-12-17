package BouncingBall;

import java.io.*;
import java.util.*;

public class HighScoreSaver {
    private final String filePath = "highscores.txt";
    private List<HighScore> scores = new ArrayList<>();

    public HighScoreSaver() {
        loadScores();
    }

    // Load high scores from the file
    public void loadScores() {
        scores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(HighScore.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("No previous high scores found.");
        }
    }

    // Save scores back to the file
    public void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (HighScore score : scores) {
                writer.write(score.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new score
    public void addScore(String playerName, int score) {
        scores.add(new HighScore(playerName, score));
        scores.sort((a, b) -> Integer.compare(b.score, a.score)); // Sort descending
        if (scores.size() > 5) { // Keep top 5 scores
            scores.remove(scores.size() - 1);
        }
        saveScores();
    }

    public List<HighScore> getScores() {
        return scores;
    }
}

