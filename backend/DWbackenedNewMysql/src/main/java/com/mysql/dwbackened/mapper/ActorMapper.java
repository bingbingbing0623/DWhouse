package com.mysql.dwbackened.mapper;

import com.mysql.dwbackened.dto.ActorRelationCountDto;
import com.mysql.dwbackened.dto.ActorRelationInfoDto;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Actor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author wyx20
 * @version 1.0
 * @title ActorMapper
 * @description
 * @create 2023/12/25 19:08
 */
@Mapper
public interface ActorMapper extends BaseMapper<Actor> {
//    @Select("SELECT act.movie_id " +
//            "FROM act " +
//            "JOIN actor ON act.actor_id = actor.actor_id " +
//            "WHERE actor.actor_name LIKE CONCAT ('%', #{actor}, '%')")
//    Set<String> selectByName(MovieSearchDto movieSearchDto);

//    @Select("SELECT DISTINCT(actor_id) FROM actor where actor_name LIKE CONCAT('%', #{actor}, '%')")
//    Set<Integer> selectByName(MovieSearchDto movieSearchDto);
@Select("SELECT DISTINCT(asin) FROM asin_actor_mapping " +
        "JOIN actor ON asin_actor_mapping.actor_id = actor.actor_id " +
        "WHERE actor.actor_name LIKE CONCAT('%', #{actor}, '%')")
    Set<String> selectByName(MovieSearchDto movieSearchDto);


    @Select({
            "<script>",
            "SELECT movie_id FROM act",
            "WHERE actor_id IN",
            "<foreach item='actorId' collection='actorIdList' open='(' separator=',' close=')'>",
            "#{actorId}",
            "</foreach>",
            "</script>"
    })
    Set<String> selectMovieByActorId(Set<Integer> actorIdList);



    @Select("select DISTINCT(actor_name) from actor join act on actor.actor_id=act.actor_id where act.movie_id=#{movieId}")
    List<String> selectNameByMovieId(String movieId);

    @Select("SELECT" +
            "    LEFT_PERSON_ID," +
            "    RIGHT_PERSON_ID," +
            "    COUNT(*) AS COOPERATION_COUNT" +
            " FROM " +
            "    actor_actor" +
            " GROUP BY " +
            "    LEFT_PERSON_ID, RIGHT_PERSON_ID " +
            "HAVING " +
            "    COUNT(*) > 5;")
     List<ActorRelationCountDto> selectActorRelationCount();

    @Select("SELECT " +
            "    aam1.actor_id AS leftPersonId, " +
            "    aam2.actor_id AS rightPersonId, " +
            "    COUNT(*) AS cooperationCount " +
            "FROM " +
            "    asin_actor_mapping aam1 " +
            "JOIN " +
            "    asin_actor_mapping aam2 " +
            "ON " +
            "    aam1.ASIN = aam2.ASIN " +
            "    AND aam1.actor_id < aam2.actor_id " +
            "GROUP BY " +
            "    aam1.actor_id, aam2.actor_id " +
            "HAVING " +
            "    COUNT(*) > 5 " +
            "ORDER BY " +
            "    cooperationCount DESC " +
            "LIMIT 200")
    List<ActorRelationCountDto> selectActorRelationPage();

    @Select("select actor_name from actor where actor_id=#{personId} LIMIT 1")
    String selectNameByActorId(int personId);

    @Select("SELECT " +
            "    aam1.actor_id AS LEFT_PERSON_ID, " +
            "    aam2.actor_id AS RIGHT_PERSON_ID, " +
            "    SUM(m.comment_count) AS reviewCount, " +
            "    COUNT(*) AS COOPERATION_COUNT " +
            "FROM " +
            "    asin_actor_mapping aam1 " +
            "    JOIN asin_actor_mapping aam2 ON aam1.ASIN = aam2.ASIN AND aam1.actor_id < aam2.actor_id " +
            "    JOIN movies m ON aam1.ASIN = m.ASIN " +
            "WHERE " +
            "    m.genres LIKE CONCAT('%', #{genre}, '%') " +
            "GROUP BY " +
            "    aam1.actor_id, aam2.actor_id " +
            "ORDER BY " +
            "    reviewCount DESC " +
            "LIMIT 1;")
    List<ActorRelationCountDto> getGenreRelationInfo(String genre);


    @Select("SELECT DISTINCT am.ASIN FROM asin_actor_mapping am JOIN actor a ON am.actor_id = a.actor_id WHERE a.actor_name LIKE CONCAT('%', #{actor}, '%') AND am.is_main_actor = 1")
    Set<String> selectMainMovieByActorName(MovieSearchDto movieSearchDto);
}
