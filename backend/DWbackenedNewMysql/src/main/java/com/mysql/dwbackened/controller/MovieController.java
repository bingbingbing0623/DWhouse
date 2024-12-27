package com.mysql.dwbackened.controller;

import com.mysql.dwbackened.dto.MovieSearchDto;
import com.mysql.dwbackened.service.ActorService;
import com.mysql.dwbackened.service.MovieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author wyx20
 * @version 1.0
 * @title MovieController
 * @description
 * @create 2023/12/25 14:26
 */
@RestController
@RequestMapping("/movie")
public class MovieController {


    @Autowired
    MovieService movieService;
    @Autowired
    ActorService actorService;
    @PostMapping("/count")
    @ApiOperation(notes = "", value = "获取查询结果总数")
    public HashMap<String,Object> getMovieCount(@RequestBody MovieSearchDto movieSearchDto){
        return movieService.getMovieCount(movieSearchDto);
    }

    @PostMapping("/detail")
    @ApiOperation(notes="",value="获取查询详细结果")
    public HashMap<String,Object> getMovieInfo(@RequestBody MovieSearchDto movieSearchDto){
        System.out.println("1111111111111111111111111111111111111");
        System.out.println("1111111111111111111111111111111111111");
        System.out.println(movieSearchDto);
        return movieService.getMovieInfo(movieSearchDto);
    }

    @PostMapping("/main")
    @ApiOperation(notes="",value="获取某演员主演的电影")
    public HashMap<String,Object> getActorMovie(@RequestBody MovieSearchDto movieSearchDto){

        return actorService.getActorMovie(movieSearchDto);
    }
}
