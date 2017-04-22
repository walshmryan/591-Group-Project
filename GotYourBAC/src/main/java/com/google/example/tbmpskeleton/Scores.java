package com.google.example.tbmpskeleton;

import java.text.DateFormat;
import java.util.Date;

public class Scores {
    private String userId;
    private double score;
    private String gameType;
    private String timestamp;

    public Scores(){};

    public Scores(String userId, double score, String gameType) {
        this.userId = userId;
        this.score = score;
        this.gameType = gameType;
        this.timestamp = DateFormat.getDateTimeInstance().format(new Date());

    };

    public double getScore() {
        return score;
    }

    public String getGameType() {
        return gameType;
    }

    public String getUserId() { return userId; }

    public String getTimestamp() { return timestamp; }

}