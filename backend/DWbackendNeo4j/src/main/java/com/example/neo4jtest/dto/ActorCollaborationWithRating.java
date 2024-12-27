package com.example.neo4jtest.dto;

public class ActorCollaborationWithRating {

    private String actorName1;
    private String actorName2;
    private int collaborationCount;
    private double avgRating;

    // 公开的无参构造函数
    public ActorCollaborationWithRating() {
    }

    // 带参数的构造函数
    public ActorCollaborationWithRating(String actorName1, String actorName2, int collaborationCount, double avgRating) {
        this.actorName1 = actorName1;
        this.actorName2 = actorName2;
        this.collaborationCount = collaborationCount;
        this.avgRating = avgRating;
    }

    // Getter 和 Setter 方法
    public String getActorName1() {
        return actorName1;
    }

    public void setActorName1(String actorName1) {
        this.actorName1 = actorName1;
    }

    public String getActorName2() {
        return actorName2;
    }

    public void setActorName2(String actorName2) {
        this.actorName2 = actorName2;
    }

    public int getCollaborationCount() {
        return collaborationCount;
    }

    public void setCollaborationCount(int collaborationCount) {
        this.collaborationCount = collaborationCount;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
}
