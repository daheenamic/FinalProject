package com.seoullo.book.vo;


import lombok.Data;

@Data
public class GuideDetailVO {
	
	private long no; // 예약번호
	private String name, tel, email, gender; // 예약자이름, 연락처, 이메일, 성별
	private long peopleA, peopleB; // 대인인원, 소인인원
	private String bookStatus, review; // 예약상태, 리뷰작성가능여부

}
