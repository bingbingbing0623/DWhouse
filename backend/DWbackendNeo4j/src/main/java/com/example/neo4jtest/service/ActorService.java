package com.example.neo4jtest.service;

import com.example.neo4jtest.dto.*;
import com.example.neo4jtest.entity.Movie;
import com.example.neo4jtest.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public List<ActorCollaboration> getFrequentCollaborators(Integer start, Integer perPage) {
        return actorRepository.findFrequentCollaborators(start, perPage);
    }
    public List<ActorDirectorCollaboration> getFrequentDirector(Integer start, Integer perPage) {
        return actorRepository.findFrequentDirectors(start, perPage);
    }

    public Collaboration2ResultDTO getCollaboration2(String genre) {
        double startTime = System.currentTimeMillis(); // 获取开始时间

        List<ActorDoubleCollaboration> doubleCollab = actorRepository.findMostReviewedCollaboration(genre);

        double endTime = System.currentTimeMillis(); // 结束时间

        double executionTimeInSeconds = (endTime - startTime) / 1000.0;

        return new Collaboration2ResultDTO(doubleCollab, executionTimeInSeconds);
    }

    public List<ActorCollaborationWithRating> getTopActorCollaborationsWithRatings(int start, int perPage) {
        return actorRepository.findActorCollaborationWithHighestRatings(start, perPage);
    }
    public MovieResultDTO getMovieByScore(Float minscore, Float maxscore) {
        // 输出评分区间
        System.out.println("评分区间: " + minscore + " 到 " + maxscore);


        // 获取开始时间
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByScore(minscore, maxscore);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }


    public MovieResultDTO getMovieByGenre(String genre, Integer page, Integer perPage) {
        // 获取开始时间
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByGenre(genre,page,perPage);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量genre: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }
    public List<String> findDirectorNames(){
        return actorRepository.findDirectorNames();
    }

    public MovieResultDTO getMovieByDirector(String director, Integer page, Integer perPage) {

        // 获取开始时间
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByDirector(director,page,perPage);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量director: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }

    public List<String> findActorNames() {
        return actorRepository.findActorNames();
    }
    public MovieResultDTO getMovieByActor(String actor, Integer page, Integer perPage){
        // 获取开始时间
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByActor(actor,page,perPage);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量actor: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }
    public MovieResultDTO getMovieByMainActor(String actor){
        // 获取开始时间
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByMainActor(actor);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量actor: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }

    public MovieResultDTO getMovieByTitle(String title, int start,int perPage){
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByTitle(title,start,perPage);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量title: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }

    public List<String> findMovieNames() {
        return actorRepository.findMovieNames();
    }
    public MovieResultDTO getMovieByYear(String year, int start,int perPage){
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByYear(year,start,perPage);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量year: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }

    public MovieResultDTO getMovieByYearAndMonth(String year,String month, int start,int perPage){
        double startTime = System.currentTimeMillis();

        // 调用数据库查询
        List<MovieCollaboration> movieList = actorRepository.findMovieByYearAndMonth(year,month,start,perPage);

        // 输出查询返回的电影数目
        System.out.println("查询返回的电影数量year: " + movieList.size());
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(movieList, executionTimeInSeconds);
    }

    public MovieResultDTO getMovieByYearAndSeason(String year, String months, Integer page, Integer perPage) {
        double startTime = System.currentTimeMillis();
        List<MovieCollaboration> all= new ArrayList<>();
        // 将字符串按逗号分割成数组
        String[] monthst = months.split(",");

        // 调用数据库查询
        for (String month : monthst) {
            int currentPage = page; // 每个月的数据从第1页开始
            List<MovieCollaboration> movieList;
            do {
                // 查询当前月的数据
                movieList = actorRepository.findMovieByYearAndMonth(year, month, currentPage, perPage);
                all.addAll(movieList);
                currentPage++; // 增加分页
            } while (movieList.size() == perPage); // 如果当前页的数量等于perPage，说明可能还有更多数据
        }
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");

        // 返回结果
        return new MovieResultDTO(all, executionTimeInSeconds);
    }

    public List<MovieCollaboration> findAllMovies() {
        return actorRepository.findAllMovies();
    }
}
