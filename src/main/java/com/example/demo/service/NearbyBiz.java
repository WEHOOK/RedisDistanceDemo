package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.RedisDao;
import com.example.demo.pojo.NearbyBO;
import com.example.demo.pojo.NearbyPO;
import com.example.demo.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class NearbyBiz {

    /** 2017-09-01 毫秒值/1000 (秒) **/
    private static final int BASE_SORT_NUM = 1504195200;

    /** 最大距离 **/
    private static final int MAX_DISTANCE = 3000;

    /** 8小时（秒） **/
    private static final int EIGHT_HOUR_SECOND = 60 * 60 * 8;

    /** 附近的人缓存key值，p1-城市编号，p2-地区编号 **/
    private static final String NEARBY_CACHE_KEY = "nearby_%s_%s";

    /** 附近的人用户缓存key值，p1-城市编号，p2-地区编号,p3-用户id **/
    private static final String NEARBY_USER_CACHE_KEY = "nearby_user_%s_%s_%s";

    @Autowired
    private RedisDao redisDao;

    // 线程池
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // 附近的人
    public Result<List<NearbyBO>> nearby(NearbyPO paramObj) {
        int nowSortNum = (int) (new Date().getTime() / 1000);
        // 此处仅为了减低排序的序号（ 获取缓存集合最大排序下标）
        int endIndex = nowSortNum - BASE_SORT_NUM;

        // 缓存key值
        String cacheKey = String.format(NEARBY_CACHE_KEY, paramObj.getCityCode(), paramObj.getAdCode());

        // 取同一城市地区&&八小时区间范围数据(八小时之前缓存数据会删除)
        Set<String> redisNearby = redisDao.getSetByKeyAndScore(cacheKey, endIndex - EIGHT_HOUR_SECOND, endIndex);

        // 开启新线程写入数据(让主线程“专心”处理主业务)
        threadPoolTaskExecutor.execute(new InsertCache(paramObj, cacheKey, endIndex));

        if (redisNearby.size() == 0)
            return new Result<List<NearbyBO>>(false, "附近查无用户", null);

        List<NearbyBO> result = new ArrayList<NearbyBO>(redisNearby.size());
        boolean oneself = true;
        for (String item : redisNearby) {
            NearbyPO cacheNearby = JSONObject.parseObject(item, NearbyPO.class);
            // 缓存里可能有用户自己
            if (cacheNearby.getId().intValue() == paramObj.getId())
                continue;
            double distance = countDistance(paramObj.getLongitude(), paramObj.getLatitude(), cacheNearby.getLongitude(),
                    cacheNearby.getLatitude());
            // 大于限定距离
            if (distance > MAX_DISTANCE)
                continue;
            result.add(new NearbyBO(cacheNearby.getId(), cacheNearby.getName(), distance));
            oneself = false;
        }
        if (oneself)
            return new Result<List<NearbyBO>>(false, "附近查无用户", null);
        return new Result<List<NearbyBO>>(true, "获取成功", result);
    }

    // 把用户定位信息写入缓存
    private class InsertCache implements Runnable {
        // 用户提交的最新坐标信息
        private NearbyPO paramObj;
        // “附近的人”缓存集合key
        private String cacheKey;
        // 获取缓存集合最大排序下标
        private Integer endIndex;

        public InsertCache(NearbyPO paramObj, String cacheKey, Integer endIndex) {
            this.paramObj = paramObj;
            this.cacheKey = cacheKey;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            String userCacheKey = String.format(NEARBY_USER_CACHE_KEY, paramObj.getCityCode(), paramObj.getAdCode(),
                    paramObj.getId());
            String cacheNewData = JSONObject.toJSONString(paramObj);
            String cacheUserPosition = redisDao.getOneStringByKey(userCacheKey);
            // 确保用户坐标信息缓存清除慢于“附近的人”坐标信息
            redisDao.setOneStringByKey(userCacheKey, cacheNewData, EIGHT_HOUR_SECOND + 60);

            // 保存用户坐标信息至“附近的人”缓存集合
            redisDao.addOneStringToZSet(cacheKey, cacheNewData, cacheUserPosition, endIndex);
        }

    }

    /**
     * 计算两经纬度点之间的距离（单位：米）
     *
     * @param longitude1
     *            坐标1经度
     * @param latitude1
     *            坐标1纬度
     * @param longitude2
     *            坐标2经度
     * @param latitude2
     *            坐标1纬度
     * @return
     */
    private static double countDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double radLat1 = Math.toRadians(latitude1);
        double radLat2 = Math.toRadians(latitude2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(longitude1) - Math.toRadians(longitude2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}