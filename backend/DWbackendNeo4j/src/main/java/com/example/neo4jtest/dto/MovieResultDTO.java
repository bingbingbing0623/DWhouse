package com.example.neo4jtest.dto;

import com.example.neo4jtest.entity.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class MovieResultDTO {
    private List<MovieCollaboration> movieList;
    private double time; // 时间单位为秒

    // Constructor, Getters, and Setters
    public MovieResultDTO(List<MovieCollaboration> movieList, double executionTime) {
        this.movieList =movieList;
        this.time = executionTime;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public List<MovieCollaboration> getMovieList() {
        return movieList;
    }
    public void setMovieList(List<MovieCollaboration> movieList) {
        this.movieList = movieList;
    }
}
