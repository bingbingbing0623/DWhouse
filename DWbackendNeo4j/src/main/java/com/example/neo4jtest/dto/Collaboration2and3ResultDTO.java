package com.example.neo4jtest.dto;

import java.util.List;

public class Collaboration2and3ResultDTO {
    private List<ActorDoubleCollaboration> doubleCollaboration;
    private List<ActorTripleCollaboration> tripleCollaboration;
    private double time; // 时间单位为秒

    // Constructor, Getters, and Setters
    public Collaboration2and3ResultDTO(List<ActorDoubleCollaboration> doubleCollaboration, List<ActorTripleCollaboration> tripleCollaboration, double executionTime) {
        this.doubleCollaboration = doubleCollaboration;
        this.tripleCollaboration = tripleCollaboration;
        this.time = executionTime;
    }

    public List<ActorDoubleCollaboration> getDoubleCollaboration() {
        return doubleCollaboration;
    }

    public void setDoubleCollaboration(List<ActorDoubleCollaboration> doubleCollaboration) {
        this.doubleCollaboration = doubleCollaboration;
    }

    public List<ActorTripleCollaboration> getTripleCollaboration() {
        return tripleCollaboration;
    }

    public void setTripleCollaboration(List<ActorTripleCollaboration> tripleCollaboration) {
        this.tripleCollaboration = tripleCollaboration;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Collaboration2and3ResultDTO() {
    }
}
