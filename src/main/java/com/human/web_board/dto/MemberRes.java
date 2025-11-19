package com.human.web_board.dto;

import com.human.web_board.Entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberRes {
    private Long id;
    private String email;
    private String pwd;
    private String nickname;  // name → nickname
    private String grade;     // grade 추가
    private LocalDateTime regDate; //날짜
    private int point;
    private String profileImg; // 이미지 추가


    public MemberRes(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.pwd = member.getPassword();
        this.nickname = member.getNickname();
        this.grade = member.getGrade();
        this.regDate = member.getRegDate();
        this.point = member.getPoint();
        this.profileImg = member.getProfileImg();
    }
}
