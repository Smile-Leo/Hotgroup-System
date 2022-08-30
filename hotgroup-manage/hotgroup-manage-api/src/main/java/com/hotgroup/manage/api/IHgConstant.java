package com.hotgroup.manage.api;

/**
 * @author Lzw
 * @date 2022/6/18.
 */
public interface IHgConstant {

    interface DateStatus {
        int enable = 1;     //启用
        int disable = 0;    //禁用
    }

    interface AuditStatus {
        int await = 0;      //待审核
        int success = 1;    //成功
        int fail = 2;       //失败
    }
}
