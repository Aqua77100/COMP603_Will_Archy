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
public class PlayerRecord {
    public int id;
    public String name;
    public Timestamp createdDate;
    
    public PlayerRecord(int id, String name, Timestamp createdDate){
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }
}
