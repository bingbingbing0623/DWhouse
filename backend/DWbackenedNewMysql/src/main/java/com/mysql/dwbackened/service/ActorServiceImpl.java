package com.mysql.dwbackened.service;


import com.mysql.dwbackened.dto.ActorRelationCountDto;
import com.mysql.dwbackened.dto.ActorRelationInfoDto;
import com.mysql.dwbackened.dto.MovieDetailDto;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Movie;
import com.mysql.dwbackened.mapper.ActorMapper;
import com.mysql.dwbackened.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    ActorMapper actorMapper;

    @Autowired
    MovieMapper movieMapper;

    @Override
    public HashMap<String, Object> getActorRecommend(String actor, int amount) {
        HashMap<String, Object> result=new HashMap<>();
        result.put("suggestions",null);
        return result;
    }

    /**
     * @description 查询合作次数大于五的组合总数
     * @author wyx20
     * @param[1] page
     * @param[2] perPage
     * @throws
     * @return HashMap<Object>
     * @time 2023/12/26 21:46
     */

    @Override
    public HashMap<String, Object> selectActorRelationCount(int page, int perPage) {
        HashMap<String,Object> result=new HashMap<>();
        result.put("pages",actorMapper.selectActorRelationCount().size()/perPage);
        return result;
    }


    @Override
    public HashMap<String, Object> selectActorRelationInfo() {
        long startTime = System.currentTimeMillis();
        HashMap<String,Object> result=new HashMap<>();
        //int start=(page-1)*perPage;

        List<ActorRelationCountDto> actorRelationCountDtoList =actorMapper.selectActorRelationPage();

        List<ActorRelationInfoDto> actorRelationInfoDtoList=new ArrayList<>();

        for(ActorRelationCountDto actorRelationCountDto : actorRelationCountDtoList){
            ActorRelationInfoDto actorRelationInfoDto=new ActorRelationInfoDto();
            //查询对应人名
            actorRelationInfoDto.setName1(actorMapper.selectNameByActorId(actorRelationCountDto.getLeftPersonId()));
            actorRelationInfoDto.setName2(actorMapper.selectNameByActorId(actorRelationCountDto.getRightPersonId()));
            actorRelationInfoDto.setTimes(actorRelationCountDto.getCooperationCount());

            actorRelationInfoDtoList.add(actorRelationInfoDto);
        }
        result.put("data",actorRelationInfoDtoList);

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        return result;
    }

    @Override
    public HashMap<String, Object> getGenreRelationInfo(String genre) {
        long startTime = System.currentTimeMillis();
        HashMap<String,Object> result=new HashMap<>();

        List<ActorRelationCountDto> actorRelationCountDtoList =actorMapper.getGenreRelationInfo(genre);
        List<ActorRelationInfoDto> actorRelationInfoDtoList=new ArrayList<>();

        for(ActorRelationCountDto actorRelationCountDto : actorRelationCountDtoList){
            ActorRelationInfoDto actorRelationInfoDto=new ActorRelationInfoDto();
            //查询对应人名
            actorRelationInfoDto.setName1(actorMapper.selectNameByActorId(actorRelationCountDto.getLeftPersonId()));
            actorRelationInfoDto.setName2(actorMapper.selectNameByActorId(actorRelationCountDto.getRightPersonId()));
            actorRelationInfoDto.setTimes(actorRelationCountDto.getCooperationCount());

            actorRelationInfoDtoList.add(actorRelationInfoDto);
        }
        result.put("data",actorRelationInfoDtoList);

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        return result;
    }

    public List<Movie> getMoviesByBatch(Set<String> movieSet) {
        List<Movie> result = new ArrayList<>();
        int batchSize = 1000; // 每次最多查询1000个
        List<String> asinList = new ArrayList<>(movieSet);

        for (int i = 0; i < asinList.size(); i += batchSize) {
            List<String> batch = asinList.subList(i, Math.min(i + batchSize, asinList.size()));
            List<Movie> batchResult = movieMapper.getMovieInfo(new HashSet<>(batch));
            result.addAll(batchResult);
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getActorMovie(MovieSearchDto movieSearchDto) {
        long startTime = System.currentTimeMillis();
        HashMap<String, Object> result = new HashMap<>();

        Set<String> movieSet= movieMapper.selectByScoreAndTitle(movieSearchDto);

        movieSet.retainAll(actorMapper.selectMainMovieByActorName(movieSearchDto));

        List<Movie> movieList = getMoviesByBatch(movieSet);

        List<MovieDetailDto> movieDetailDtoList = new ArrayList<>();
        for (Movie movie : movieList) {
            MovieDetailDto movieDetailDto = new MovieDetailDto();
            movieDetailDto.setMovieId(movie.getMovieId());
            movieDetailDto.setMovieTitle(movie.getTitle());
            movieDetailDto.setMovieRating(movie.getAverageRating());
            movieDetailDto.setMovieDate(movie.getReleaseDate().toString());
            movieDetailDtoList.add(movieDetailDto);
        }
        result.put("data", movieDetailDtoList);
        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        return result;
    }
}
