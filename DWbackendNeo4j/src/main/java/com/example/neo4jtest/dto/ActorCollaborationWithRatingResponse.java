package com.example.neo4jtest.dto;

import java.util.List;

public class ActorCollaborationWithRatingResponse {
    private List<ActorCollaborationWithRating> ActorCollaborationWithRating;
    private double time;

    public ActorCollaborationWithRatingResponse(List<ActorCollaborationWithRating> ActorCollaborationWithRatings, double time) {
        this.ActorCollaborationWithRating = ActorCollaborationWithRatings;
        this.time = time;
    }

    // Getters and setters
    public List<ActorCollaborationWithRating> getActorCollaborationWithRating() {
        return ActorCollaborationWithRating;
    }

    public void setActorCollaborationWithRating(List<ActorCollaborationWithRating> ActorCollaborationWithRating) {
        this.ActorCollaborationWithRating = ActorCollaborationWithRating;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

}
