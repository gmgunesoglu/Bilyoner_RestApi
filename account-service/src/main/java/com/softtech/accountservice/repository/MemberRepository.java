package com.softtech.accountservice.repository;

import com.softtech.accountservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUserNameOrEmail(String userNameOrEmail, String userNameOrEmail1);
}
