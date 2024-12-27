package com.mysql.dwbackened.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailDto {
    private String movieId;
    private String movieTitle;
    private String movieRating;
    private String movieGenre;
    private String movieReviewNum;

}
