package com.hotgroup.commons.database.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Long转String;
 * <p>
 * 只有Long的值大于9007199254740992才会转换为字符串
 *
 * @author Lzw
 */
public class JacksonLongToStringSerializer extends StdSerializer<Long> {

    public static final JacksonLongToStringSerializer instance = new JacksonLongToStringSerializer(Long.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * js支持的最大数
     */
    private final static long LOW = -9007199254740991L;

    private final static long HIGH = 9007199254740991L;

    protected JacksonLongToStringSerializer(Class<Long> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            if (value > HIGH || value < LOW) {
                gen.writeString(value.toString());
            } else {
                gen.writeNumber(value);
            }
        }
    }

}
