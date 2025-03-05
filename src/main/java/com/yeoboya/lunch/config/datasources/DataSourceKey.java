package com.yeoboya.lunch.config.datasources;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Component
class DataSourceKey {
    private static final String MASTER_KEY = "master";
    private static final String SLAVE_KEY = "slave";
    private static final int DEFAULT_SLAVE_NUMBER = 1;

    public String getMasterKey() {
        return MASTER_KEY;
    }

    public String getSlaveKeyByNumber(int idx) {
        return SLAVE_KEY + idx;
    }

    public String getDefaultSlaveKey() {
        return SLAVE_KEY + DEFAULT_SLAVE_NUMBER;
    }
}