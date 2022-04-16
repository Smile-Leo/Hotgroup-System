package com.hotgroup.commons.database.typehandler;

/**
 * @author Lzw
 */
public class LongArrayTypeHandler extends StringTokenizerTypeHandler<Long> {

    public LongArrayTypeHandler() {
        super(Long.class);
    }

    @Override
    Long parseString(String value) {
        return Long.parseLong(value);
    }

}
