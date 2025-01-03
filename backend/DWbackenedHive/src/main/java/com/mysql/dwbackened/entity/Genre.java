package com.mysql.dwbackened.entity;

//import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private Integer genreId;
    private String movieId;
    private String genre;
}
