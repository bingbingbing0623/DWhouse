package com.mysql.dwbackened.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Genre;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface GenreMapper extends BaseMapper<Genre> {
    @Select("select movieid from genre where  genre LIKE CONCAT('%', #{genre_name}, '%')")
    Set<String> selectByGenreName(MovieSearchDto movieSearchDto);

    @Select("select DISTINCT(genre) from genre where genre LIKE CONCAT('%', #{genre}, '%') LIMIT #{amount}")
    List<String> getGenreRecommend(String genre, int amount);
}
