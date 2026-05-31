/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603.model;

import java.sql.Timestamp;

/**
 *
 * @author willpurdon
 */
public class SessionRecord {
    public int id;
    public String playerName;
    public int healthRemaining;
    public int deathCount;
    public String endingChosen;
    public Timestamp startTime;
    public Timestamp endTime;
    public int score;

    public SessionRecord(int id, String playerName, int healthRemaining,
                         int deathCount, String endingChosen, Timestamp startTime, Timestamp endTime, int score) {
        this.id = id;
        this.playerName = playerName;
        this.healthRemaining = healthRemaining;
        this.deathCount = deathCount;
        this.endingChosen = endingChosen;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
    }
}
