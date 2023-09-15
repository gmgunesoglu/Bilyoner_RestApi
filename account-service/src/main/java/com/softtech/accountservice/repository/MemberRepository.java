package com.softtech.accountservice.repository;

import com.softtech.accountservice.entity.Member;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUserNameOrEmail(String userNameOrEmail, String userNameOrEmail1);

    Member findByUserName(String userName);

    Member findByEmail(String email);

    @Query("SELECT true from Member m WHERE m.email = :email")
    Boolean checkEmailInUse(String email);

    @Query("SELECT true from Member m WHERE m.userName = :userName")
    Boolean checkUserNameInUse(String userName);
}
