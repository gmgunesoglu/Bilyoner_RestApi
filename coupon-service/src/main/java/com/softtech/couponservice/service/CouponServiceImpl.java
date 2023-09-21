package com.softtech.couponservice.service;


import com.softtech.couponservice.client.AccountClient;
import com.softtech.couponservice.dto.*;
import com.softtech.couponservice.entity.Coupon;
import com.softtech.couponservice.entity.CouponTransaction;
import com.softtech.couponservice.entity.CouponTransactionType;
import com.softtech.couponservice.entity.EventCoupon;
import com.softtech.couponservice.exceptionhandling.GlobalRuntimeException;
import com.softtech.couponservice.repository.CouponRepository;
import com.softtech.couponservice.repository.CouponTransactionRepository;
import com.softtech.couponservice.repository.EventCouponRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final JwtUtil jwtService;
    private final CouponRepository repository;
    private final EventCouponRepository eventCouponRepository;
    private final AccountClient accountClient;
    private final CouponTransactionRepository couponTransactionRepository;

    private final double minPayAmount=35;

    @Override
    public List<CouponDto> getAll() {
        Date date = new Date(System.currentTimeMillis()+300000);
        List<CouponDto> dtos = repository.getAllCouponDto(date);
        for(CouponDto dto:dtos){
            dto.setTotalMatch(eventCouponRepository.getEventCount(dto.getId()));
        }
        return dtos;
    }

    @Override
    public CouponDetailDto get(Long id) {
        CouponDetailDto dto = repository.getCouponDetailDto(id);
        if(dto==null || dto.getStartDate().before(new Date(System.currentTimeMillis()+300000))){
            throw new GlobalRuntimeException("Coupon not found!", HttpStatus.NOT_FOUND);
        }
        dto.setTotalMatch(eventCouponRepository.getEventCount(dto.getId()));
        dto.setMatches(repository.getMatches(dto.getId()));
        return dto;
    }

    @Override
    public String purchase(HttpServletRequest request, List<CouponPurchaseDto> dtos) {

        // get member id
        String jwt = request.getHeader("Authorization").substring(7);
        Long memberId = jwtService.getId(jwt);

        double payAmount=0;
        List<Coupon> coupons = new ArrayList<>();
        List<CouponTransaction> couponTransactions = new ArrayList<>();
        for(CouponPurchaseDto dto : dtos){

            // check coupon
            Coupon coupon = repository.findById(dto.getCouponId()).get();
            if(coupon == null || coupon.getPayAmount()!=0){
                throw new GlobalRuntimeException("Coupon not found for id: "+coupon.getId(),HttpStatus.NOT_FOUND);
            }if(dto.getPayAmount()<minPayAmount){
                throw new GlobalRuntimeException("Pay amount can't be lower than "+minPayAmount,HttpStatus.BAD_REQUEST);
            }

            payAmount+=dto.getPayAmount();
            CouponTransaction couponTransaction = new CouponTransaction();
            couponTransaction.setDate(new Date());
            couponTransaction.setAmount(dto.getPayAmount());
            couponTransaction.setMemberId(memberId);
            couponTransaction.setCouponId(dto.getCouponId());
            couponTransaction.setCouponTransactionType(CouponTransactionType.COUPON_PURCHASE);
            couponTransactions.add(couponTransaction);
            coupon.setPayAmount(dto.getPayAmount());
            coupons.add(coupon);
        }

        // try to buy
        BalanceTransactionDto clientDto = new BalanceTransactionDto();
        clientDto.setMemberId(memberId);
        clientDto.setTransactionAmount(payAmount);
        clientDto.setTransactionType(TransactionCouponType.COUPON_PURCHASE);
        try{
            Boolean clientResponse = accountClient.balanceTransaction(clientDto);
            if(!clientResponse){
                return "Insufficient amount!";
            }
        }catch (Exception e){
            throw e;
        }

        // update coupons
        for(Coupon c : coupons){
            repository.save(c);
        }

        // add transactions
        for(CouponTransaction ct : couponTransactions){
            couponTransactionRepository.save(ct);
        }

        // return
        return "Purchase is success.";
    }

    @Override
    public String cancel(HttpServletRequest request, Long id) {

        // get member id
        String jwt = request.getHeader("Authorization").substring(7);
        Long memberId = jwtService.getId(jwt);

        // check coupon
        Coupon coupon = repository.findById(id).get();
        if(coupon == null){
            throw new GlobalRuntimeException("Coupon not found for: "+id,HttpStatus.NOT_FOUND);
        }
        if(coupon.getPayAmount()==0){
            throw new GlobalRuntimeException("Coupon not purchased!",HttpStatus.BAD_REQUEST);
        }
        if(!repository.getBuyerIdOfCoupon(coupon.getId()).equals(memberId)){
            throw new GlobalRuntimeException("This coupon is not belong to you!\nCoupon id: "+id,HttpStatus.BAD_REQUEST);
        }
        if(!new Date(System.currentTimeMillis()+300000).before(coupon.getStartDate())){
            throw new GlobalRuntimeException("Coupons 5 minutes until start can't cancel!\nCoupon id: "+id,HttpStatus.BAD_REQUEST);
        }

        CouponTransaction couponTransaction = new CouponTransaction();
        couponTransaction.setDate(new Date());
        couponTransaction.setAmount(coupon.getPayAmount());
        couponTransaction.setMemberId(memberId);
        couponTransaction.setCouponId(id);
        couponTransaction.setCouponTransactionType(CouponTransactionType.COUPON_CANCEL);
        coupon.setPayAmount(0);

        // pay-back
        BalanceTransactionDto clientDto = new BalanceTransactionDto();
        clientDto.setMemberId(memberId);
        clientDto.setTransactionAmount(couponTransaction.getAmount());
        clientDto.setTransactionType(TransactionCouponType.COUPON_CANCEL);
        if(!accountClient.balanceTransaction(clientDto)){
            throw new GlobalRuntimeException("Payback service error!",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // update-save db
        repository.save(coupon);
        couponTransactionRepository.save(couponTransaction);

        // return
        return "Payback is success.";
    }

    @Override
    public List<CouponHistoryDto> getAllPurchased(HttpServletRequest request) {

        // get memberId
        String jwt = request.getHeader("Authorization").substring(7);
        Long memberId = jwtService.getId(jwt);

        // get all purchased coupons


        // create CouponHistoryDto list and return
        return null;
    }

    @Override
    public CouponHistoryDetailDto getPurchased(HttpServletRequest request, Long id) {
        return null;
    }

    @Override
    public void saveCoupons(EventCombinator eventCombinator){
        List<CouponCreateDto> dtos = eventCombinator.getCoupons();
        for(CouponCreateDto dto : dtos){
            Coupon coupon = new Coupon();
            coupon.setStartDate(dto.getStartDate());
            coupon.setRatio(dto.getRatio());
            coupon.setStatue(true);
            coupon = repository.save(coupon);
            for(CombinatorDto event : dto.getEvents()){
                EventCoupon eventCoupon = new EventCoupon();
                eventCoupon.setEventId(event.getId());
                eventCoupon.setCouponId(coupon.getId());
                eventCoupon.setBet(event.getBet());
                eventCouponRepository.save(eventCoupon);
            }
        }
    }

}
