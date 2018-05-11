package com.licc.task.service;

import com.licc.task.Const;
import com.licc.task.util.RedisUtils;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class IpService {
    @Resource
    RedisUtils redisUtils;


    public Set getIp() {
        return redisUtils.hkeys(Const.REDIS_USE_IP);
    }

    public Set getUseFulProxy() {
        return redisUtils.hkeys(Const.REDIS_USEFUL_PROXY);
    }


    public  void deleteUseProxy(String ip){
         redisUtils.hdelete(Const.REDIS_USEFUL_PROXY,ip);

    }
    public void setUseIp(String ip){
        redisUtils.hset(Const.REDIS_USE_IP,ip,ip);
    }

    public void setUseUrlIp(String url,String ip){
        redisUtils.hset(Const.REDIS_USE_START_IP,url+"_"+ip,ip);
    }

    //判断是否刷过该链接
    public boolean hasUseUrlIp(String url,String ip){
      return   redisUtils.hasKey(Const.REDIS_USE_START_IP,url+"_"+ip);
    }

}
