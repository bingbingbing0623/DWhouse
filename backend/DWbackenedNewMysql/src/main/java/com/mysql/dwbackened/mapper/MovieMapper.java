package com.mysql.dwbackened.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author wyx20
 * @version 1.0
 * @title MovieMapper
 * @description
 * @create 2023/12/25 9:54
 */
@Mapper
public interface MovieMapper extends BaseMapper<Movie> {

    @Select("SELECT ASIN FROM movies " +
            "WHERE (#{min_score} IS NULL OR #{min_score} = 0.0 OR average_rating >= #{min_score}) " +
            "AND (#{max_score} IS NULL OR #{max_score} = 0.0 OR average_rating <= #{max_score}) " +
            "AND (#{title} IS NULL OR #{title} = '' OR LOCATE(#{title}, title) > 0)")
    Set<String> selectByScoreAndTitle(MovieSearchDto movieSearchDto);

    @Select({
            "<script>",
            "SELECT ASIN AS movieId, title AS movieTitle, average_rating AS movieScore,",
            "comment_count AS commentNum, release_date AS releaseDate, genres AS genre",
            "FROM movies",
            "WHERE ASIN IN",
            "<foreach item='asin' collection='movieSet' open='(' separator=',' close=')'>",
            "#{asin}",
            "</foreach>",

            "</script>"
    })
    List<Movie> getMovieInfo(Set<String> movieSet);





}
