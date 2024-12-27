package com.example.neo4jtest.dto;

import java.util.List;

public class Collaboration2ResultDTO {

    private List<ActorDoubleCollaboration> doubleCollaboration;
    private double time; // 时间单位为秒

    // Constructor, Getters, and Setters
    public Collaboration2ResultDTO(List<ActorDoubleCollaboration> doubleCollaboration, double executionTime) {
        this.doubleCollaboration =doubleCollaboration;
        this.time = executionTime;
    }

    public List<ActorDoubleCollaboration> getDoubleCollaboration() {
        return doubleCollaboration;
    }

    public void setDoubleCollaboration(List<ActorDoubleCollaboration> doubleCollaboration) {
        this.doubleCollaboration = doubleCollaboration;
    }


    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Collaboration2ResultDTO() {
    }
}
