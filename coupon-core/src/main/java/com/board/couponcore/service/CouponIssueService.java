package com.board.couponcore.service;


import com.board.couponcore.exception.CouponIssueException;
import com.board.couponcore.model.Coupon;
import com.board.couponcore.model.CouponIssue;
import com.board.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.board.couponcore.repository.mysql.CouponIssueRepository;
import com.board.couponcore.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.board.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.board.couponcore.exception.ErrorCode.DUPLICATE_COUPON_ISSUE;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueService couponIssueService;
    private final CouponIssueRepository couponIssueRepository;


    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = findCoupon(couponId);

        coupon.issue();

        saveCouponIssue(couponId, userId);
    }

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId){
        return couponJpaRepository.findById(couponId).orElseThrow(()->{
            throw new CouponIssueException(COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
        });
    }

    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId){
        checkAlreadyIssuance(couponId, userId);
        CouponIssue issue = CouponIssue.builder()
                                        .couponId(couponId)
                                        .userId(userId)
                                        .build();
        return couponIssueJpaRepository.save(issue);
    }

     private void checkAlreadyIssuance(long couponId, long userId){
       CouponIssue issue =  couponIssueRepository.findFirstCouponIssue(couponId, userId);
       if(issue !=null){
           throw new CouponIssueException(DUPLICATE_COUPON_ISSUE,"이미 발급된 쿠폰입니다. user_id: $s coupon_id: $s".formatted(userId, couponId));
       }
     }




}
