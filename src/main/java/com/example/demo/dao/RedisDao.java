package com.example.demo.dao;

import java.util.Set;

public interface RedisDao {

    /**
     *
     * 根据key值获取String
     *
     * @param key
     * @return
     */
    public String getOneStringByKey(String key);

    /**
     *
     * 缓存一个String
     *
     * @param key
     * @param value
     * @param timeoutSeconds
     */
    public void setOneStringByKey(String key, String value, int timeoutSeconds);

    /**
     * 在获取元素下标区间之外的元素会被删除
     *
     * @param key
     * @param beginScore
     *            获取元素的排序开始下标
     * @param endScore
     *            获取元素的排序结束下标
     * @return 指定排序下标范围内的元素
     */
    public Set<String> getSetByKeyAndScore(String key, int beginScore, int endScore);

    /**
     *
     * @param key
     * @param newVal
     *            新值
     * @param oldVal
     *            旧值（非空则删除元素）
     * @param score
     *            排序（使用时间基准值来判断是否删除元素）
     */
    public void addOneStringToZSet(String key, String newVal, String oldVal, double score);

}