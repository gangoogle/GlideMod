package com.gangoogle.glide.load.engine;

/**
 * @author zgyi
 * @date 2019/6/21
 * @Description: 自定义key
 */

public class BaseKey {
    private String key;

    public BaseKey(String key) {
        this.key = key;
    }

    /**
     * 获取自定义Key
     *
     * @return
     */
    public String getKey() {
        return this.key;
    }
}
