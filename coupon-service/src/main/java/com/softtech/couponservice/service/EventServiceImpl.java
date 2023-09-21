package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.CombinatorDto;
import com.softtech.couponservice.dto.CouponCreateDto;
import com.softtech.couponservice.dto.EventCreateDto;
import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.entity.*;
import com.softtech.couponservice.exceptionhandling.GlobalRuntimeException;
import com.softtech.couponservice.repository.CouponRepository;
import com.softtech.couponservice.repository.EventCouponRepository;
import com.softtech.couponservice.repository.EventRepository;
import com.softtech.couponservice.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository repository;

    private final TeamRepository teamRepository;

    private final CouponService couponService;

    private final CouponRepository couponRepository;

    private final EventCouponRepository eventCouponRepository;


    @Override
    public List<EventDetailDto> getAll() {
        Date date = new Date(System.currentTimeMillis()+300000);
        return repository.getEventDetailDtoList(date);
    }

    @Override
    public EventDetailDto get(Long id) {
        EventDetailDto dto =  repository.getEventDetailDto(id);
        if(dto==null){
            throw new GlobalRuntimeException("Event not found!", HttpStatus.NOT_FOUND);
        }
        return dto;
    }

    @Override
    public EventDetailDto add(EventCreateDto dto) {

        // check time
        if(!(new Date(System.currentTimeMillis()+300000)).before(dto.getStartDate())){
            throw new GlobalRuntimeException("Event start date should be later!",HttpStatus.BAD_REQUEST);
        }

        // check teams are same
        if(dto.getAwayTeam().equals(dto.getHomeTeam())){
            throw new GlobalRuntimeException("Home team and away team can't be same!",HttpStatus.BAD_REQUEST);
        }

        // get team ids
        Long homeTeamId = teamRepository.getId(dto.getHomeTeam(), dto.getMatchType());
        Long awayTeamId = teamRepository.getId(dto.getAwayTeam(), dto.getMatchType());
        if(awayTeamId==null || awayTeamId == null){
            throw new GlobalRuntimeException("Team not found!",HttpStatus.NOT_FOUND);
        }


        // check win/lose ratio
        float drawRatio = 1-(dto.getLoseRatio()+dto.getWinRatio());
        if(drawRatio<=0 || drawRatio>=1){
            throw new GlobalRuntimeException("Win/Lose ratios not acceptable!",HttpStatus.BAD_REQUEST);
        }
        float winPoint = calculatePoint(dto.getWinRatio());
        float losePoint = calculatePoint(dto.getLoseRatio());
        float drawPoint = calculatePoint(drawRatio);

        // create event
        Event event = new Event();
        event.setMatchType(dto.getMatchType());
        event.setStartDate(dto.getStartDate());
        event.setHomeTeamId(homeTeamId);
        event.setAwayTeamId(awayTeamId);
        event.setMbsPoint(dto.getMbsPoint());
        event.setWinRatio(dto.getWinRatio());
        event.setLoseRatio(dto.getLoseRatio());
        event.setWinPoint(winPoint);
        event.setDrawPoint(drawPoint);
        event.setLosePoint(losePoint);
        event.setStatue(true);
        event = repository.save(event);

        // create coupons
        eventCombinator(event);

        // create dto and return
        EventDetailDto rDto = new EventDetailDto();
        rDto.setId(event.getId());
        rDto.setMatchType(event.getMatchType());
        rDto.setStartDate(event.getStartDate());
        rDto.setHomeTeam(dto.getHomeTeam());
        rDto.setAwayTeam(dto.getAwayTeam());
        rDto.setMbsPoint(dto.getMbsPoint());
        rDto.setWinPoint(event.getWinPoint());
        rDto.setDrawPoint(event.getDrawPoint());
        rDto.setLosePoint(event.getLosePoint());
        return rDto;
    }

    @Override
    @Transactional
    public String disable(Long id) {

        // check event
        Event event = repository.findById(id).get();
        if(event == null){
            throw new GlobalRuntimeException("Event not found!",HttpStatus.NOT_FOUND);
        }
        if(!event.isStatue()){
            throw new GlobalRuntimeException("Event is already disabled!",HttpStatus.BAD_REQUEST);
        }
        if(!new Date(System.currentTimeMillis()+300000).before(event.getStartDate())){
            throw new GlobalRuntimeException("Events 5 minutes until start can't remove!",HttpStatus.BAD_REQUEST);
        }

        // remove event from relating coupons and save
        List<Coupon> coupons = repository.getRelatingCoupons(event.getId());
        float ratio;
        EventCoupon eventCoupon;
        for(Coupon coupon : coupons){
            MatchResultType matchResultType = eventCouponRepository.getMatchResultType(id,coupon.getId());
            if(matchResultType==MatchResultType.WIN){
                ratio = event.getWinPoint();
            }else if(matchResultType==MatchResultType.DRAW){
                ratio = event.getDrawPoint();
            }else{
                ratio = event.getLosePoint();
            }
            coupon.setRatio(coupon.getRatio()/ratio);
            couponRepository.save(coupon);
        }
        eventCouponRepository.deleteAllByEventId(id);
        event.setStatue(false);
        repository.save(event);

        // return
        return "Event removed!";
    }

    private void eventCombinator(Event event){

        // create CombinatorDto
        CombinatorDto newEvent = new CombinatorDto();
        newEvent.setId(event.getId());
        newEvent.setMbsPoint(event.getMbsPoint());
        newEvent.setWinPoint(event.getWinPoint());
        newEvent.setDrawPoint(event.getDrawPoint());
        newEvent.setLosePoint(event.getLosePoint());
        newEvent.setStartDate(event.getStartDate());

        // create CombinatorDto list
        Date date = new Date(System.currentTimeMillis()+300000);
        List<CombinatorDto> dtos;
        EventCombinator eventCombinations;
        if(event.getMatchType() == MatchType.FOOTBALL){
            dtos = repository.getCombinatorDtoListWithMatchType(event.getId(), date,MatchType.TENNIS);
        }else if(event.getMatchType() == MatchType.TENNIS){
            dtos = repository.getCombinatorDtoListWithMatchType(event.getId(), date,MatchType.FOOTBALL);
        }else{

            // for basketball+football
            dtos = repository.getCombinatorDtoListWithMatchType(event.getId(), date,MatchType.TENNIS);

            // create Combinator
            eventCombinations = new EventCombinator(newEvent,dtos);

            // sent Combinator to couponService
            couponService.saveCoupons(eventCombinations);

            // for basketball+tennis
            dtos = repository.getCombinatorDtoListWithMatchType(event.getId(), date,MatchType.FOOTBALL);
        }



        // create Combinator
        eventCombinations = new EventCombinator(newEvent,dtos);

        // sent Combinator to couponService
        couponService.saveCoupons(eventCombinations);
    }

    private float calculatePoint(float ratio){
        return (0.35f/ratio)+0.65f;
    }
}