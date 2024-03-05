package com.yeoboya.lunch.config.security.event;

import com.yeoboya.lunch.config.security.metaDataSource.UrlSecurityMetadataSource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigChangeListener implements ApplicationListener<SecurityConfigChangeEvent> {

    private final UrlSecurityMetadataSource urlSecurityMetadataSource;

    public SecurityConfigChangeListener(UrlSecurityMetadataSource urlSecurityMetadataSource) {
        this.urlSecurityMetadataSource = urlSecurityMetadataSource;
    }

    @Override
    public void onApplicationEvent(SecurityConfigChangeEvent event) {
        try {
            urlSecurityMetadataSource.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
