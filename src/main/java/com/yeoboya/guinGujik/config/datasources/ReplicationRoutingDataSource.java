package com.yeoboya.guinGujik.config.datasources;

import com.yeoboya.guinGujik.config.annotation.ExcludeScan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@ExcludeScan
@Slf4j
@RequiredArgsConstructor
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private final DataSourceKey dataSourceKey;

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            log.info("Connection Slave");
            return dataSourceKey.getDefaultSlaveKey();
        } else {
            log.info("Connection Master");
            return dataSourceKey.getMasterKey();
        }
    }
}