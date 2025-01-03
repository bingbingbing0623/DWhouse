package com.mysql.dwbackened.service;


import com.mysql.dwbackened.dto.ActorRelationCountDto;
import com.mysql.dwbackened.dto.ActorRelationInfoDto;
import com.mysql.dwbackened.dto.ActorTopRelationDto;
import com.mysql.dwbackened.dto.ActorTopRelationInfoDto;
import com.mysql.dwbackened.mapper.ActorMapper;
import com.mysql.dwbackened.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    ActorMapper actorMapper;

    @Autowired
    MovieMapper movieMapper;

    @Override
    public HashMap<String, Object> getActorRecommend(String actor, int amount) {
        HashMap<String, Object> result=new HashMap<>();
        result.put("suggestions",actorMapper.getActorRecommend(actor,amount));
        return result;
    }

    @Override
    public HashMap<String, Object> selectActorRelationCount(int page, int perPage) {
        HashMap<String,Object> result=new HashMap<>();
        result.put("pages",actorMapper.selectActorRelationCount().size()/perPage);
        return result;
    }


    @Override
    public HashMap<String, Object> selectActorRelationInfo(int page, int perPage) {
        long startTime = System.currentTimeMillis();
        HashMap<String,Object> result=new HashMap<>();
        int start=(page-1)*perPage;
        List<ActorRelationCountDto> actorRelationCountDtoList =actorMapper.selectActorRelationPage(start,perPage);
        System.out.println("#########一开始actorRelationCountDtoList"+ actorRelationCountDtoList);
        List<ActorRelationInfoDto> actorRelationInfoDtoList=new ArrayList<>();
        for(ActorRelationCountDto actorRelationCountDto : actorRelationCountDtoList){
            ActorRelationInfoDto actorRelationInfoDto=new ActorRelationInfoDto();
            //查询对应人名
            actorRelationInfoDto.setName1(actorMapper.selectNameByActorId(actorRelationCountDto.getFirstPersonId()));
            actorRelationInfoDto.setName2(actorMapper.selectNameByActorId(actorRelationCountDto.getSecondPersonId()));
            actorRelationInfoDto.setTimes(actorRelationCountDto.getCooperationCount());

            actorRelationInfoDtoList.add(actorRelationInfoDto);
        }
        result.put("data",actorRelationInfoDtoList);

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        System.out.println("#########这是最终的结果"+ result);
        return result;
    }

    @Override
    public HashMap<String, Object> getGenreRelationInfo(String genre) {
        long startTime = System.currentTimeMillis();
        HashMap<String,Object> result=new HashMap<>();

        List<ActorRelationCountDto> actorRelationCountDtoList =actorMapper.getGenreRelationInfo(genre);
        System.out.println("#########一开始actorRelationCountDtoList"+ actorRelationCountDtoList);
        List<ActorRelationInfoDto> actorRelationInfoDtoList=new ArrayList<>();
        for(ActorRelationCountDto actorRelationCountDto : actorRelationCountDtoList){
            ActorRelationInfoDto actorRelationInfoDto=new ActorRelationInfoDto();
            //查询对应人名
            actorRelationInfoDto.setName1(actorMapper.selectNameByActorId(actorRelationCountDto.getFirstPersonId()));
            actorRelationInfoDto.setName2(actorMapper.selectNameByActorId(actorRelationCountDto.getSecondPersonId()));
            actorRelationInfoDto.setTimes(actorRelationCountDto.getCooperationCount());
            actorRelationInfoDtoList.add(actorRelationInfoDto);
        }
        result.put("data",actorRelationInfoDtoList);

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        System.out.println("#########这是最终的结果"+ result);
        return result;
    }

    @Override
    public HashMap<String, Object> getTopRelationInfo() {
        long startTime = System.currentTimeMillis();
        HashMap<String,Object> result=new HashMap<>();

        List<ActorTopRelationDto> actorTopRelationDtoList =actorMapper.getTopRelationInfo();
        System.out.println("#########一开始actorRelationCountDtoList"+ actorTopRelationDtoList);
        List<ActorTopRelationInfoDto> actorTopRelationInfoDtoList=new ArrayList<>();
        for(ActorTopRelationDto actorTopRelationDto : actorTopRelationDtoList){
            ActorTopRelationInfoDto actorTopRelationInfoDto=new ActorTopRelationInfoDto();
            //查询对应人名
            actorTopRelationInfoDto.setName1(actorMapper.selectNameByActorId(actorTopRelationDto.getFirstPersonId()));
            actorTopRelationInfoDto.setName2(actorMapper.selectNameByActorId(actorTopRelationDto.getSecondPersonId()));
            actorTopRelationInfoDto.setAverageRating(actorTopRelationDto.getAverageRating());
            actorTopRelationInfoDtoList.add(actorTopRelationInfoDto);
        }
        result.put("data",actorTopRelationInfoDtoList);

        //统计查询时间
        long queryTimeMillis = System.currentTimeMillis() - startTime;
        double queryTimeSeconds = queryTimeMillis / 1000.0; // 将毫秒转换为秒
        result.put("consuming_time",queryTimeSeconds);
        System.out.println("#########这是最终的结果"+ result);
        return result;
    }
}
