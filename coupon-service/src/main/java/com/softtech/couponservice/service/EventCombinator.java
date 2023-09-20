package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.CombinatorDto;
import com.softtech.couponservice.dto.CouponCreateDto;
import com.softtech.couponservice.entity.MatchResultType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventCombinator {

    private CombinatorDto newEvent;
    private List<CombinatorDto> oldEvents;
    private int start;
    private int end;
    private List<Long> temp;
    private List<List<Long>> combinations;
    private List<CouponCreateDto> coupons;

    public EventCombinator(CombinatorDto newEvent, List<CombinatorDto> oldEvents) {
        this.newEvent = newEvent;
        this.oldEvents = oldEvents;
        start = newEvent.getMbsPoint();
        end = oldEvents.size();
        combinations = new ArrayList<>();
        coupons = new ArrayList<>();
        generate();
        permuteCoupon();
    }

    private void generate(){
        if(start == 1){
            temp=new ArrayList<>();
            temp.add(newEvent.getId());
            combinations.add(temp);
            start++;
        }
        if(start == 2){
            temp=new ArrayList<>();
            temp.add(newEvent.getId());
            generateListWithN2();
        }
        for(start=2;start<=end;start++){
            temp=new ArrayList<>();
            temp.add(newEvent.getId());
            generateListWithBigN(0,start);
        }
    }

    // generates combinations with n>=3
    private void generateListWithBigN(int index, int n){
        while(n > 0 && index+n <= end){
            if(n==1){
                // kaydet
                List<Long> list = new ArrayList<>(temp);
                list.add(oldEvents.get(index).getId());
                combinations.add(list);
            }else{
                temp.add(oldEvents.get(index).getId());
                generateListWithBigN(index+1,n-1);
                temp.remove(temp.size()-1);
            }
            index++;
        }
    }

    private void generateListWithN2(){
        int index = 0;
        while(index < end){
            if(oldEvents.get(index).getMbsPoint()<3){
               List<Long> list= new ArrayList<>(temp);
               list.add(oldEvents.get(index).getId());
               combinations.add(list);
            }
            index++;
        }
    }

    private void permuteCoupon(){
        for(List<Long> eventIds : combinations){
            List<CombinatorDto> events = longListToCombinatorDtoList(eventIds);
            permute(events,0);
        }
    }

    private List<CombinatorDto> longListToCombinatorDtoList(List<Long> eventIds){
        List<CombinatorDto> events = new ArrayList<>();
        for(Long eventId : eventIds){
            if(newEvent.getId().equals(eventId)){
                events.add(newEvent);
            }
            for(CombinatorDto event : oldEvents){
                if(event.getId().equals(eventId)){
                    events.add(event);
                }
            }
        }
        return events;
    }

    private void permute(List<CombinatorDto> events, int index){
        if(index==events.size()){
            addCoupon(events);
            return;
        }
        events.get(index).setBet(MatchResultType.WIN);
        permute(events,index+1);

        events.get(index).setBet(MatchResultType.DRAW);
        permute(events,index+1);

        events.get(index).setBet(MatchResultType.LOSE);
        permute(events,index+1);
    }

    private void addCoupon(List<CombinatorDto> events){
        CouponCreateDto dto = new CouponCreateDto();
        float ratio = 1;
        Date startDate = events.get(0).getStartDate();
        List<CombinatorDto> list = new ArrayList<>();
        for(CombinatorDto event : events){
            CombinatorDto item = new CombinatorDto(
                    event.getId(),
                    event.getMbsPoint(),
                    event.getWinPoint(),
                    event.getDrawPoint(),
                    event.getLosePoint(),
                    event.getStartDate()
            );
            if(event.getBet()==MatchResultType.WIN){
                ratio*=event.getWinPoint();
                item.setBet(MatchResultType.WIN);
            }else if(event.getBet()==MatchResultType.DRAW){
                ratio*=event.getDrawPoint();
                item.setBet(MatchResultType.DRAW);
            }else{
                ratio*=event.getLosePoint();
                item.setBet(MatchResultType.LOSE);
            }
            if(startDate.before(event.getStartDate())){
                startDate=event.getStartDate();
            }
            list.add(item);
        }
        dto.setRatio(ratio);
        dto.setStartDate(startDate);
        dto.setEvents(list);
        coupons.add(dto);
    }

    public List<CouponCreateDto> getCoupons(){
        return coupons;
    }
}
