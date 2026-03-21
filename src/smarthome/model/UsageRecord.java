/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

/**
 *
 * @author rlack
 */
import java.io.Serializable;
import java.time.LocalDateTime;

public class UsageRecord implements Serializable{
    private LocalDateTime start;
    private LocalDateTime end;

    public UsageRecord(LocalDateTime start) {
        this.start = start;
    }

    public void endRecord(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean isComplete() {
        return start != null && end != null;
    }
}