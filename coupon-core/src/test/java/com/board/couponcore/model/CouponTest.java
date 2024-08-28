package com.board.couponcore.model;

import com.board.couponcore.exception.CouponIssueException;
import com.board.couponcore.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("발급 수량이 남아있다면 true를 반환한다.")
    void availableIssueQuantity_1(){
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssueQuantity();

        Assertions.assertTrue(result);
    }
    @Test
    @DisplayName("발급 수량이 남아있지 않다면 false를 반환한다.")
    void availableIssueQuantity_2(){
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        boolean result = coupon.availableIssueQuantity();

        Assertions.assertFalse(result);
    }
    @Test
    @DisplayName("발급 수량이 설정되지 않았다면 true를 반환한다. ")
    void availableIssueQuantity_3(){
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(99)
                .build();

        boolean result = coupon.availableIssueQuantity();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급 기간이 시작되지 않았다면 false를 반환한다")
    void availableIssueDate_1() {
        // given
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();
        // when
        boolean result = coupon.availableIssueDate();
        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("발급 기간에 해당되면 true를 반환한다")
    void availableIssueDate_2() {
        // given
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();
        // when
        boolean result = coupon.availableIssueDate();
        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급 기간이 종료되면 false를 반환한다")
    void availableIssueDate_3() {
        // given
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();
        // when
        boolean result = coupon.availableIssueDate();
        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량과 발급 기간이 유효하다면 발급에 성공한다")
    void issue_1() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();
        // when
        coupon.issue();
        // then
        Assertions.assertEquals(coupon.getIssuedQuantity(), 100);
    }

    @Test
    @DisplayName("발급 수량을 초과하면 예외를 반환한다")
    void issue_2() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();
        // when & then
        CouponIssueException ex  =  Assertions.assertThrows(CouponIssueException.class, coupon::issue);
        Assertions.assertEquals(ex.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
    }

    @Test
    @DisplayName("발급 기간이 아니면 예외를 반환한다")
    void issue_3() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();
        // when & then
        CouponIssueException ex  =  Assertions.assertThrows(CouponIssueException.class, coupon::issue);
        Assertions.assertEquals(ex.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }





}