package com.hotgroup.commons.database.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.core.utils.ServletUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页工具类,兼容mybatis-plus跟pagehelper
 *
 * @author Lzw
 */
public class PageHelper {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * @param <T>
     * @param request
     * @return
     */
    public static <T> Page<T> getPage(HttpServletRequest request) {
        Page<T> page = new Page<T>();
        page.setSize(NumberUtils.toInt(request.getParameter(PAGE_SIZE), 10));
        page.setCurrent(NumberUtils.toInt(request.getParameter(PAGE_NUM), 1));
        return page;
    }

    /**
     * 封装分页对象
     */
    public static <T> Page<T> getPage() {
        Page<T> page = new Page<T>();
        page.setSize(ServletUtils.getParameterToInt(PAGE_SIZE, 10));
        page.setCurrent(ServletUtils.getParameterToInt(PAGE_NUM, 1));
        return page;
    }


}
