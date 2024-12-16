package com.example.neo4jtest.repository;

import com.example.neo4jtest.dto.*;
import com.example.neo4jtest.entity.Actor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActorRepository extends Neo4jRepository<Actor, Integer> {
    //1.最常合作的演员
    @Query("MATCH (a1:Actor)-[:ACTED]->(:Movie)<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, COUNT(*) AS collaborationCount " +
            "ORDER BY collaborationCount DESC " +
            "SKIP ($start - 1) * $perPage " +
            "LIMIT $perPage")
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
            "ORDER BY collaborationCount DESC " +
            "SKIP ($start - 1) * $perPage " +
            "LIMIT $perPage")
    List<ActorDirectorCollaboration> findFrequentDirectors(int start, int perPage);

    //4.查找合作演员口碑排名
    @Query("MATCH (a1:Actor)-[:ACTED]->(m:Movie)<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) AND m.movie_average_rating IS NOT NULL " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, COUNT(*) AS collaborationCount, AVG(m.movie_average_rating) AS avgRating " +
            "ORDER BY avgRating DESC, collaborationCount DESC " +
            "SKIP ($start - 1) * $perPage " +
            "LIMIT $perPage")
    List<ActorCollaborationWithRating> findActorCollaborationWithHighestRatings(int start, int perPage);
    //5.前10平均总评论数最多2个演员
    @Query("MATCH (a1:Actor)-[:ACTED]->(m:Movie {movie_genre: $genre})<-[:ACTED]-(a2:Actor) " +
            "WHERE ID(a1) < ID(a2) AND m.movie_review_num IS NOT NULL " +
            "WITH a1, a2, COUNT(m) AS movieCount, SUM(COALESCE(TOINTEGER(m.movie_review_num), 0)) AS totalReviews, " +
            "     CASE WHEN COUNT(m) > 0 THEN SUM(COALESCE(TOINTEGER(m.movie_review_num), 0)) / COUNT(m) ELSE 0 END AS avgReviews " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, avgReviews " +
            "ORDER BY avgReviews DESC " +
            "LIMIT 10")
    List<ActorDoubleCollaboration> findMostReviewedCollaboration(String genre);
    //6.前10平均总评论数最多3个演员
    @Query("MATCH (a1:Actor)-[:ACTED]->(m1:Movie {movie_genre: $genre})<-[:ACTED]-(a2:Actor)-[:ACTED]->(m2:Movie {movie_genre: $genre})<-[:ACTED]-(a3:Actor) " +
            "WHERE ID(a1) < ID(a2) AND ID(a2) < ID(a3) AND m1.movie_review_num IS NOT NULL AND m2.movie_review_num IS NOT NULL " +
            "WITH a1, a2, a3, COUNT(m1) + COUNT(m2) AS movieCount, " +
            "     SUM(COALESCE(TOINTEGER(m1.movie_review_num), 0) + COALESCE(TOINTEGER(m2.movie_review_num), 0)) AS totalReviews, " +
            "     CASE WHEN COUNT(m1) + COUNT(m2) > 0 THEN SUM(COALESCE(TOINTEGER(m1.movie_review_num), 0) + COALESCE(TOINTEGER(m2.movie_review_num), 0)) / (COUNT(m1) + COUNT(m2)) ELSE 0 END AS avgReviews " +
            "RETURN a1.actor_name AS actorName1, a2.actor_name AS actorName2, a3.actor_name AS actorName3, avgReviews " +
            "ORDER BY avgReviews DESC " +
            "LIMIT 10")
    List<ActorTripleCollaboration> findMostReviewedTripleCollaboration(String genre);




}
