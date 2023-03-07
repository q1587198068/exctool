package com.lilan.exctool.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("test")
public class TestController {

    private static HashMap map=new HashMap();

    @RequestMapping("setMapkv")
    @ResponseBody
    public String setMapkv(@RequestParam("mapk") String mapk,@RequestParam("mapv") String mapv){
        map.put(mapk,mapv);
        System.out.println(map.toString());
        return String.valueOf(map.size());
    }


    @RequestMapping("getMapkv")
    @ResponseBody
    public String getMapkv(@RequestParam("mapk") String mapk){
        Object o = map.get(mapk);
        System.out.println(map.toString());

        return o.toString();
    }






}
