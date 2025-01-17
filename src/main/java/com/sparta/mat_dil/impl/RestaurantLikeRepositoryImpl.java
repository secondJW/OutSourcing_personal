package com.sparta.mat_dil.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.mat_dil.entity.QRestaurantLike;
import com.sparta.mat_dil.entity.RestaurantLike;
import com.sparta.mat_dil.repository.RestaurantLikeRepositoryQuery;
import com.sparta.mat_dil.util.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
@Component
public class RestaurantLikeRepositoryImpl implements RestaurantLikeRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public Collection<RestaurantLike> getAllLikeRestaurant(String accountId, Pageable pageable) {
        QRestaurantLike restaurantLike=QRestaurantLike.restaurantLike;
        List<OrderSpecifier> ORDERS= getAllOrderSpecifiers(pageable);
        return queryFactory.selectFrom(restaurantLike)
                .where(restaurantLike.user.accountId.eq(accountId))
                //orderBy가 List<>를 지원하지 않아 배열로 바꿔준다
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                //Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                Order direction = Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        OrderSpecifier<?> orderCreatedAt = QueryDslUtil.getSortedColumn(direction, QRestaurantLike.restaurantLike, "createdAt");
                        ORDERS.add(orderCreatedAt);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
