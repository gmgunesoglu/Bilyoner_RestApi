package com.softtech.accountservice.entity;

import com.softtech.accountservice.dto.TransactionCouponType;
import com.softtech.accountservice.dto.TransactionMoveType;

public enum TransactionType {
    OPEN_ACCOUNT,
    DEPOSIT_AMOUNT,
    COUPON_PURCHASE,
    COUPON_CANCEL,
    COUPON_WIN,
    WITHDRAW_AMOUNT,
    CLOSE_ACCOUNT;

    public static TransactionType fromTransactionCouponType(TransactionCouponType couponType) {
        switch (couponType) {
            case COUPON_PURCHASE:
                return COUPON_PURCHASE;
            case COUPON_CANCEL:
                return COUPON_CANCEL;
            case COUPON_WIN:
                return COUPON_WIN;
            default:
                throw new IllegalArgumentException("Invalid TransactionCouponType: " + couponType);
        }
    }

    public static TransactionType fromTransactionMoveType(TransactionMoveType couponType) {
        switch (couponType) {
            case DEPOSIT_AMOUNT:
                return DEPOSIT_AMOUNT;
            case WITHDRAW_AMOUNT:
                return WITHDRAW_AMOUNT;
            default:
                throw new IllegalArgumentException("Invalid TransactionCouponType: " + couponType);
        }
    }
}
