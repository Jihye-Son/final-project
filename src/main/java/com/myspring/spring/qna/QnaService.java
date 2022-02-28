package com.myspring.spring.qna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.product.ProductMapper;

@Service
public class QnaService {
	private QnaMapper qnaMapper;
	private ProductMapper productMapper;

	@Autowired
	public QnaService(QnaMapper qnaMapper, ProductMapper productMapper) {
		this.qnaMapper = qnaMapper;
		this.productMapper = productMapper;
	}

	// 카테고리별 전체 개수 가져오기
	// product => 'general', 'product', 'productNotice', 'productReply',
	// 'generalReply'
	// beforeDelivery => 'cancel', 'change', 'changeaddress', 'cancelNotice',
	// 'cancelReply', 'changeReply', 'changeaddressReply'
	// delivery => 'delivery', 'deliveryNotice', 'deliveryReply'
	// afterDelivery => 'return', 'exchange', 'error', 'returnNotice',
	// 'returnReply', 'exchangeReply', 'errorReply'
//	public ResponseEntity<?> getCount(String search, String searchWord, String type) {
//		int res = 0;
//		System.out.println(type);
//
//		if (type.equals("product")) {
//			// 상품문의 전체 개수 조회
//			res = qnaMapper.getQnaProductCount(search, searchWord, type);
//		} else if (type.equals("beforeDelivery")) {
//			// 배송전 문의 전체 개수 조회
//			res = qnaMapper.getQnaBeforeDeliveryCount(search, searchWord, type);
//		} else if (type.equals("delivery")) {
//			// 배송문의 전체 개수 조회
//			res = qnaMapper.getQnaDeliveryCount(search, searchWord);
//		} else if (type.equals("afterDelivery")) {
//			// 배송후 문의 전체 개수 조회
//			res = qnaMapper.getQnaAfterDeliveryCount(search, searchWord);
//		} else {
//			// 이외에는 에러 반환
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		if (res == 0)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}

	// 문의게시판 목록 출력
	public ResponseEntity<?> getQnaWithSearch(int page, int perPage, String search, String searchWord) {
		int start = (page - 1) * perPage;
		List<QnaVO> res = qnaMapper.getQnaWithSearch(start, perPage, search, searchWord);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 전체 조회
	public ResponseEntity<?> getQnaAll() {
		List<QnaVO> res = qnaMapper.getQnaAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 카테고리별 조회
	public ResponseEntity<?> getQnaByType(String type) {
		List<QnaVO> res = qnaMapper.getQnaByType(type);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	// 상품문의 카테고리 전체 조회
//	public ResponseEntity<?> getQnaProductAll(int page, int perPage, String search, String searchWord) {
//		int start = (page - 1) * perPage;
//		List<QnaVO> res = qnaMapper.getQnaProductAll(start, perPage, search, searchWord);
//		if (res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//
//	// 배송문의 카테고리 전체 조회
//	public ResponseEntity<?> getQnaDeliveryAll(int page, int perPage, String search, String searchWord) {
//		int start = (page - 1) * perPage;
//		List<QnaVO> res = qnaMapper.getQnaDelieveryAll(start, perPage, search, searchWord);
//		if (res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//
//	// 배송 전 변경&취소 카테고리 전체 조회
//	public ResponseEntity<?> getQnaBeforeDeliveryAll(int page, int perPage, String search, String searchWord) {
//		int start = (page - 1) * perPage;
//		List<QnaVO> res = qnaMapper.getQnaBeforeDeliveryAll(start, perPage, search, searchWord);
//		if (res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//
//	// 배송 후 교환&반품 카테고리 전체 조회
//	public ResponseEntity<?> getQnaAfterDeliveryAll(int page, int perPage, String search, String searchWord) {
//		int start = (page - 1) * perPage;
//		List<QnaVO> res = qnaMapper.getQnaAfterDeliveryAll(start, perPage, search, searchWord);
//		if (res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}

	// 문의 등록
	public ResponseEntity<?> insertQna(QnaVO qnaVO) {
		int res = qnaMapper.insertQna(qnaVO);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);

	}

	// 댓글 등록 시 원글에 productNo가 있을 때 댓글 productNo도 넣어줘야함 =====
	// 댓글 등록 - originalNo 받아서 reply = true로 바꿔주기
	public ResponseEntity<?> insertReply(QnaVO qnaVO) {
		int res = qnaMapper.insertReply(qnaVO);
		int originalNo = qnaVO.getOriginalNo();
		int resReply = qnaMapper.updateReplyTrue(originalNo);

		QnaVO qna = qnaMapper.getQnaByQnaNo(originalNo);
		if (qna.getProductNo() != 0) {
			qnaVO.setProductNo(qna.getProductNo());
		}

		if (res == 0) {
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			if (resReply == 0) {
				return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(res, HttpStatus.OK);
			}
		}

	}

	// 문의 수정 & 댓글 수정
	public ResponseEntity<?> updateQna(int qnaNo, String type, String content, boolean secret, String image) {
		int res = qnaMapper.updateQna(qnaNo, type, content, secret, image);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 삭제 & 댓글 삭제
	// 댓글 삭제 시 원글 reply = false로 바꿔주기
	// 댓글을 삭제 시 원글의 originalNo를 qnaNo와 같도록 세팅
	public ResponseEntity<?> deleteQna(int qnaNo) {
		QnaVO res = qnaMapper.getQna(qnaNo);
		// 문의 삭제
		int resQna = qnaMapper.deleteQna(qnaNo);


		if (res.isReply() == true) {
			int resDelReply = qnaMapper.deleteReply(qnaNo);
			if (resDelReply == 0 )
				return new ResponseEntity<>(resDelReply, HttpStatus.INTERNAL_SERVER_ERROR);
			else
				return new ResponseEntity<>(resDelReply, HttpStatus.OK);
		}else {
		// 답글이 삭제될때 원글의 reply가 false로 바꾸기
		// reply글의 originalNo를 받아와서 그 originalNo의 reply를 false로 바꿔주기
		// qnaNo != originalNo 일 때는 답글없다는 의미 -> updateReplyFalse()
		if (res.getQnaNo() != res.getOriginalNo()) {
			// qna가 삭제될때 reply도 같이 삭제하기
			int resReply = qnaMapper.updateReplyFalse(res.getOriginalNo());
			res.setOriginalNo(res.getQnaNo());
			if (resReply == 0)
				return new ResponseEntity<>(resReply, HttpStatus.INTERNAL_SERVER_ERROR);
			else
				return new ResponseEntity<>(resReply, HttpStatus.OK);
		}
		}
		if (resQna == 0 )
			return new ResponseEntity<>(resQna, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(resQna, HttpStatus.OK);
	}

	// 아이디로 문의 검색
	public ResponseEntity<?> searchQnaById(String id) {
		List<QnaVO> res = qnaMapper.searchQnaById(id);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 내용으로 문의 검색
	public ResponseEntity<?> searchQnaByContent(String content) {
		List<QnaVO> res = qnaMapper.searchQnaByContent(content);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 1개 찾기
	public ResponseEntity<?> getQnaByQnaNo(int qnaNo) {
		System.out.println(qnaNo);
		QnaVO res = qnaMapper.getQnaByQnaNo(qnaNo);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 카테고리별로 qna목록 출력
	public ResponseEntity<?> getQnaListByType(int page, int perPage, String search, String searchWord, String type) {
		int start = (page - 1) * perPage;
		List<QnaAndProductVO> qnaList = qnaMapper.getQnaListByType(start, perPage, search, searchWord, type);
		int count = qnaMapper.getQnaCountByType(search, searchWord, type);

		if (qnaList == null || count == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("qnaList", qnaList);
			resMap.put("count", count);
			return new ResponseEntity<>(resMap, HttpStatus.OK);
		}

	}

	// originalNo로 조회
	public ResponseEntity<?> getQnaByOriginalNo(int originalNo) {
		QnaVO res = qnaMapper.getQnaByOriginalNo(originalNo);
		if (res == null) {
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
	}

	// productDetail qna 불러오기
	public ResponseEntity<?> getQnaListByProductNo(int page, int perPage, String search, String searchWord, String type,
			int productNo) {
		int start = (page - 1) * perPage;
		List<QnaVO> productQnaList = qnaMapper.getQnaListByProductNo(start, perPage, search, searchWord, productNo);
		int count = qnaMapper.getQnaCountByProductNo(search, searchWord, productNo);
		if (productQnaList == null || count == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("productQnaList", productQnaList);
			resMap.put("count", count);
			return new ResponseEntity<>(resMap, HttpStatus.OK);
		}

	}

//	//기간으로 문의 검색(일주일)
//	public ResponseEntity<?> searchQnaByWeek() {
//		List<QnaVO> res = qnaMapper.searchQnaByWeek();
//		if(res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//	
//	//기간으로 문의 검색(한달)
//	public ResponseEntity<?> searchQnaByMonth() {
//		List<QnaVO> res = qnaMapper.searchQnaByMonth();
//		if(res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//	//기간으로 문의 검색(세달)
//	public ResponseEntity<?> searchQnaByMonths() {
//		List<QnaVO> res = qnaMapper.searchQnaByMonths();
//		if(res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}

}
