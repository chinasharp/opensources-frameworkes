package org.opensourceframework.demo.helper.protobuf;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户示例
 *
 * @author yu.ce@foxmail.com
 * 
 * @since 1.0.0
 */
@ProtobufClass
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String name;
    private String address;
    private Integer age;
}
