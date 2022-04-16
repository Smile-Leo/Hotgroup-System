package com.hotgroup.commons.database.typehandler;

/**
 * @author Lzw
 */
public class IntegerArrayTypeHandler extends StringTokenizerTypeHandler<Integer> {

    public IntegerArrayTypeHandler() {
        super(Integer.class);
    }

    @Override
    Integer parseString(String value) {
        return Integer.parseInt(value);
    }

}
