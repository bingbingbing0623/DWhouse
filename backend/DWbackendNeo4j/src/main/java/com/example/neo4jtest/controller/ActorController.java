package com.example.neo4jtest.controller;

import com.example.neo4jtest.dto.*;
import com.example.neo4jtest.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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

    //6.电影详细信息通过评分
    @GetMapping("/score")
    public MovieResultDTO getMovieByScore(@RequestParam Float minscore, @RequestParam Float maxscore) {
        // 调试输出输入的参数
        System.out.println("进入 getMovieByScore 方法，接收到的参数：minscore = " + minscore + ", maxscore = " + maxscore);

        // 调用服务层方法，并输出返回的结果
        MovieResultDTO result = actorService.getMovieByScore(minscore, maxscore);

        // 输出返回的结果
        System.out.println("返回的 MovieResultDTO: " + result);

        return result;
    }

    @GetMapping("/genre")
    public MovieResultDTO getMovieByGenre(@RequestParam String genre,@RequestParam Integer page, @RequestParam Integer per_page) {
        // 调用服务层方法，并输出返回的结果
        System.out.println("genre_name: " + genre);
        System.out.println("page: " + page);
        System.out.println("per_page: " + per_page);

        return actorService.getMovieByGenre(genre,page,per_page);
    }

    @GetMapping("/findDir")
    public List<String> getDirectorName(){
        return actorService.findDirectorNames();
    }

    @GetMapping("/findAct")
    public List<String> getActorName(){
        return actorService.findActorNames();
    }

    @GetMapping("/findMov")
    public List<String> getMovieName(){
        return actorService.findMovieNames();
    }

    @GetMapping("/director")
    public MovieResultDTO getMovieByDirector(@RequestParam String director,@RequestParam Integer page, @RequestParam Integer per_page) {

        return actorService.getMovieByDirector(director,page,per_page);
    }

    @GetMapping("/actor")
    public MovieResultDTO getMovieByActor(@RequestParam String actor,@RequestParam Integer page, @RequestParam Integer per_page) {

        return actorService.getMovieByActor(actor,page,per_page);
    }

    @GetMapping("/main-actor")
    public MovieResultDTO getMovieByMainActor(@RequestParam String actor) {

        return actorService.getMovieByMainActor(actor);
    }

    @GetMapping("/movie")
    public MovieResultDTO getMovieByTitle(@RequestParam String title,@RequestParam Integer page, @RequestParam Integer per_page) {
        System.out.println("返回的title: " + title);
        return actorService.getMovieByTitle(title,page,per_page);
    }

    @GetMapping("/year")
    public MovieResultDTO getMovieByYear(@RequestParam String year,@RequestParam Integer page, @RequestParam Integer per_page) {
        return actorService.getMovieByYear(year,page,per_page);
    }
    @GetMapping("/month")
    public MovieResultDTO getMovieByYearAndMonth(@RequestParam String year,@RequestParam String month,@RequestParam Integer page, @RequestParam Integer per_page) {
        return actorService.getMovieByYearAndMonth(year,month,page,per_page);
    }
    @GetMapping("/season")
    public MovieResultDTO getMovieByYearAndSeason(@RequestParam String year,@RequestParam String season,@RequestParam Integer page, @RequestParam Integer per_page) {
        return actorService.getMovieByYearAndSeason(year,season,page,per_page);
    }
    @GetMapping("/weekday")
    public MovieResultDTO getMoviesByYearAndDay(@RequestParam String year,
                                                @RequestParam String weekday, // 星期几
                                                @RequestParam Integer page,
                                                @RequestParam Integer per_page) {

        // 解析年份
        int targetYear = Integer.parseInt(year);
        double startTime = System.currentTimeMillis();

        // 解析星期几
        DayOfWeek targetDayOfWeek = parseDayOfWeek( weekday);

        // 获取所有电影
        List<MovieCollaboration> allMovies = actorService.findAllMovies();

        List<MovieCollaboration> filteredMovies = new ArrayList<>();

        // 遍历所有电影，筛选出符合条件的
        for (MovieCollaboration movie : allMovies) {
            String releaseDate = movie.getMovieDate();
            // 如果发布日期无效（null），跳过该电影
            if (releaseDate == null) {
                continue;
            }
            // 判断年份和星期几是否匹配
            LocalDate date = parseReleaseDate(releaseDate); // 确保将日期解析为 LocalDate
            if(date==null){
                continue;
            }
            if (date.getYear() == targetYear && date.getDayOfWeek() == targetDayOfWeek) {
                filteredMovies.add(movie);
            }
        }

        // 组装返回结果
        MovieResultDTO result = new MovieResultDTO();
        result.setMovieList(filteredMovies);
        // 获取结束时间
        double endTime = System.currentTimeMillis();

        // 计算执行时间（秒）
        double executionTimeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("查询执行时间: " + executionTimeInSeconds + "秒");
        result.setTime(executionTimeInSeconds);
        return result;
    }

    // 辅助方法：根据字符串解析星期几
    private DayOfWeek parseDayOfWeek(String day) {
        int dayOfWeekInt;

        try {
            // 将传入的参数转换为整数
            dayOfWeekInt = Integer.parseInt(day);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid weekday parameter: " + day);
        }

        // 根据数字返回对应的 DayOfWeek
        switch (dayOfWeekInt) {
            case 1: return DayOfWeek.MONDAY;
            case 2: return DayOfWeek.TUESDAY;
            case 3: return DayOfWeek.WEDNESDAY;
            case 4: return DayOfWeek.THURSDAY;
            case 5: return DayOfWeek.FRIDAY;
            case 6: return DayOfWeek.SATURDAY;
            case 7: return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Invalid day of week number: " + dayOfWeekInt);
        }
    }


    // 辅助方法：解析 releaseDate
    private LocalDate parseReleaseDate(String dateStr) {
        try {
            // 如果日期字符串是 null 或者无效值（例如 "R"），则跳过处理
            if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("R")) {
                // 可以根据需求返回null，表示无效日期被跳过
                return null;  // 或者可以选择返回默认日期，或者不做任何处理
            }
            if (!dateStr.matches("\\d{1,2}-[A-Za-z]{3}-\\d{2}")) {
                return null;  // 如果格式不匹配，直接返回 null
            }
            // 定义自定义的日期格式（d-MMM-yy）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy", Locale.ENGLISH);
            // 解析日期
            LocalDate date = LocalDate.parse(dateStr, formatter);

            // 获取年份（两位数）
            int year = date.getYear() % 100; // 只取后两位

            // 判断年份是否小于30
            if (year < 30) {
                // 如果年份小于30，设为20xx年
                date = LocalDate.of(2000 + year, date.getMonth(), date.getDayOfMonth());
            } else {
                // 如果年份大于等于30，设为19xx年
                date = LocalDate.of(1900 + year, date.getMonth(), date.getDayOfMonth());
            }
            System.out.println("Parsed Date: " + date);

            return date;
        } catch (Exception e) {
            // 错误处理（例如日期格式不正确等）
            System.err.println("Error parsing date: " + dateStr);
            throw e;
        }
    }
}
