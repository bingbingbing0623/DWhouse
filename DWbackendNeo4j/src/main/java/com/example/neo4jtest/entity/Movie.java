package com.example.neo4jtest.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Movie {
    @Id
    private String movieId;//数据库中ASIN
    private String movieTitle;//数据库中title
    private String movieGenre;//数据库中Genres
    private Integer movieReviewNum;//数据库中comment_count
    private Float movieRating;//数据库中average_rating
    private String movieReleaseDate;//数据库中Release date

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
