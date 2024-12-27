package com.mysql.dwbackened.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.dwbackened.dto.DirectorActorCountDto;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Director;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author wyx20
 * @version 1.0
 * @title DirectorMapper
 * @description
 * @create 2023/12/25 19:02
 */
@Mapper
public interface DirectorMapper extends BaseMapper<Director> {

//    @Select("SELECT direct.movie_id " +
//            "FROM direct " +
//            "JOIN director ON direct.director_id = director.director_id " +
//            "WHERE director.director_name LIKE CONCAT('%', #{director}, '%')")
//    Set<String> selectByName(MovieSearchDto movieSearchDto);

//    @Select("SELECT movie_id,director_id from direct")
//    List<Direct>  selectTotalDirect();

//    @Select("SELECT DISTINCT(director_id) FROM director where director_name LIKE CONCAT('%', #{director}, '%')")
//    Set<Integer> selectByName(MovieSearchDto movieSearchDto);

    @Select("SELECT DISTINCT(asin) FROM asin_director_mapping " +
            "JOIN director ON asin_director_mapping.director_id = director.director_id " +
            "WHERE director.director_name LIKE CONCAT('%', #{director}, '%')")
    Set<String> selectByName(MovieSearchDto movieSearchDto);
//
    @Select({
            "<script>",
            "SELECT movie_id FROM direct",
            "WHERE director_id IN",
            "<foreach item='directorId' collection='directorIdList' open='(' separator=',' close=')'>",
            "#{directorId}",
            "</foreach>",
            "</script>"
    })
    Set<String> selectMovieByDirectorId(Set<Integer> directorIdList);



    @Select("select DISTINCT(director_name) from director join direct on director.director_id=direct.director_id where direct.movie_id=#{movieId}")
    List<String> selectNameByMovieId(String movieId);

    @Select("SELECT" +
            "    DIRECTOR_ID," +
            "    ACTOR_ID," +
            "    COUNT(*) AS COOPERATION_COUNT" +
            " FROM " +
            "    director_actor" +
            " GROUP BY " +
            "    DIRECTOR_ID, ACTOR_ID " +
            "HAVING " +
            "    COUNT(*) > 5")
    List<DirectorActorCountDto> selectActorDirectorCount();


    @Select("SELECT " +
            "    adm.director_id AS DIRECTOR_ID, " +
            "    aam.actor_id AS ACTOR_ID, " +
            "    COUNT(*) AS COOPERATION_COUNT " +
            "FROM " +
            "    asin_director_mapping adm " +
            "JOIN " +
            "    asin_actor_mapping aam " +
            "ON " +
            "    adm.ASIN = aam.ASIN " +
            "GROUP BY " +
            "    adm.director_id, aam.actor_id " +
            "HAVING " +
            "    COUNT(*) > 5 " +
            "ORDER BY " +
            "    COOPERATION_COUNT DESC " +
            "LIMIT 200;")
    List<DirectorActorCountDto> selectDirectorActorPage();

    @Select("select director_name from director where director_id=#{directorId} LIMIT 1")
    String selectNameByActorId(int directorId);
}
