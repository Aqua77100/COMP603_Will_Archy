/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comp603.dao;

/**
 *
 * @author willpurdon
 */
public interface IDeathLogDAO {
    void logDeath(int sessionId, String sceneName);
}
