package com.seoullo.book.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class GuideVO {
	
	private long tourNo; // 투어번호
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date day; // 투어일자
	private String status; // 투어상태
	private long reserveNum, maxNum; // 예약인원, 마감인원
	
	private List<GuideDetailVO> guideDetailList; // 투어정보가 담긴 리스트
}
