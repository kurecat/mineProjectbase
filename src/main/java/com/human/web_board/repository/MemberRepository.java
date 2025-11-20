package com.human.web_board.repository;

import com.human.web_board.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


// JpaRepository<다룰 Entity, PK타입>
public interface MemberRepository extends JpaRepository<Member, Long> {
}