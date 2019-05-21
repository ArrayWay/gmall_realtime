package com.atguigu.gmall1128.publisher.controller;


import com.alibaba.fastjson.JSON;
import com.atguigu.gmall1128.publisher.service.PublisherService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @GetMapping("realtime-total")
    public String getTotal(@RequestParam("date") String date){

        List<Map> totalList=new ArrayList();
        Map dauMap=new HashMap();
        dauMap.put("id","dau");
        dauMap.put("name","新增日活");
        Integer dauTotal = publisherService.getDauTotal(date);
        dauMap.put("value",dauTotal);
        totalList.add(dauMap);

        Map newMidMap=new HashMap();
        newMidMap.put("id","new_mid");
        newMidMap.put("name","新增设备");
        newMidMap.put("value",12000);
        totalList.add(newMidMap);

       return   JSON.toJSONString(totalList);
    }

    /**
     * 求分时统计
     * @param id
     * @param today
     * @return
     */
    @GetMapping("realtime-hour")
    public String getRealtimeHour(@RequestParam("id")String id ,@RequestParam("date") String today){
        String result="";
        if("dau".equals(id)){
            Map dauHoursTDMap = publisherService.getDauHoursMap(today);
            String yesterday = getYesterday(today);
            Map dauHoursYDMap = publisherService.getDauHoursMap(yesterday);

            Map dauHourMap=new HashMap();
            dauHourMap.put("today",dauHoursTDMap);
            dauHourMap.put("yesterday",dauHoursYDMap);
            result=JSON.toJSONString(dauHourMap);
        }
        return result;
    }



    public String  getYesterday(String today){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String  yesterday=null;
        try {
            Date todayDate = simpleDateFormat.parse(today);
            Date yesterdayDate = DateUtils.addDays(todayDate, -1);
            yesterday=simpleDateFormat.format(yesterdayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return yesterday;
    }

}
