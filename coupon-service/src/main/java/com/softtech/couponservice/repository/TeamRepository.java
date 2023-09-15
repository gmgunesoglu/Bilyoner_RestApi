package com.softtech.couponservice.repository;

import com.softtech.couponservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Event, Long> {
}
