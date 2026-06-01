/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comp603.dao;

import comp603.model.SessionRecord;
import java.util.List;

/**
 *
 * @author willpurdon
 */
public interface ISessionDAO {
    int createSession(int playerId);
    void completeSession(int sessionId, int healthRemaining, int deathCount, String endingChosen);
    void incrementDeaths(int sessionId);
    int getBestHealth(int playerId);
    List<SessionRecord> getLeaderboard();
}
