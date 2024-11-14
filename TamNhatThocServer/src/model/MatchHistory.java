package model;

import java.io.Serializable;

public class MatchHistory implements Serializable {
    private int matchId;
    private int numSeed;
    private int numType;
    private int timeMatch;
    private String datePlay;
    private String roomId;
    private int timeWin;

    public MatchHistory(int matchId, int numSeed, int numType, int timeMatch, String datePlay, String roomId, int timeWin) {
        this.matchId = matchId;
        this.numSeed = numSeed;
        this.numType = numType;
        this.timeMatch = timeMatch;
        this.datePlay = datePlay;
        this.roomId = roomId;
        this.timeWin = timeWin;
    }

    // Getter và setter cho các thuộc tính

    public int getMatchId() {
        return matchId;
    }

    public int getNumSeed() {
        return numSeed;
    }

    public int getNumType() {
        return numType;
    }

    public int getTimeMatch() {
        return timeMatch;
    }

    public String getDatePlay() {
        return datePlay;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getTimeWin() {
        return timeWin;
    }

    @Override
    public String toString() {
        return "Match ID: " + matchId + ", Seed: " + numSeed + ", Type: " + numType +
               ", Time Match: " + timeMatch + ", Date Played: " + datePlay +
               ", Room ID: " + roomId + ", Result: " + (timeWin > 0 ? "Win" : timeWin == -1 ? "Loss" : "Draw");
    }
}
