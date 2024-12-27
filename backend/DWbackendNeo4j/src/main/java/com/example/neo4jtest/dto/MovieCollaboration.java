package com.example.neo4jtest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieCollaboration {
    private String movieId;
    private Float movieRating;
    private int movieReviewNum;
    private String movieGenre;
    private String movieTitle;
    @Getter
    private String movieDate;
    private String movieLanguage;
    private String movieAge;
    private String movieRunTime;
    private String movieMediaFormat;
    private String movieStudio;
    private String movieEdition;

}

