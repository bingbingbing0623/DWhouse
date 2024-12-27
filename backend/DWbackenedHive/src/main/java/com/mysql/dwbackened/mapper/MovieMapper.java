package com.mysql.dwbackened.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {

    @Select("select movieId from movie where movieTitle LIKE CONCAT('%', #{title}, '%')")
    Set<String> selectByTitle(MovieSearchDto movieSearchDto);

    @Select({
            "SELECT movieId, movieTitle, movieRating, movieReviewNum FROM movie WHERE movieId = #{movieId}",
            "LIMIT #{start}, #{pageNum}",
    })
    Movie getMovieInfo(String movieId,int start,int pageNum);

    @Select("select movieId from movie where movieRating>=#{min_score} and movieRating<=#{max_score}")
    Set<String> selectByScore(MovieSearchDto movieSearchDto);

}
