package com.mysql.dwbackened.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author haookok
 * @version 1.0
 * @title DateMapper
 * @description
 * @create 2023/12/25 8:50
 */
@Mapper
@Repository
public interface DateMapper extends BaseMapper<Movie> {

    // 直接查询 movie 表中的 release_date 列
    @Select("SELECT COUNT(*) FROM movies WHERE YEAR(release_date) = #{year}")
    int selectByYear(String year);

    @Select("SELECT m.ASIN " +
            "FROM movies m " +
            "WHERE " +
            "(#{year} IS NULL OR YEAR(m.release_date) = CAST(#{year} AS SIGNED)) " +
            "AND (#{month} IS NULL OR MONTH(m.release_date) = CAST(#{month} AS SIGNED)) " +
            "AND (#{season} IS NULL OR ( " +
            "  (#{season} = 1 AND MONTH(m.release_date) BETWEEN 1 AND 3) OR " +
            "  (#{season} = 2 AND MONTH(m.release_date) BETWEEN 4 AND 6) OR " +
            "  (#{season} = 3 AND MONTH(m.release_date) BETWEEN 7 AND 9) OR " +
            "  (#{season} = 4 AND MONTH(m.release_date) BETWEEN 10 AND 12)" +
            ")) " +
            "AND (#{weekday} IS NULL OR WEEKDAY(m.release_date) = CAST(#{weekday} - 1 AS SIGNED))")
    Set<String> selectByTime(MovieSearchDto movieSearchDto);


    @Select("SELECT release_date FROM movies WHERE movie_id = #{movieId}")
    String selectDateByMovieId(String movieId);
}
