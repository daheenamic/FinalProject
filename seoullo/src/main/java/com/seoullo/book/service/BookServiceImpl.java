package com.seoullo.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.seoullo.book.mapper.BookMapper;
import com.seoullo.book.vo.BookDetailVO;
import com.seoullo.book.vo.BookVO;
import com.seoullo.book.vo.GuideVO;
import com.webjjang.util.PageObject;

import lombok.Setter;

@Service
@Qualifier("bookServiceImpl")
public class BookServiceImpl implements BookService {
	
	@Setter(onMethod_ = @Autowired)
	private BookMapper mapper;

	// 예약 리스트 - 관리자
	@Override
	public List<BookVO> list(PageObject pageObject) {
		pageObject.setTotalRow(mapper.getTotalRow(pageObject)); // 페이징 처리
		List<BookVO> list = mapper.list(pageObject); // list를 불러와서 저장
		for (BookVO vo : list) { // vo에 여러건의 list를 담기
			vo.setBookDetailList(mapper.viewDetail(vo.getNo()));
		}
		return list;
	}

	// 예약 리스트 - 회원
	@Override
	public List<BookVO> perList(PageObject pageObject, String id) {
		pageObject.setTotalRow(mapper.getTotalRow(pageObject));
		List<BookVO> list = mapper.perList(pageObject, id); // 본인의 리스트만 불러와야 하기 때문에 id도 같이 데이터를 넘긴다.
		for (BookVO vo : list) { // vo에 여러건의 list를 담기
			vo.setBookDetailList(mapper.viewDetail(vo.getNo()));
		}
		return list;
	}

	// 예약 리스트 - 가이드
	@Override
	public List<GuideVO> guideList(long tourNo) {
		List<GuideVO> list = mapper.guideList(tourNo); // 해당 투어 번호의 데이터를 넘긴다.
		for (GuideVO vo : list) {
			// 해당 투어 번호와 해당 일자의 데이터를 넘기고 그에 맞는 예약자 정보 리스트를 불러온다.
			vo.setGuideDetailList(mapper.guideDetailList(vo.getDay(), tourNo));
		}
		return list;
	}
	
	// 상세보기
	@Override
	public BookVO view(long no) {
		BookVO vo = mapper.view(no); // 해당 예약번호의 데이터를 넘긴다.
		vo.setBookDetailList(mapper.viewDetail(no));
		return vo;
	}

	// 예약하기
	@Override
	public int book(BookVO vo) {
		mapper.book(vo); // 예약자 정보가 담긴 vo를 실행하고 예약 정보를 Insert한다.
		long no = mapper.findBookNo(vo.getId()); // 방금 Insert한 예약 번호를 찾아온다.
		for (BookDetailVO o : vo.getBookDetailList()) {
			o.setBookNo(no);
			int result = mapper.bookDetail(o);  // 해당 예약번호에 여러건의 투어를 담는 처리.
			if(result == 1) {				
				mapper.incReserveNum(o); // 예약 인원 증가 처리
			}
			// 해당 일자의 마감 처리를 위해 예약인원을 가져온다.
			BookDetailVO dvo = mapper.checkReserveNum(o);	
			// 예약 인원이 마감인원보다 같거나 크면 해당 일자는 '마감'처리를 한다.
			if(dvo.getReserveNum() >= dvo.getMaxNum()) {
				dvo.setStatus("마감");
				mapper.reserveStatusUpdate(dvo); // 해당 일자의 '마감'처리
				mapper.tourStatusUpdateWhenBooking(dvo.getTourNo()); // 투어 마감 상태 변경 프로시저 호출
			}
		}
		return 1;
	}

	// 예약 취소하기
	@Override
	public int cancel(BookDetailVO vo) {
	    Integer result = mapper.cancel(vo.getNo()); // 해당 예약 번호의 예약 취소 처리
	    if(result != null) {
	    	mapper.decReserveNum(vo); // 해당 인원만큼 예약 인원 감소 처리
	    	BookDetailVO dvo = mapper.checkReserveNum(vo);
	    	// 해당 일자의 '예약가능' 처리를 위해 예약 인원을 가져온다.
	    	dvo.setStatus("예약가능");
	    	mapper.reserveStatusUpdate(dvo); // 해당 일자의 '예약가능'처리
	    	mapper.tourStatusUpdateWhenCancel(dvo.getTourNo());  // 투어 예약가능 상태 변경 프로시저 호출
	    }
	    return result;
	}

	// 투어 완료 처리 - 가이드
	@Override
	public int tourComplete(long no) {
		mapper.deleteReview(no); 
	    return 1;
	}

	// 회원의 예약 정보 수정 - 관리자
	@Override
	public int bookInfoUpdate(BookVO vo) {
		Integer result = mapper.bookInfoUpdate(vo);
		if(result == 1) {
			mapper.bookStatusUpdate(vo.getNo());
		}
		return result;
	}
}
