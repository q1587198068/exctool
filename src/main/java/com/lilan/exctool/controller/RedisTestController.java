package com.lilan.exctool.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("redis")
public class RedisTestController {


    /*redis 测试*/
    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("/set/{key}/{value}")
    @ResponseBody
    public String s1(@PathVariable String key, @PathVariable String value) {
        // 设置字符串
        RBucket<String> keyObj = redissonClient.getBucket(key);
        keyObj.set(value);
        return key + "-" + value;
    }

    @GetMapping("/get/{key}")
    @ResponseBody
    public String getKey(@PathVariable String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        Object o1 = bucket.get();

        String o = null == o1? "null":bucket.get().toString();
        return o;
    }


    @RequestMapping("getlock1")
    @ResponseBody
    public String redisLockTest() {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        try {
            System.out.println("方法1  获取锁");
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("方法1  释放锁");
            lock.unlock();
        }
        return "释放锁";
    }


    @RequestMapping("getlock2")
    @ResponseBody
    public String redisLockTest2() {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        try {
            System.out.println("方法2  获取锁");
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println("方法2  释放锁");
        return "释放锁";
    }

    @RequestMapping("setex/{key}/{time}")
    @ResponseBody
    public String redisexTest(@PathVariable String key,@PathVariable int time) {
        RBucket<Object> k1 = redissonClient.getBucket(key);
        k1.set(key+"过期time"+time+"秒", time, TimeUnit.SECONDS);
        
        return key+"过期time"+time+"秒";
    }

}
