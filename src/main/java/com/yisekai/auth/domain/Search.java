package com.yisekai.auth.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhengweijie
 * @time 2024/2/5 17:12
 */
@Data
public class Search implements Serializable {
    /**
     * 关键词
     */

    private String keyword;


    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页的数量
     */
    private Integer size = 10;
}
