package com.hotgroup.commons.core.enums;

/**
 * 业务操作类型
 *
 * @author Lzw
 */
public enum BusinessType {
    /**
     * 其它
     */
    OTHER(0),

    /**
     * 新增
     */
    INSERT(1),

    /**
     * 修改
     */
    UPDATE(2),

    /**
     * 删除
     */
    DELETE(3),

    /**
     * 授权
     */
    GRANT(4),

    /**
     * 导出
     */
    EXPORT(5),

    /**
     * 导入
     */
    IMPORT(6),

    /**
     * 强退
     */
    FORCE(7),

    /**
     * 生成代码
     */
    GENCODE(8),

    /**
     * 清空数据
     */
    CLEAN(9),

    /**
     * 初始化
     */
    INIT(10),

    /**
     * 同步
     */
    SYNC(11);

    /**
     *
     */
    private int code;

    private BusinessType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
