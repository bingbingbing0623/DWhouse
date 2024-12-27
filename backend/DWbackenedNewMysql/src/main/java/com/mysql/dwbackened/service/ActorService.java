package com.mysql.dwbackened.service;

import com.mysql.dwbackened.dto.MovieSearchDto;

import java.util.HashMap;

/**
 * @version 1.0
 * @title ActorService
 * @description
 */
public interface ActorService {
    HashMap<String, Object> getActorRecommend(String actor, int amount);

    HashMap<String, Object> selectActorRelationCount(int page, int perPage);

    HashMap<String, Object> selectActorRelationInfo();


    HashMap<String, Object> getGenreRelationInfo(String genre);

    HashMap<String, Object> getActorMovie(MovieSearchDto movieSearchDto);
}
