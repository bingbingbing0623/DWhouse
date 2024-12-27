package com.example.neo4jtest.repository;

import com.example.neo4jtest.dto.*;
import com.example.neo4jtest.entity.Actor;
import com.example.neo4jtest.entity.Movie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActorRepository extends Neo4jRepository<Actor, Integer> {
    //1.最常合作的演员
    @Query("MATCH (a1:Actor)-[:ACTED]->(:Movie)<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, COUNT(*) AS collaborationCount " +
            "ORDER BY collaborationCount DESC "+
            "LIMIT 200")
    List<ActorCollaboration> findFrequentCollaborators(int start, int perPage);
    //2.演员合作大于5次的总人数
    @Query("MATCH (a1:Actor)-[:ACTED]->(:Movie)<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) " +
            "WITH a1, a2, COUNT(*) AS collaborationCount " +
            "WHERE collaborationCount > 5 " +
            "RETURN COUNT(*) AS frequentCollaborationCount ")
    Integer findFrequentCollaboratorsNum();
    //3.最常合作的演员和导演
    @Query("MATCH (d:Director)-[:DIRECT]->(:Movie)<-[:ACTED]-(a:Actor) " +
            "RETURN a.actor_name as actorName, d.director_name as directorName, COUNT(*) AS collaborationCount " +
            "ORDER BY collaborationCount DESC "+
            "LIMIT 200")
    List<ActorDirectorCollaboration> findFrequentDirectors(int start, int perPage);

    //4.查找合作演员口碑排名
    @Query("MATCH (a1:Actor)-[:ACTED]->(m:Movie)<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) AND m.movie_average_rating IS NOT NULL " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, COUNT(*) AS collaborationCount, AVG(m.movie_average_rating) AS avgRating " +
            "ORDER BY avgRating DESC, collaborationCount DESC "+
            "LIMIT 200" )
    List<ActorCollaborationWithRating> findActorCollaborationWithHighestRatings(int start, int perPage);
    //5.前10平均总评论数最多2个演员
    @Query("MATCH (a1:Actor)-[:ACTED]->(m:Movie {movie_genre: $genre})<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) AND m.movie_review_num IS NOT NULL " +
            "WITH a1, a2, COUNT(m) AS movieCount, SUM(COALESCE(TOINTEGER(m.movie_review_num), 0)) AS totalReviews, " +
            "     CASE WHEN COUNT(m) > 0 THEN SUM(COALESCE(TOINTEGER(m.movie_review_num), 0)) / COUNT(m) ELSE 0 END AS avgReviews " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, avgReviews " +
            "ORDER BY avgReviews DESC " +
            "LIMIT 200")
    List<ActorDoubleCollaboration> findMostReviewedCollaboration(String genre);

    //7.通过评分找电影信息
    @Query("MATCH (m:Movie) " +
            "WHERE m.movie_average_rating >= 1.03 AND m.movie_average_rating <= 1.13 " +
            "RETURN m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition,"+
            "m.movie_language AS movieLanguage,"+
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre " +
            "ORDER BY movieRating DESC")

    List<MovieCollaboration> findMovieByScore( @Param("minscore")float minscore,  @Param("maxscore")float maxscore);
    @Query("MATCH (m:Movie) " +
            "WHERE m.movie_genre = $genre " +
            "RETURN m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition,"+
            "m.movie_language AS movieLanguage,"+
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre ") // 每页返回的记录数
    List<MovieCollaboration> findMovieByGenre(@Param("genre") String genre, @Param("start") int start, @Param("perPage") int perPage);


    @Query("MATCH (n:Director)" +
            "RETURN n.director_name")
    List<String> findDirectorNames();


    @Query("MATCH (d:Director)-[:DIRECT]->(m:Movie) " +
            "WHERE d.director_name CONTAINS $director " +
            "RETURN m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition,"+
            "m.movie_language AS movieLanguage,"+
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre ")  // 限制每页返回的记录数
    List<MovieCollaboration> findMovieByDirector(String director, Integer page, Integer perPage);

    @Query("MATCH (n:Actor)" +
            "RETURN n.actor_name")
    List<String> findActorNames();

    @Query("MATCH (a:Actor)-[:ACTED]->(m:Movie) " +
            "WHERE a.actor_name CONTAINS $actor " +
            "RETURN m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition,"+
            "m.movie_language AS movieLanguage,"+
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre ")  // 限制每页返回的记录数
    List<MovieCollaboration> findMovieByActor(String actor, Integer page, Integer perPage);

    @Query("MATCH (a:Actor)-[r:ACTED]->(m:Movie) " +
            "WHERE a.actor_name = $actor AND r.is_main_actor = '1' " +
            "RETURN DISTINCT m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition, " +
            "m.movie_language AS movieLanguage, " +
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre ")
    List<MovieCollaboration> findMovieByMainActor(String actor);

    @Query("MATCH (n:Movie)" +
            "RETURN n.movie_title")
    List<String> findMovieNames();

    @Query("MATCH (m:Movie) " +
            "WHERE m.movie_title CONTAINS $title " +
            "RETURN m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition,"+
            "m.movie_language AS movieLanguage,"+
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre ") // 每页返回的记录数
    List<MovieCollaboration> findMovieByTitle(@Param("title") String title, @Param("start") int start, @Param("perPage") int perPage);

    @Query("MATCH (m:Movie) " +
            "WHERE CASE " +
            "    WHEN toInteger(split(m.movie_release_date, '-')[2]) < 30 " +
            "    THEN '20' + split(m.movie_release_date, '-')[2] " +
            "    ELSE '19' + split(m.movie_release_date, '-')[2] " +
            "END = $year " +
            "RETURN m.movie_id AS movieId, " +
            "       toString(m.movie_average_rating) AS movieRating, " +
            "       m.movie_title AS movieTitle, " +
            "       m.movie_release_date AS movieDate, " +
            "       m.movie_rated AS movieAge, " +
            "       m.movie_run_time AS movieRunTime, " +
            "       m.movie_media_format AS movieMediaFormat, " +
            "       m.movie_studio AS movieStudio, " +
            "       m.movie_edition AS movieEdition, " +
            "       m.movie_language AS movieLanguage, " +
            "       COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "       CASE " +
            "           WHEN m.movie_genre IS NULL OR m.movie_genre = '' " +
            "           THEN 'No Genre' " +
            "           ELSE m.movie_genre " +
            "       END AS movieGenre ")
    List<MovieCollaboration> findMovieByYear(@Param("year") String year,
                                               @Param("start") int start,
                                               @Param("perPage") int perPage);

    @Query("MATCH (m:Movie) " +
            "WHERE CASE " +
            "    WHEN toInteger(split(m.movie_release_date, '-')[2]) < 30 " +
            "    THEN '20' + split(m.movie_release_date, '-')[2] " +
            "    ELSE '19' + split(m.movie_release_date, '-')[2] " +
            "END = $year " +
            "AND toLower(split(m.movie_release_date, '-')[1]) = toLower($month) " +
            "RETURN m.movie_id AS movieId, " +
            "       toString(m.movie_average_rating) AS movieRating, " +
            "       m.movie_title AS movieTitle, " +
            "       m.movie_release_date AS movieDate, " +
            "       m.movie_rated AS movieAge, " +
            "       m.movie_run_time AS movieRunTime, " +
            "       m.movie_media_format AS movieMediaFormat, " +
            "       m.movie_studio AS movieStudio, " +
            "       m.movie_edition AS movieEdition, " +
            "       m.movie_language AS movieLanguage, " +
            "       COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "       CASE " +
            "           WHEN m.movie_genre IS NULL OR m.movie_genre = '' " +
            "           THEN 'No Genre' " +
            "           ELSE m.movie_genre " +
            "       END AS movieGenre ")
    List<MovieCollaboration> findMovieByYearAndMonth(@Param("year") String year,
                                                       @Param("month") String month,
                                                       @Param("page") int page,
                                                       @Param("perPage") int perPage);


    @Query("MATCH (m:Movie) " +
            "RETURN m.movie_id AS movieId, " +
            "toString(m.movie_average_rating) AS movieRating, " +
            "m.movie_title AS movieTitle, " +
            "m.movie_release_date AS movieDate, " +
            "m.movie_rated AS movieAge, " +
            "m.movie_run_time AS movieRunTime, " +
            "m.movie_media_format AS movieMediaFormat, " +
            "m.movie_studio AS movieStudio, " +
            "m.movie_edition AS movieEdition,"+
            "m.movie_language AS movieLanguage,"+
            "COALESCE(m.movie_review_num, 0) AS movieReviewNum, " +
            "CASE WHEN m.movie_genre IS NULL OR m.movie_genre = '' THEN 'No Genre' ELSE m.movie_genre END AS movieGenre " )  // 限制每页返回的记录数
    List<MovieCollaboration> findAllMovies();


}
