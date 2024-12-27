package com.mysql.dwbackened.service;

import com.mysql.dwbackened.dto.MovieDetailDto;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Movie;
import com.mysql.dwbackened.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wyx20
 * @version 1.0
 * @title MovieServiceImpl
 * @description
 * @create 2023/12/25 14:39
 */
@Service
public class MovieServiceImpl implements MovieService{
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private DateMapper dateMapper;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private DirectorMapper directorMapper;


    @Override
    public HashMap<String, Object> getMovieCount(MovieSearchDto movieSearchDto) {
        // 数据源使用 hive
//        DataSourceUtil.setDB("db2");

        HashMap<String,Object> result=new HashMap<>();
        Set<String> movieSet=movieMapper.selectByScoreAndTitle(movieSearchDto);
        if(movieSearchDto.getGenre_name()!="") {
            movieSet.retainAll(genreMapper.selectByGenreName(movieSearchDto));
        }

        movieSet.retainAll(dateMapper.selectByTime(movieSearchDto));

        if(movieSearchDto.getActor()!="") {
            //根据演员名先获取对应演员Id集合，再遍历act表将所有演员Id在该集合中的记录保留
//            Set<Integer> actorIdList=actorMapper.selectByName(movieSearchDto);
//            movieSet.retainAll(actorMapper.selectMovieByActorId(actorIdList));
            movieSet.retainAll(actorMapper.selectByName(movieSearchDto));
        }

        if (movieSearchDto.getDirector() != "") {
            //根据导演名先获取对应导演Id集合，再遍历director表将所有导演Id在该集合中的记录保留
//            Set<Integer> directorIdList=directorMapper.selectByName(movieSearchDto);
//            movieSet.retainAll(directorMapper.selectMovieByDirectorId(directorIdList));
            movieSet.retainAll(directorMapper.selectByName(movieSearchDto));
        }

//        movieSet.retainAll(directMovieSet);
        //result.put("pages",movieSet.size()/ movieSearchDto.getPer_page());
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
    public HashMap<String, Object> getMovieInfo(MovieSearchDto movieSearchDto) {
        long startTime = System.currentTimeMillis();
        //int start= (movieSearchDto.getPage()-1) * movieSearchDto.getPer_page();

        HashMap<String,Object> result=new HashMap<>();
        Set<String> movieSet=movieMapper.selectByScoreAndTitle(movieSearchDto);

        System.out.println("movieSet.size():"+movieSet.size());

        System.out.println("movieSearchDto.getGenre_name()"+movieSearchDto.getGenre_name());
        System.out.println(Objects.equals(movieSearchDto.getGenre_name(), ""));
        System.out.println(movieSearchDto.getGenre_name() != null);
        if(!Objects.equals(movieSearchDto.getGenre_name(), "") && movieSearchDto.getGenre_name() != null) {
            movieSet.retainAll(genreMapper.selectByGenreName(movieSearchDto));
        }

        System.out.println("movieSet.size():"+movieSet.size());


        //System.out.println("movieSearchDto.getYear() = " + movieSearchDto.getYear() + ", type: " + ((Object) movieSearchDto.getYear()).getClass().getName());
        //System.out.println(dateMapper.selectByYear(String.valueOf(movieSearchDto.getYear())));
        movieSet.retainAll(dateMapper.selectByTime(movieSearchDto));

        System.out.println("movieSet.size():"+movieSet.size());


        if(movieSearchDto.getActor() != null) {
            //根据演员名先获取对应演员Id集合，再遍历act表将所有演员Id在该集合中的记录保留
//            Set<Integer> actorIdList=actorMapper.selectByName(movieSearchDto);
//            movieSet.retainAll(actorMapper.selectMovieByActorId(actorIdList));
            movieSet.retainAll(actorMapper.selectByName(movieSearchDto));
        }
        if (movieSearchDto.getDirector() != null) {
            //根据导演名先获取对应导演Id集合，再遍历director表将所有导演Id在该集合中的记录保留
//            Set<Integer> directorIdList=directorMapper.selectByName(movieSearchDto);
//            movieSet.retainAll(directorMapper.selectMovieByDirectorId(directorIdList));
            movieSet.retainAll(directorMapper.selectByName(movieSearchDto));
        }
//        result.put("pages",movieSet.size()/ movieSearchDto.getPer_page());

        List<MovieDetailDto> movieDetailDtoList = new ArrayList<>();
        if (!movieSet.isEmpty()) {
            List<Movie> movieList = getMoviesByBatch(movieSet);
            for (Movie movie : movieList) {
                MovieDetailDto movieDetailDto = new MovieDetailDto();

                // 设置电影ID（ASIN）
                movieDetailDto.setMovieId(movie.getMovieId());

                // 设置电影标题
                movieDetailDto.setMovieTitle(movie.getTitle());

                // 设置评分
                movieDetailDto.setMovieRating(movie.getAverageRating());

//                // 设置评论数量
//                if (movieSearchDto.getColumns().contains("comment_num")) {
//                    movieDetailDto.setPositive(String.valueOf(movie.getCommentCount()));
//                }

                // 设置上映日期
                movieDetailDto.setMovieDate(movie.getReleaseDate().toString());

                movieDetailDtoList.add(movieDetailDto);
            }
        }

        result.put("data",movieDetailDtoList);

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        return result;
    }

    @Override
    public HashMap<String, Object> getTitleRecommend(String title, int amount) {
        HashMap<String, Object> result=new HashMap<>();
        result.put("suggestions",null);
        return result;
    }


}
