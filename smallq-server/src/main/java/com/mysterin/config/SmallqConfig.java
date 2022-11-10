package com.mysterin.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/10 11:21
 */
@Slf4j
@Data
public class SmallqConfig {

    private Integer port;

    private String serverName;

    /**
     * 配置文件
     */
    private static final String CONFIG_FILE = "config.properties";

    public SmallqConfig() {
        try {
            init();
        } catch (Exception e) {
            log.error("初始化配置异常", e);
            throw new RuntimeException(e);
        }
    }

    public void init() throws IOException, IllegalAccessException {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FILE);
        Properties properties = new Properties();
        properties.load(inputStream);
        populate(properties);
    }

    /**
     * 填充属性
     * @param properties
     * @throws IllegalAccessException
     */
    public void populate(Properties properties) throws IllegalAccessException {
        Class<? extends SmallqConfig> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String value = properties.getProperty(name);
            if (Objects.isNull(value)) {
                continue;
            }
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == Integer.class) {
                field.set(this, Integer.valueOf(value));
            } else {
                field.set(this, value);
            }
        }
    }
}
