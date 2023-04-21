package com.seoullo.cart.mapper;

import java.util.List;

import com.seoullo.cart.vo.CartVO;

public interface CartMapper {
	
	// 장바구니 전체 리스트
	public List<CartVO> list(String id);
	
	// 장바구니 번호로 가져오는 리스트
	public List<CartVO> orderList(long no);
	
	// 장바구니 담기
	public int add(CartVO vo);
	
	// 장바구니 수정
	public int update(CartVO vo);
	
	// 장바구니 삭제
	public int delete(long no);
	
	// 마감 인원 확인
	public CartVO checkReserveNum(CartVO vo);
	
	// 장바구니 중복 확인
	public Long searchCart(CartVO vo);
}
