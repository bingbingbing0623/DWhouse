package com.mysql.dwbackened.mapper;

import com.mysql.dwbackened.dto.ActorRelationCountDto;
import com.mysql.dwbackened.dto.ActorRelationInfoDto;
import com.mysql.dwbackened.dto.ActorTopRelationDto;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Actor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface ActorMapper extends BaseMapper<Actor> {
//    @Select("SELECT act.movie_id " +
//            "FROM act " +
//            "JOIN actor ON act.actor_id = actor.actor_id " +
//            "WHERE actor.actor_name LIKE CONCAT ('%', #{actor}, '%')")
//    Set<String> selectByName(MovieSearchDto movieSearchDto);

//    @Select("SELECT DISTINCT(actor_id) FROM actor where actor_name LIKE CONCAT('%', #{actor}, '%')")
//    Set<Integer> selectByName(MovieSearchDto movieSearchDto);
    @Select("SELECT DISTINCT(act.movieId) FROM actor join act on act.actorId=actor.actorId  where actorName LIKE CONCAT('%', #{actor}, '%')")
    Set<String> selectByName(MovieSearchDto movieSearchDto);


    @Select({
            "<script>",
            "SELECT movieId FROM act",
            "WHERE actor_id IN",
            "<foreach item='actorId' collection='actorIdList' open='(' separator=',' close=')'>",
            "#{actorId}",
            "</foreach>",
            "</script>"
    })
    Set<String> selectMovieByActorId(Set<Integer> actorIdList);

    @Select("select DISTINCT(actorName) from actor where actorName LIKE CONCAT('%', #{actor}, '%') LIMIT #{amount}")
    List<String> getActorRecommend(String actor, int amount);

    @Select("select DISTINCT(actorName) from actor join act on actor.actorId=act.actorId where act.movieId=#{movieId}")
    List<String> selectNameByMovieId(String movieId);

    @Select("SELECT" +
            "    firstPersonId," +
            "    secondPersonId," +
            "    COUNT(*) AS COOPERATION_COUNT" +
            " FROM " +
            "    actor_actor" +
            " GROUP BY " +
            "    firstPersonId, secondPersonId " +
            "HAVING " +
            "    COUNT(*) > 5")
     List<ActorRelationCountDto> selectActorRelationCount();

    @Select("SELECT" +
            "    firstPersonId," +
            "    secondPersonId," +
            "    COUNT(*) AS COOPERATION_COUNT" +
            " FROM " +
            "    actor_actor" +
            " GROUP BY " +
            "    firstPersonId, secondPersonId " +
            "HAVING " +
            "    COUNT(*) > 5" +
            " ORDER BY " +
            "    COOPERATION_COUNT DESC " +
            " LIMIT #{start},#{perPage}")
    List<ActorRelationCountDto> selectActorRelationPage(int start, int perPage);

    @Select("select actorName from actor where actorId=#{personId} LIMIT 1")
    String selectNameByActorId(int personId);

    @Select("SELECT " +
            "    a.firstPersonId AS firstPersonId, " +
            "    a.secondPersonId AS secondPersonId, " +
            "    SUM(m.movieReviewNum) AS reviewCount, " +
            "    COUNT(*) AS COOPERATION_COUNT " +
            "FROM " +
            "    actor_actor a " +
            "    JOIN movie m ON a.movieId = m.movieId " +
            "    JOIN genre g ON a.movieId = g.movieId " +
            "WHERE " +
            "    g.genre LIKE CONCAT('%', #{genre}, '%') " +
            "GROUP BY " +
            "    a.firstPersonId, a.secondPersonId " +
            "ORDER BY " +
            "    reviewCount DESC " +
            "LIMIT 1")
    List<ActorRelationCountDto> getGenreRelationInfo(String genre);

    @Select("SELECT " +
            "    a.firstPersonId AS firstPersonId, " +
            "    a.secondPersonId AS secondPersonId, " +
            "    m.movieRating AS averageRating " +
            "FROM " +
            "    actor_actor a " +
            "    JOIN movie m ON a.movieId = m.movieId " +
            "WHERE " +
            "    m.movieId = (SELECT m.movieId FROM movie m ORDER BY m.movieRating DESC LIMIT 1) " +
            "LIMIT 1")
    List<ActorTopRelationDto> getTopRelationInfo();



}
