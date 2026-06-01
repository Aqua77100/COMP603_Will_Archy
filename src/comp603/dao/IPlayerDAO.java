/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comp603.dao;

import comp603.model.PlayerRecord;
import java.util.List;

/**
 *
 * @author willpurdon
 */
public interface IPlayerDAO {
    int createPlayer(String name);
    int findPlayerName(String name);
    List<PlayerRecord> getAllPlayers();
}
