package com.mysql.dwbackened.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author haookok
 * @version 1.0
 * @title movie
 * @description
 * @create 2023/12/25 8:31
 */


@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @Column(name = "ASIN")
    private String movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "genres")
    private String genres;

    @Column(name = "comment_count")
    private String commentCount;

    @Column(name = "average_rating")
    private String averageRating;


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }
}
