package com.board.couponcore.repository.mysql;

import com.board.couponcore.model.Coupon;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

}
