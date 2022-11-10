package com.mysterin;

import com.mysterin.config.SmallqConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/10 11:20
 */
@Slf4j
public class SmallqServer {

    public static void main(String[] args) {
        SmallqConfig smallqConfig = new SmallqConfig();
        log.debug("config={}", smallqConfig);
    }
}
