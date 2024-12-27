package com.mysql.dwbackened.service;

import com.mysql.dwbackened.dto.ActorRelationCountDto;
import com.mysql.dwbackened.dto.DirectorActorCountDto;
import com.mysql.dwbackened.dto.MovieDetailDto;
import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.entity.Movie;
import com.mysql.dwbackened.mapper.*;
import org.apache.hadoop.mapred.lib.DelegatingInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private VersionMapper versionMapper;

    @Override
    public HashMap<String, Object> getMovieCount(MovieSearchDto movieSearchDto) {
        // 数据源使用 hive
//        DataSourceUtil.setDB("db2");

        HashMap<String,Object> result=new HashMap<>();
        Set<String> movieSet=movieMapper.selectByTitle(movieSearchDto);
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
        result.put("pages",movieSet.size()/ movieSearchDto.getPer_page());
        return result;
    }

    @Override
    public HashMap<String, Object> getMovieInfo(MovieSearchDto movieSearchDto) {
        System.out.println("#########这是传入的"+ movieSearchDto);
        long startTime = System.currentTimeMillis();
        int start= (movieSearchDto.getPage()-1) * movieSearchDto.getPer_page();
        List<ActorRelationCountDto> actorRelationCountDtoList = new ArrayList<>();
        List<DirectorActorCountDto> directorActorCountDtoList = new ArrayList<>();

        HashMap<String,Object> result=new HashMap<>();
        Set<String> movieSet = new HashSet<>();
        if (movieSearchDto.getColumns().contains("movies")) {
            System.out.println("#########进入getTitle");
            movieSet = movieMapper.selectByTitle(movieSearchDto);
        }
        if(movieSearchDto.getColumns().contains("genres")) {
            System.out.println("#########进入getGenre");
            movieSet = (genreMapper.selectByGenreName(movieSearchDto));
        }
        if(movieSearchDto.getColumns().contains("evaluates")) {
            System.out.println("#########进入getScore");
            movieSet = (movieMapper.selectByScore(movieSearchDto));
        }
        if(movieSearchDto.getColumns().contains("date")) {
            System.out.println("#########进入getTime");
            System.out.println("#########movieSearchDto是"+movieSearchDto);
            movieSet = (dateMapper.selectByTime(movieSearchDto));
        }
        if (movieSearchDto.getColumns().contains("directors")) {
            System.out.println("#########进入getdirector");
            movieSet = (directorMapper.selectByName(movieSearchDto));
        }
        if (movieSearchDto.getColumns().contains("actors")) {
            System.out.println("#########进入getActor");
            movieSet = (actorMapper.selectByName(movieSearchDto));
        }
        if (movieSearchDto.getColumns().contains("actor")) {
            System.out.println("#########进入getActor");
            actorRelationCountDtoList = (actorMapper.selectActorRelationPage(1,1));
        }
        if (movieSearchDto.getColumns().contains("director")) {
            System.out.println("#########进入getDrector");
            directorActorCountDtoList = (directorMapper.selectDirectorActorPage(1,1));
            System.out.println("#########进入actorDirector"+directorActorCountDtoList);
        }
        List<MovieDetailDto> movieDetailDtoList=new ArrayList<>();
        if(!movieSet.isEmpty()) {
//            System.out.println("#########这是perpage"+ movieSearchDto.getPer_page());
//            System.out.println("#########这是start"+ start);
//            System.out.println("#########这是movieSet"+ movieSet);
            System.out.println("#########这是movieset"+movieSet);
            Iterator<String> iterator = movieSet.iterator();
            List<Movie> movieList = new ArrayList<>();
            while (iterator.hasNext()) {
                String movieId = iterator.next();
                Movie currentMovie = movieMapper.getMovieInfo(movieId, start, movieSearchDto.getPer_page());
                System.out.println("#########这是currentMovie:"+ currentMovie);
                if (currentMovie != null) {
                    movieList.add(currentMovie);
                }
            }
//            System.out.println("#########这是movieList"+ movieList);
            for(Movie movie : movieList){
                MovieDetailDto movieDetailDto=new MovieDetailDto();
                if(movieSearchDto.getColumns().contains("movies")
                        ||movieSearchDto.getColumns().contains("directors")
                        ||movieSearchDto.getColumns().contains("actors")
                        ||movieSearchDto.getColumns().contains("genres")
                        ||movieSearchDto.getColumns().contains("evaluates")
                        ||movieSearchDto.getColumns().contains("date")) {
                    movieDetailDto.setMovieId(movie.getMovieId());
                    movieDetailDto.setMovieTitle(movie.getMovieTitle());
                    movieDetailDto.setMovieRating(String.valueOf(movie.getMovieRating()));
                    movieDetailDto.setMovieReviewNum(String.valueOf(movie.getMovieReviewNum()));
                }
                movieDetailDtoList.add(movieDetailDto);
            }
        }
        if (movieSearchDto.getColumns().contains("actor")){
            result.put("data",actorRelationCountDtoList);
        } else if (movieSearchDto.getColumns().contains("director")) {
            result.put("data",directorActorCountDtoList);
        } else {
            result.put("data", movieDetailDtoList);
        }

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        System.out.println("#########这是最终的结果"+ result);
        return result;
    }
}
