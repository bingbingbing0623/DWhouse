package com.example.neo4jtest.controller;

import com.example.neo4jtest.dto.*;
import com.example.neo4jtest.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000") // 允许前端的URL跨域访问
@RestController
@RequestMapping("/neo4j/actors")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }
    //1.合作最多的2个演员
    @GetMapping("/frequent-actors")
    public ActorCollaborationResponse getFrequentActors(@RequestParam Integer page, @RequestParam Integer per_page) {
        double startTime = System.currentTimeMillis();
        List<ActorCollaboration> collaborations = actorService.getFrequentCollaborators(page, per_page);
        double endTime = System.currentTimeMillis();
        double executionTime = endTime - startTime;

        // 返回带数据的响应
        return new ActorCollaborationResponse(collaborations, executionTime / 1000.0);
    }
    //2.合作次数大于5次的演员总数量
    @GetMapping("/frequent-actors-number")
    public Integer getFrequentActorsNumber() {
        return actorService.getFrequentCollaboratorNumber();
    }

    //3.最常合作的导演和演员
    @GetMapping("/frequent-directors")
    public ActorDirectorCollaborationResponse getFrequentDirectors(@RequestParam Integer page, @RequestParam Integer per_page) {
        double startTime = System.currentTimeMillis(); // 获取开始时间
        List<ActorDirectorCollaboration> collaborations = actorService.getFrequentDirector(page, per_page); // 调用方法
        long endTime = System.currentTimeMillis(); // 结束时间

        double executionTime = endTime - startTime; // 计算执行时间
        return new ActorDirectorCollaborationResponse(collaborations, executionTime/1000.0); // 返回新的封装对象
    }
    //4.合作口碑最高
    @GetMapping("/toprating")
    public ActorCollaborationWithRatingResponse  getTopActorCollaborationsWithRating(@RequestParam Integer page, @RequestParam Integer per_page) {
        double startTime = System.currentTimeMillis(); // 获取开始时间
        List<ActorCollaborationWithRating> c = actorService.getTopActorCollaborationsWithRatings(page, per_page); // 调用方法
        long endTime = System.currentTimeMillis(); // 结束时间

        double executionTime = endTime - startTime; // 计算执行时间
        return new ActorCollaborationWithRatingResponse(c, executionTime/1000.0); // 返回新的封装对象
    }
    //5.前10平均总评论数最多2个演员
    @GetMapping("/most-attracted-group-2")
    public Collaboration2ResultDTO getCollaboration2(String genre) {

        return actorService.getCollaboration2(genre);
    }

    //6.前10平均总评论数最多3个演员
    @GetMapping("/most-attracted-group-23")
    public Collaboration2and3ResultDTO getCollaboration2and3(String genre) {
        return actorService.getCollaboration2and3(genre);
    }

}
