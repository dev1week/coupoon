package com.board.couponcore.repository.mysql;


import com.board.couponcore.model.CouponIssue;
import com.board.couponcore.model.QCoupon;
import com.board.couponcore.model.QCouponIssue;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.board.couponcore.model.QCouponIssue.couponIssue;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {

    private final JPQLQueryFactory queryFactory;


    public CouponIssue findFirstCouponIssue(long couponId, long userId){

        return queryFactory.selectFrom(couponIssue)
                .where(couponIssue.couponId.eq(userId))
                .where(couponIssue.userId.eq(userId))
                .fetchFirst();
    }

}
