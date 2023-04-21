package com.seoullo.cart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.seoullo.cart.mapper.CartMapper;
import com.seoullo.cart.vo.CartVO;

import lombok.Setter;

@Service
@Qualifier("cartServiceImpl")
public class CartServiceImpl implements CartService {
	
	@Setter(onMethod_ = @Autowired)
	private CartMapper mapper;

	// 장바구니 리스트
	@Override
	public List<CartVO> list(String id) {
	    List<CartVO> list = mapper.list(id);
	    for (CartVO vo : list) {
	    	CartVO reserve = mapper.checkReserveNum(vo);
	    	vo.setReserveNum(reserve.getReserveNum()); // 현재 예약 인원 확인
	    	vo.setMaxNum(reserve.getMaxNum()); // 마감인원 확인
	    }
	    return list;
	}

	// 장바구니에서 예약할 때
	@Override
	public List<CartVO> orderList(List<Long> nos) {
	    List<CartVO> list = new ArrayList<>();
	    for (Long no : nos) {
	    	// 장바구니 번호 데이터를 넘겨 그 장바구니에 담긴 투어 정보와 예약 정보를 불러온다.
	        List<CartVO> orderList = mapper.orderList(no);
	        list.addAll(orderList);
	    }
	    return list;
	}
	
	// 장바구니 담기
	@Override
	public int add(CartVO vo) {
		return mapper.add(vo);
	}

	// 장바구니 수정
	@Override
	public int update(CartVO vo) {
		return mapper.update(vo);
	}

	// 장바구니 삭제
	@Override
	public int delete(List<Long> nos) {
	    int result = 0;
	    for (Long no : nos) {
	        result += mapper.delete(no);
	    }
	    return result;
	}

	// 장바구니 중복 확인을 위한 장바구니 번호 찾기
	@Override
	public Long searchCart(CartVO vo) {
		return mapper.searchCart(vo);
	}


}
