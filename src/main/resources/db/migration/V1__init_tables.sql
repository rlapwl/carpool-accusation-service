CREATE TABLE accusations (
  id BIGINT AUTO_INCREMENT NOT NULL,
   member_id VARCHAR(255) NOT NULL COMMENT '신고 등록 회원 ID',
   accusation_status VARCHAR(255) NOT NULL COMMENT '신고 상태',
   result_comment VARCHAR(255) NULL COMMENT '관리자 코멘트',
   created_date_time datetime NULL COMMENT '등록 시간',
   modified_date_time datetime NULL COMMENT '최근 수정된 시간',
   accused_member_id VARCHAR(255) NOT NULL COMMENT '신고대상 ID',
   accused_member_name VARCHAR(255) NOT NULL COMMENT '신고대상 이름',
   accused_member_email VARCHAR(255) NOT NULL COMMENT '신고대상 이메일',
   party_id BIGINT NOT NULL COMMENT '파티 ID',
   place_of_departure VARCHAR(255) NOT NULL COMMENT '출발지',
   destination VARCHAR(255) NOT NULL COMMENT '도착지',
   party_started_date_time VARCHAR(255) NOT NULL COMMENT '출발 시간',
   title VARCHAR(255) NOT NULL COMMENT '신고 제목',
   `description` VARCHAR(255) NULL COMMENT '신고 내용',
   CONSTRAINT pk_accusations PRIMARY KEY (id)
) COMMENT '신고내역';
