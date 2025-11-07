DROP TABLE comments;

DROP TABLE posts;

DROP TABLE category;

DROP TABLE main_category;

DROP TABLE members;

CREATE TABLE members (
   email             VARCHAR2(100) PRIMARY KEY,
   pwd               VARCHAR2(50) NOT NULL,
   nickname          VARCHAR2(50) NOT NULL UNIQUE,
   grade             NUMBER NOT NULL,
   nick_changed_date DATE NULL,
   reg_date          DATE NOT NULL,
   point             NUMBER NULL
);

CREATE TABLE main_category (
   maincategory_id   NUMBER PRIMARY KEY,
   maincategory_name VARCHAR2(30) NOT NULL UNIQUE
);

CREATE TABLE category (
   category_id     NUMBER PRIMARY KEY,
   category_name   VARCHAR2(30) NOT NULL UNIQUE,
   maincategory_id NUMBER
      REFERENCES main_category ( maincategory_id )
         ON DELETE CASCADE
);

CREATE TABLE posts (
   post_id               NUMBER PRIMARY KEY,
   title                 VARCHAR2(100) NOT NULL,
   content               CLOB NOT NULL,
   category_id           NUMBER
      REFERENCES category ( category_id )
         ON DELETE CASCADE,
   view_count            NUMBER NOT NULL,
   recommendations_count NUMBER NOT NULL,
   create_at             DATE NOT NULL,
   member_email          VARCHAR2(100)
      REFERENCES members ( email )
         ON DELETE CASCADE
);

CREATE TABLE comments (
   comment_id   NUMBER PRIMARY KEY,
   post_id      NUMBER
      REFERENCES posts ( post_id )
         ON DELETE CASCADE,
   member_email VARCHAR2(100)
      REFERENCES members ( email )
         ON DELETE CASCADE,
   content      CLOB NOT NULL,
   create_at    DATE NOT NULL,
   reg_date     DATE NOT NULL,
   point        NUMBER NOT NULL
);

INSERT INTO main_category (
   maincategory_id,
   maincategory_name
) VALUES ( 1,
           '꿀팁' );

INSERT INTO main_category (
   maincategory_id,
   maincategory_name
) VALUES ( 2,
           '질문' );

INSERT INTO main_category (
   maincategory_id,
   maincategory_name
) VALUES ( 3,
           '후기' );

INSERT INTO main_category (
   maincategory_id,
   maincategory_name
) VALUES ( 4,
           '잡담' );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 1,
           '자취요리레시피',
           1 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 2,
           '방 청소 노하우',
           1 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 3,
           '좋은 매물 구하기',
           1 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 4,
           '집 꾸미기',
           1 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 5,
           '첫 자취생활 질문',
           2 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 6,
           '방 운영규칙 질문',
           2 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 7,
           '맛집 후기',
           3 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 8,
           '자취용품 후기',
           3 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 9,
           '동네 인프라 후기',
           3 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 10,
           '아이돌',
           4 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 11,
           '운동',
           4 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 12,
           '축제',
           4 );

INSERT INTO category (
   category_id,
   category_name,
   maincategory_id
) VALUES ( 13,
           '취미생활',
           4 );

INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user01@example.com',
           'pwd01',
           '요리왕',
           1,
           NULL,
           TO_DATE('2025-06-01','YYYY-MM-DD'),
           120 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user02@example.com',
           'pwd02',
           '청소요정',
           2,
           TO_DATE('2025-10-08','YYYY-MM-DD'),
           TO_DATE('2025-03-21','YYYY-MM-DD'),
           80 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user03@example.com',
           'pwd03',
           '매물헌터',
           3,
           NULL,
           TO_DATE('2025-05-10','YYYY-MM-DD'),
           95 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user04@example.com',
           'pwd04',
           '인테리어러버',
           1,
           TO_DATE('2025-10-28','YYYY-MM-DD'),
           TO_DATE('2025-08-09','YYYY-MM-DD'),
           60 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user05@example.com',
           'pwd05',
           '자취초보',
           2,
           NULL,
           TO_DATE('2024-12-01','YYYY-MM-DD'),
           40 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user06@example.com',
           'pwd06',
           '규칙지킴이',
           3,
           TO_DATE('2025-10-30','YYYY-MM-DD'),
           TO_DATE('2025-09-08','YYYY-MM-DD'),
           70 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user07@example.com',
           'pwd07',
           '맛집탐험가',
           1,
           NULL,
           TO_DATE('2025-04-11','YYYY-MM-DD'),
           110 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user08@example.com',
           'pwd08',
           '용품리뷰어',
           2,
           TO_DATE('2025-10-18','YYYY-MM-DD'),
           TO_DATE('2025-02-28','YYYY-MM-DD'),
           90 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user09@example.com',
           'pwd09',
           '인프라분석가',
           3,
           NULL,
           TO_DATE('2025-08-24','YYYY-MM-DD'),
           55 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user10@example.com',
           'pwd10',
           '아이돌덕후',
           1,
           TO_DATE('2025-10-23','YYYY-MM-DD'),
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           100 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user11@example.com',
           'pwd11',
           '운동매니아',
           2,
           NULL,
           TO_DATE('2025-01-01','YYYY-MM-DD'),
           85 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user12@example.com',
           'pwd12',
           '축제러버',
           3,
           TO_DATE('2025-09-28','YYYY-MM-DD'),
           TO_DATE('2025-05-21','YYYY-MM-DD'),
           75 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user13@example.com',
           'pwd13',
           '취미왕',
           1,
           NULL,
           TO_DATE('2025-08-09','YYYY-MM-DD'),
           65 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user14@example.com',
           'pwd14',
           '요리초보',
           2,
           TO_DATE('2025-10-13','YYYY-MM-DD'),
           TO_DATE('2025-03-09','YYYY-MM-DD'),
           50 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user15@example.com',
           'pwd15',
           '청소고수',
           3,
           NULL,
           TO_DATE('2025-06-20','YYYY-MM-DD'),
           95 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user16@example.com',
           'pwd16',
           '매물전문가',
           1,
           TO_DATE('2025-10-26','YYYY-MM-DD'),
           TO_DATE('2025-09-18','YYYY-MM-DD'),
           105 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user17@example.com',
           'pwd17',
           '인테리어초보',
           2,
           NULL,
           TO_DATE('2024-10-01','YYYY-MM-DD'),
           45 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user18@example.com',
           'pwd18',
           '자취고수',
           3,
           TO_DATE('2025-10-31','YYYY-MM-DD'),
           TO_DATE('2025-05-29','YYYY-MM-DD'),
           115 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user19@example.com',
           'pwd19',
           '규칙전문가',
           1,
           NULL,
           TO_DATE('2025-08-29','YYYY-MM-DD'),
           90 );
INSERT INTO members (
   email,
   pwd,
   nickname,
   grade,
   nick_changed_date,
   reg_date,
   point
) VALUES ( 'user20@example.com',
           'pwd20',
           '잡담러버',
           2,
           TO_DATE('2025-10-20','YYYY-MM-DD'),
           TO_DATE('2025-04-30','YYYY-MM-DD'),
           100 );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 1,
           '간단한 계란말이 레시피',
           '계란 3개, 소금 약간, 파 송송 썰어 넣고...',
           1,
           120,
           15,
           TO_DATE('2025-06-02','YYYY-MM-DD'),
           'user01@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 2,
           '청소할 때 유용한 팁',
           '베이킹소다와 식초를 활용하면...',
           2,
           85,
           10,
           TO_DATE('2025-06-05','YYYY-MM-DD'),
           'user02@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 3,
           '좋은 매물 찾는 법',
           '직방, 다방 앱 활용법과 지역 필터링 팁...',
           3,
           150,
           20,
           TO_DATE('2025-06-10','YYYY-MM-DD'),
           'user03@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 4,
           '방 분위기 바꾸는 인테리어 소품',
           '조명 하나로 분위기가 확 바뀌어요...',
           4,
           60,
           5,
           TO_DATE('2025-06-12','YYYY-MM-DD'),
           'user04@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 5,
           '자취 초보의 첫 질문',
           '세탁기 돌릴 때 세제는 얼마나 넣나요?',
           5,
           40,
           2,
           TO_DATE('2025-06-15','YYYY-MM-DD'),
           'user05@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 6,
           '공동생활 규칙 공유',
           '밤 10시 이후 조용히 하기, 쓰레기 분리 철저히...',
           6,
           70,
           8,
           TO_DATE('2025-06-18','YYYY-MM-DD'),
           'user06@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 7,
           '홍대 맛집 후기',
           '홍대 OO돈까스 정말 맛있어요. 바삭하고 육즙 가득...',
           7,
           200,
           25,
           TO_DATE('2025-06-20','YYYY-MM-DD'),
           'user07@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 8,
           '자취용품 추천 후기',
           '무선 청소기 진짜 편해요. 강추!',
           8,
           90,
           12,
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           'user08@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 9,
           '동네 인프라 후기',
           '편의점, 세탁소, 병원까지 도보 5분 거리라 만족해요.',
           9,
           75,
           9,
           TO_DATE('2025-06-25','YYYY-MM-DD'),
           'user09@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 10,
           '최애 아이돌 콘서트 후기',
           '현장에서 본 무대는 정말 감동적이었어요...',
           10,
           300,
           40,
           TO_DATE('2025-06-28','YYYY-MM-DD'),
           'user10@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 11,
           '운동 루틴 공유',
           '아침에 30분 스트레칭과 저녁에 근력운동...',
           11,
           65,
           6,
           TO_DATE('2025-07-01','YYYY-MM-DD'),
           'user11@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 12,
           '축제 후기: OO페스티벌',
           '사람 많았지만 분위기 최고였어요!',
           12,
           180,
           22,
           TO_DATE('2025-07-03','YYYY-MM-DD'),
           'user12@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 13,
           '취미생활 추천: 드로잉',
           '퇴근 후 드로잉하면 스트레스가 풀려요.',
           13,
           55,
           4,
           TO_DATE('2025-07-05','YYYY-MM-DD'),
           'user13@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 14,
           '자취요리 실패담',
           '계란찜 하다가 폭발했어요... 물 조절 중요!',
           1,
           95,
           11,
           TO_DATE('2025-07-07','YYYY-MM-DD'),
           'user14@example.com' );

INSERT INTO posts (
   post_id,
   title,
   content,
   category_id,
   view_count,
   recommendations_count,
   create_at,
   member_email
) VALUES ( 15,
           '운동하다 생긴 에피소드',
           '헬스장에서 덤벨 떨어뜨려서 민망했어요...',
           11,
           40,
           3,
           TO_DATE('2025-07-09','YYYY-MM-DD'),
           'user11@example.com' );

INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 1,
           1,
           'user02@example.com',
           '이 레시피 진짜 유용하네요!',
           TO_DATE('2025-06-03','YYYY-MM-DD'),
           TO_DATE('2025-06-03','YYYY-MM-DD'),
           10 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 2,
           1,
           'user05@example.com',
           '계란말이 실패했는데 이건 성공했어요!',
           TO_DATE('2025-06-04','YYYY-MM-DD'),
           TO_DATE('2025-06-04','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 3,
           2,
           'user01@example.com',
           '청소 꿀팁 감사합니다!',
           TO_DATE('2025-06-06','YYYY-MM-DD'),
           TO_DATE('2025-06-06','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 4,
           2,
           'user06@example.com',
           '베이킹소다 활용법 좋네요.',
           TO_DATE('2025-06-07','YYYY-MM-DD'),
           TO_DATE('2025-06-07','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 5,
           3,
           'user04@example.com',
           '매물 찾기 어렵던데 도움됐어요.',
           TO_DATE('2025-06-11','YYYY-MM-DD'),
           TO_DATE('2025-06-11','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 6,
           3,
           'user07@example.com',
           '지역 필터링 팁 유용해요.',
           TO_DATE('2025-06-11','YYYY-MM-DD'),
           TO_DATE('2025-06-11','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 7,
           3,
           'user10@example.com',
           '좋은 정보 감사합니다.',
           TO_DATE('2025-06-12','YYYY-MM-DD'),
           TO_DATE('2025-06-12','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 8,
           4,
           'user03@example.com',
           '조명 추천 좀 해주세요!',
           TO_DATE('2025-06-13','YYYY-MM-DD'),
           TO_DATE('2025-06-13','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 9,
           4,
           'user08@example.com',
           '분위기 바꾸기 좋은 팁이에요.',
           TO_DATE('2025-06-13','YYYY-MM-DD'),
           TO_DATE('2025-06-13','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 10,
           5,
           'user06@example.com',
           '세제 양은 보통 뚜껑 기준이에요.',
           TO_DATE('2025-06-16','YYYY-MM-DD'),
           TO_DATE('2025-06-16','YYYY-MM-DD'),
           4 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 11,
           6,
           'user01@example.com',
           '규칙 공유 감사합니다.',
           TO_DATE('2025-06-19','YYYY-MM-DD'),
           TO_DATE('2025-06-19','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 12,
           6,
           'user09@example.com',
           '공동생활에 꼭 필요한 내용이네요.',
           TO_DATE('2025-06-19','YYYY-MM-DD'),
           TO_DATE('2025-06-19','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 13,
           7,
           'user02@example.com',
           '홍대 OO돈까스 저도 좋아해요!',
           TO_DATE('2025-06-21','YYYY-MM-DD'),
           TO_DATE('2025-06-21','YYYY-MM-DD'),
           10 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 14,
           7,
           'user11@example.com',
           '줄이 길어서 못 먹었는데 다음엔 도전!',
           TO_DATE('2025-06-21','YYYY-MM-DD'),
           TO_DATE('2025-06-21','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 15,
           7,
           'user13@example.com',
           '사진도 있으면 좋겠어요.',
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 16,
           8,
           'user04@example.com',
           '무선 청소기 브랜드 뭐 쓰세요?',
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 17,
           8,
           'user14@example.com',
           '저도 강추합니다!',
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 18,
           9,
           'user05@example.com',
           '인프라 좋은 동네 추천해주세요.',
           TO_DATE('2025-06-26','YYYY-MM-DD'),
           TO_DATE('2025-06-26','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 19,
           9,
           'user12@example.com',
           '편의점 많은 동네가 좋아요.',
           TO_DATE('2025-06-26','YYYY-MM-DD'),
           TO_DATE('2025-06-26','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 20,
           10,
           'user06@example.com',
           '콘서트 후기 너무 좋아요!',
           TO_DATE('2025-06-29','YYYY-MM-DD'),
           TO_DATE('2025-06-29','YYYY-MM-DD'),
           10 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 21,
           10,
           'user15@example.com',
           '현장 분위기 전해져요.',
           TO_DATE('2025-06-29','YYYY-MM-DD'),
           TO_DATE('2025-06-29','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 22,
           10,
           'user17@example.com',
           '사진도 공유해주세요!',
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 23,
           11,
           'user08@example.com',
           '운동 루틴 참고할게요.',
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 24,
           11,
           'user10@example.com',
           '근력운동 추천 루틴도 알려주세요.',
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 25,
           11,
           'user12@example.com',
           '스트레칭 중요하죠!',
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 26,
           12,
           'user01@example.com',
           '축제 분위기 최고였어요',
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           TO_DATE('2025-07-02','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 26,
           12,
           'user01@example.com',
           '축제 분위기 최고였어요!',
           TO_DATE('2025-07-04','YYYY-MM-DD'),
           TO_DATE('2025-07-04','YYYY-MM-DD'),
           10 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 27,
           12,
           'user14@example.com',
           '사람 많았지만 재밌었어요.',
           TO_DATE('2025-07-04','YYYY-MM-DD'),
           TO_DATE('2025-07-04','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 28,
           12,
           'user15@example.com',
           '내년에도 꼭 갈 거예요!',
           TO_DATE('2025-07-05','YYYY-MM-DD'),
           TO_DATE('2025-07-05','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 29,
           13,
           'user02@example.com',
           '드로잉 재밌죠! 저도 시작했어요.',
           TO_DATE('2025-07-06','YYYY-MM-DD'),
           TO_DATE('2025-07-06','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 30,
           13,
           'user06@example.com',
           '퇴근 후 힐링에 최고!',
           TO_DATE('2025-07-06','YYYY-MM-DD'),
           TO_DATE('2025-07-06','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 31,
           13,
           'user09@example.com',
           '도구 추천도 해주세요.',
           TO_DATE('2025-07-07','YYYY-MM-DD'),
           TO_DATE('2025-07-07','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 32,
           14,
           'user03@example.com',
           '저도 계란찜 실패했어요 ㅋㅋ',
           TO_DATE('2025-07-08','YYYY-MM-DD'),
           TO_DATE('2025-07-08','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 33,
           14,
           'user07@example.com',
           '물 조절 진짜 중요하죠.',
           TO_DATE('2025-07-08','YYYY-MM-DD'),
           TO_DATE('2025-07-08','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 34,
           14,
           'user11@example.com',
           '실패담 공유 감사합니다!',
           TO_DATE('2025-07-08','YYYY-MM-DD'),
           TO_DATE('2025-07-08','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 35,
           14,
           'user13@example.com',
           '다음엔 성공하실 거예요!',
           TO_DATE('2025-07-09','YYYY-MM-DD'),
           TO_DATE('2025-07-09','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 36,
           15,
           'user04@example.com',
           '헬스장 에피소드 공감돼요.',
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           10 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 37,
           15,
           'user05@example.com',
           '민망한 순간 많죠 ㅋㅋ',
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 38,
           15,
           'user06@example.com',
           '덤벨 조심하세요!',
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 39,
           15,
           'user07@example.com',
           '운동 중 사고 조심!',
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 40,
           15,
           'user08@example.com',
           '다치지 않게 주의하세요!',
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           TO_DATE('2025-07-10','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 41,
           10,
           'user12@example.com',
           '콘서트 후기 너무 좋아요!',
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           10 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 42,
           10,
           'user13@example.com',
           '현장 분위기 전해져요.',
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           9 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 43,
           10,
           'user14@example.com',
           '사진도 공유해주세요!',
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           TO_DATE('2025-06-30','YYYY-MM-DD'),
           8 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 44,
           7,
           'user15@example.com',
           '돈까스 맛있죠!',
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           7 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 45,
           7,
           'user16@example.com',
           '홍대 맛집 정보 감사합니다.',
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 46,
           7,
           'user17@example.com',
           '다음엔 꼭 가볼게요!',
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           TO_DATE('2025-06-22','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 47,
           8,
           'user18@example.com',
           '무선 청소기 추천 감사합니다.',
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           6 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 48,
           8,
           'user19@example.com',
           '브랜드 정보도 알려주세요!',
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           5 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 49,
           8,
           'user20@example.com',
           '청소기 성능 궁금해요.',
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           4 );
INSERT INTO comments (
   comment_id,
   post_id,
   member_email,
   content,
   create_at,
   reg_date,
   point
) VALUES ( 50,
           8,
           'user01@example.com',
           '자취용품 후기 좋아요!',
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           TO_DATE('2025-06-23','YYYY-MM-DD'),
           3 );

COMMIT;