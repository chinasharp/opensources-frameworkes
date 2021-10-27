package org.opensourceframework.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 精度转换FastJson扩展
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class JsLongFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final Long MAX_LONG = 2147483647L;

    public JsLongFastJsonHttpMessageConverter() {
        this.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8)));
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ValueFilter filter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if (value != null && value instanceof Long && (Long)value > MAX_LONG) {
                    return value.toString();
                } else if (value != null && value instanceof List) {
                    List<Object> parseValue = new ArrayList();
                    Iterator iterator = ((List)value).iterator();

                    while(true) {
                        while(iterator.hasNext()) {
                            Object item = iterator.next();
                            if (item instanceof Long && (Long)item > MAX_LONG) {
                                parseValue.add(item.toString());
                            } else {
                                parseValue.add(item);
                            }
                        }

                        return parseValue;
                    }
                } else {
                    return value;
                }
            }
        };
        String text = JSON.toJSONString(obj, filter, this.getFastJsonConfig().getSerializerFeatures());
        byte[] bytes = text.getBytes(UTF8);
        OutputStream out = outputMessage.getBody();
        out.write(bytes);
    }
}
