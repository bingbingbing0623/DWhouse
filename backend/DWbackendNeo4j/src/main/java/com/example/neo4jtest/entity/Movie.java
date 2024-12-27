package com.example.neo4jtest.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node
public class Movie {
    @Id
    @Property("movie_id")
    private String movieId;

    @Property("movie_title")
    private String movieTitle;

    @Property("movie_genre")
    private String movieGenre;

    @Property("movie_review_num")
    private Integer movieReviewNum;

    @Property("movie_average_rating")
    private Float movieRating;

    @Property("movie_release_date")
    private String movieReleaseDate;

    // 构造函数
    public Movie() {}

    public Movie(String movieId, String movieTitle, String movieGenre, Integer movieReviewNum, Float movieRating, String movieReleaseDate   ) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieGenre = movieGenre;
        this.movieReviewNum = movieReviewNum;
        this.movieRating = movieRating;
        this.movieReleaseDate  = movieReleaseDate;
    }

    // Getter和Setter
    public String getMovieId() {return movieId;}

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public Integer getMovieReviewNum() {
        return movieReviewNum;
    }

    public void setMovieReviewNum(Integer movieReviewNum) {
        this.movieReviewNum = movieReviewNum;
    }

    public Float getMovieRating() {return movieRating;}
    public void setMovieRating(Float movieRating) {this.movieRating = movieRating;}

    public String getMovieReleaseDate() {return movieReleaseDate;}
    public void setMovieReleaseDate(String movieReleaseDate) {this.movieReleaseDate = movieReleaseDate;}
}
