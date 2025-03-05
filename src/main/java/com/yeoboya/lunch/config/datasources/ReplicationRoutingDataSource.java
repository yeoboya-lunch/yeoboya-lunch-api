package com.yeoboya.lunch.config.datasources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Profile({"dev", "prod"})
@Slf4j
@RequiredArgsConstructor
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private final DataSourceKey dataSourceKey;

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            log.debug("Connection Slave");
            return dataSourceKey.getDefaultSlaveKey();
        } else {
            log.debug("Connection Master");
            return dataSourceKey.getMasterKey();
        }
    }
}
