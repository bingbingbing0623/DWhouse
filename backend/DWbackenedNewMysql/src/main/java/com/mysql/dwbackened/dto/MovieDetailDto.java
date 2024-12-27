package com.mysql.dwbackened.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wyx20
 * @version 1.0
 * @title MovieDetailDto
 * @description
 * @create 2023/12/26 16:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailDto {
    private String movieId;
    private String movieTitle;
    private String movieRating;
    private String movieRunTime;
    private String movieDate;
    private String movieMediaFormat;
    private String movieEdition;
    private String movieGenre;
    private String movieReviewNum;
    private String movieLanguage;
    private String movieStudio;
    private String movieAge;


}
