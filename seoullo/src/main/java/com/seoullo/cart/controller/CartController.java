package com.seoullo.cart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seoullo.cart.service.CartService;
import com.seoullo.cart.vo.CartVO;
import com.seoullo.member.vo.MemberVO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/cart")
@Log4j
public class CartController {
	
	private String module= "cart";
	
	@Autowired
	@Qualifier("cartServiceImpl")
	private CartService service;
	
	// 장바구니 리스트
	@RequestMapping("/list.do")
	public String list(Model model, HttpSession session) {
		log.info("장바구니 리스트");
		// 세션의 아이디를 가져와서 본인의 장바구니 내역만 보이게 하기.
		MemberVO vo = (MemberVO) session.getAttribute("login");
		String id = vo.getId();
		model.addAttribute("list", service.list(id));
		return module + "/list"; 
	}
	
	// 장바구니 담기 - Ajax
	@PostMapping(value="/add.do", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> add(CartVO vo) {
		log.info("장바구니 담기");
		int result = service.add(vo);
		if (result == 1) {
			return new ResponseEntity<String>("100", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("200", HttpStatus.OK);
		}
	}
	
	// 장바구니 중복 확인 - Ajax
	@PostMapping(value="/searchCart.do", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> searchCart(CartVO vo) {
		log.info("장바구니 중복확인");
	    Long cnt = service.searchCart(vo);
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("cnt", cnt);
	    return resultMap;
	}
	
	// 장바구니 수정 - Ajax
	@PostMapping(value="/update.do", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> update(CartVO vo) {
		log.info("장바구니 수정");
		int result = service.update(vo);
		if(result == 1) {
			return new ResponseEntity<String>("1", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("2", HttpStatus.OK);
		}
	}
	
	// 장바구니 삭제 - Ajax
	@PostMapping(value="/delete.do", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> delete(@RequestParam(value = "nos[]") List<Long> nos) {
		log.info("장바구니 삭제");
		// 선택 삭제도 있기 때문에 장바구니 번호를 배열로 가져온다.
		int result = service.delete(nos);
		if(result == nos.size()) {
			return new ResponseEntity<String>("1", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("2", HttpStatus.OK);
		}
	}

}
