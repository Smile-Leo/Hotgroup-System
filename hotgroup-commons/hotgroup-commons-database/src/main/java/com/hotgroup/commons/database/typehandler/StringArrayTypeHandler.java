package com.hotgroup.commons.database.typehandler;

/**
 * @author Lzw
 */
public class StringArrayTypeHandler extends StringTokenizerTypeHandler<String> {

    public StringArrayTypeHandler() {
        super(String.class);
    }

    @Override
    String parseString(String value) {
        return value;
    }

}
