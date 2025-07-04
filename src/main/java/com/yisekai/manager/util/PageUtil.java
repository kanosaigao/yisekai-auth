package com.yisekai.manager.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yisekai.manager.domain.Search;

/**
 * 分页工具类
 *
 * @author kano
 */
public class PageUtil {

	public static <T> IPage<T> getPage(Search search) {
		return new Page<T>(search.getCurrent(), search.getSize());
	}
}
