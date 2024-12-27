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

    @Select("SELECT DISTINCT(direct.movieId) FROM director join direct on direct.directorId=director.id  where name LIKE CONCAT('%', #{director}, '%')")
    Set<String> selectByName(MovieSearchDto movieSearchDto);
//
    @Select({
            "<script>",
            "SELECT movieId FROM direct",
            "WHERE directorId IN",
            "<foreach item='directorId' collection='directorIdList' open='(' separator=',' close=')'>",
            "#{directorId}",
            "</foreach>",
            "</script>"
    })
    Set<String> selectMovieByDirectorId(Set<Integer> directorIdList);

    @Select("select DISTINCT(name) from director where name LIKE CONCAT('%', #{director}, '%') LIMIT #{amount}")
    List<String> getDirectorRecommend(String director, int amount);


    @Select("select DISTINCT(name) from director join direct on director.id=direct.directorId where direct.movieId=#{movieId}")
    List<String> selectNameByMovieId(String movieId);

    @Select("SELECT" +
            "    directorId," +
            "    actorId," +
            "    COUNT(*) AS COOPERATION_COUNT" +
            " FROM " +
            "    actor_director" +
            " GROUP BY " +
            "    directorId, actorId " +
            "HAVING " +
            "    COUNT(*) > 5")
    List<DirectorActorCountDto> selectActorDirectorCount();


    @Select("SELECT" +
            "    directorId," +
            "    actorId," +
            "    COUNT(*) AS COOPERATION_COUNT" +
            " FROM " +
            "    actor_director" +
            " GROUP BY " +
            "    directorId, actorId " +
            "HAVING " +
            "    COUNT(*) > 5" +
            " ORDER BY " +
            "    COOPERATION_COUNT DESC " +
            " LIMIT #{start},#{perPage}")
    List<DirectorActorCountDto> selectDirectorActorPage(int start, int perPage);

    @Select("select name from director where id=#{directorId} LIMIT 1")
    String selectNameByActorId(int directorId);
}
