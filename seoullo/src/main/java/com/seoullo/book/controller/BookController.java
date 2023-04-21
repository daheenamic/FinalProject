package com.seoullo.book.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seoullo.book.service.BookService;
import com.seoullo.book.vo.BookDetailVO;
import com.seoullo.book.vo.BookVO;
import com.seoullo.cart.service.CartService;
import com.seoullo.cart.vo.CartVO;
import com.seoullo.member.service.MemberService;
import com.seoullo.member.vo.MemberVO;
import com.seoullo.tour.service.TourService;
import com.webjjang.util.PageObject;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/book")
@Log4j
public class BookController {
	
	private String module = "book";
	
	@Autowired
	@Qualifier("bookServiceImpl")
	private BookService service;
	
	@Autowired
	@Qualifier("tourServiceImpl")
	private TourService tourService;
	
	@Autowired
	@Qualifier("memberServiceImpl")
	private MemberService memberService;
	
	@Autowired
	@Qualifier("cartServiceImpl")
	private CartService cartService;
	
	// 예약 리스트 불러오기
	@RequestMapping("/list.do")
	public String list(@ModelAttribute("pageObject")PageObject pageObject, Model model, HttpSession session, Long tourNo) {
		// session에 저장된 로그인 정보를 불러와서 loginVO에 저장.
		MemberVO loginVO = (MemberVO) session.getAttribute("login");
		// 회원 등급에 따라 각각 다른 예약 리스트를 보이게 하기 위해 gradeNo 변수에 등급을 저장
		int gradeNo = loginVO .getGradeNo();
		pageObject.setPerPageNum(5); // 한 페이지에 데이터 5개씩 보이게 설정. (기본값은 10개)
		if(gradeNo == 2 || gradeNo == 3) { // 가이드의 예약 리스트
			model.addAttribute("tourvo", tourService.view(tourNo, 0)); // 투어 정보 불러오기
			model.addAttribute("list", service.guideList(tourNo)); // 본인 투어에 예약한 예약자들의 리스트를 가져오기.
			return module + "/guideList"; // 가이드의 list.jsp는 형식을 다르게 해서 만들었다.
		} else if (gradeNo == 1){ // 일반회원의 예약 리스트
			String id = loginVO.getId(); // 본인의 예약 리스트를 가져와야 하므로 session에 있는 로그인 정보를 가져온다.
			model.addAttribute("list", service.perList(pageObject, id)); // 해당하는 id에 대한 리스트만 불러온다.	
			return module + "/list"; // 일반회원과 관리자의 list는 같은 jsp를 사용한다.
		} else { // 관리자의 예약 리스트
			model.addAttribute("list", service.list(pageObject)); // 모든 회원의 리스트를 불러온다.
			return module + "/list"; // 일반회원과 관리자의 list는 같은 jsp를 사용한다.
		}
	}
	
	// 예약 상세보기
	@RequestMapping("/view.do")
	public String view(long no, Model model) {
		// 해당하는 예약 번호의 데이터를 전송해서 상세정보를 불러온다.
		model.addAttribute("view", service.view(no));
		return module + "/view";
	}
	
	// 예약 취소하기
	@PostMapping("/cancel.do")
	public String cancel(BookDetailVO vo) {
		service.cancel(vo); // 취소할 예약 정보를 vo에 담아서 데이터를 전송하고 mapper.xml에서 update처리.
		return "redirect:view.do?no="+vo.getBookNo(); // 예약 취소 후에는 다시 상세보기로 돌아가기.
	}
	
	// 예약하기 폼
	@GetMapping("/book.do")
	public String bookForm(String cartNos, Long no, Model model, HttpSession session) {
		if(no != null ) { // 투어번호 들어왔을 때 - 투어 상세정보에서 바로 예약할 때			
			model.addAttribute("vo", tourService.view(no, 0));
		} else if (cartNos != null){ // 장바구니 번호 들어왔을 때 - 장바구니에서 예약할 때
			// 장바구니 번호를 String형태로 (1,2,3) 가져와서 콤마(,)를 기준으로 각 번호를 List형태로 list 변수에 저장한다.
			List<Long> list = Arrays.stream(cartNos.split(",")).map(Long::parseLong).collect(Collectors.toList());
			// 여러개의 장바구니 번호가 담긴 list 데이터를 보내서 해당 투어의 정보를 불러온다.
			model.addAttribute("list", cartService.orderList(list));
		}
		MemberVO vo = (MemberVO) session.getAttribute("login");
		String id = vo.getId();
		model.addAttribute("member", memberService.view(id));
		return module + "/book";
	}
	
	// 예약하기 처리
	@PostMapping("/book.do")
	public String book(BookVO vo, BookDetailVO dvo, Long[] cartNos) throws Exception {
		List<BookDetailVO> bookDetailList = new ArrayList<BookDetailVO>();
		// 장바구니에서 여러개 예약할 떄
		if(cartNos != null) {			
			// 장바구니 번호 가져와서 List로 바꾸기
			List<Long> cartNoList = Arrays.asList(cartNos);
			// 장바구니 리스트 여러개 가져오기
			List<CartVO> cartList = cartService.orderList(cartNoList);
			for(CartVO cartvo : cartList) {
				BookDetailVO detailvo = new BookDetailVO();
				detailvo.setTourNo(cartvo.getTourNo()); // 투어번호
				detailvo.setPeopleA(cartvo.getPeopleA()); // 대인
				detailvo.setPeopleB(cartvo.getPeopleB()); // 소인
				detailvo.setTourPrice(cartvo.getTourPrice()); // 투어가격
				detailvo.setDay(cartvo.getDay()); // 투어일자
				if(vo.getPayMethod().equals("신용카드")) { // 신용카드 선택시
					detailvo.setBookStatus("예약완료"); // 예약 완료
				} else { // 무통장입금 선택시
					detailvo.setBookStatus("예약대기"); // 예약대기
				}
				bookDetailList.add(detailvo);
			}
		// 한개만 예약할 때
		} else if(cartNos == null) {
			if(vo.getPayMethod().equals("신용카드")) { //신용카드 선택시
				dvo.setBookStatus("예약완료"); // 예약완료
			} else { // 무통장입금 선택시
				dvo.setBookStatus("예약대기"); // 예약대기
			}
			bookDetailList.add(dvo);
		}
		if(vo.getPayMethod().equals("신용카드")) {
			// 신용카드 선택했을 때는 결제완료
			vo.setPayStatus("결제완료");
		} else {
			// 무통장입금 선택했을 때는 결제대기 - 관리자 확인 필요
			vo.setPayStatus("결제대기");
		}
		vo.setBookDetailList(bookDetailList);
		Integer result = service.book(vo);
		// 장바구니에서 예약했을 시, 장바구니 삭제 처리
		if(cartNos != null && result != null) {
			List<Long> list = Arrays.asList(cartNos);
			cartService.delete(list);
		}
		return "redirect:list.do";
	}
	
	// 예약수정 폼 - 관리자만 가능
	@GetMapping("/modify.do")
	public String modifyForm(Long no, Model model) {
		// 수정 할 예약 상세보기를 가져온다.
		model.addAttribute("vo", service.view(no));
		return module + "/modify";
	}
	
	// 예약 수정 처리 - 관리자만 가능
	@PostMapping("/modify.do")
	public String modify(BookVO vo) {
		// 수정할 내용을 vo에 담아서 수정처리 하는 service로 데이터를 보낸다.
		service.bookInfoUpdate(vo);
		return "redirect:view.do?no="+vo.getNo();
	}
	
	// 투어 완료 처리 (Ajax) - 가이드만 가능
	@PostMapping(value="/tourComplete.do", produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public ResponseEntity<String> tourComplete(Long no) {
		int result = service.tourComplete(no);
		if(result != 0) {
			return new ResponseEntity<String>("1", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("2", HttpStatus.OK);
		}
	}
}
