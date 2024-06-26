package com.yeoboya.lunch.api.v1.order.repository;


import com.yeoboya.lunch.api.v1.order.domain.GroupOrder;
import com.yeoboya.lunch.api.v1.order.request.GroupOrderSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface GroupOrderRepositoryCustom {

    Slice<GroupOrder> purchaseRecruits(GroupOrderSearch orderSearch, Pageable pageable);

    Slice<GroupOrder> getJoinHistoriesByLoginId(String loginId, Pageable pageable);

//    GroupOrder getJoinHistoryByEmail(Long orderId);
}
