package com.myspring.spring.qna;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 등록
	public ResponseEntity<?> insertQna(QnaVO requestData, List<MultipartFile> fileList) {
		QnaVO result = new QnaVO();
		ResponseEntity<?> entity = null;

		try {
			qnaMapper.insertQna(requestData, result);
			int qnaNo = result.getQnaNo();
//			System.out.println("qnaNo:" + qnaNo);
			File file = new File("./images/qna/" + qnaNo + "/");
			file.mkdir();
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					MultipartFile multipartFile = fileList.get(i);
					FileOutputStream writer = new FileOutputStream(
							"./images/qna/" + qnaNo + "/" + multipartFile.getOriginalFilename());
					writer.write(multipartFile.getBytes());
					writer.close();
				}
			}
			entity = new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}

	// 댓글 등록 - originalNo 받아서 reply = true로 바꿔주기
	public ResponseEntity<?> insertReply(QnaVO qnaVO) {
//		System.out.println(qnaVO.getType());
		int res, resReply;
		int originalNo = qnaVO.getOriginalNo();
		// productReply일때 원글의 productNo와 동일하게 셋팅 -> productDetail 페이지에 답글도 같이 불러오기 위함
		if (qnaVO.getType().equals("productReply")) {
			QnaVO productReply = qnaMapper.getQnaByQnaNo(originalNo);
//			System.out.println(productReply.getProductNo());
			qnaVO.setProductNo(productReply.getProductNo());
		}
		res = qnaMapper.insertReply(qnaVO);
		resReply = qnaMapper.updateReplyTrue(originalNo);
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
	public ResponseEntity<?> updateQna(QnaVO requestData, List<MultipartFile> fileList) {
		ResponseEntity<?> entity = null;
		try {
			int res = qnaMapper.updateQna(requestData);
			if (res == 0) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			File file;
			File[] underDir;

			file = new File("./images/qna/" + requestData.getQnaNo() + "/");
			file.mkdir();
			underDir = file.listFiles();
			if (underDir != null) {
				for (int i = 0; i < underDir.length; i++) {
					underDir[i].delete();
				}
			}
			if (fileList != null) {
				for (int i = 0; i < fileList.size(); i++) {
					MultipartFile multipartFile = fileList.get(i);
					FileOutputStream writer = new FileOutputStream(
							"./images/qna/" + requestData.getQnaNo() + "/" + multipartFile.getOriginalFilename());
					writer.write(multipartFile.getBytes());
					writer.close();
				}
			}
			entity = new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}

	// 문의 삭제 & 댓글 삭제
	// 댓글 삭제 시 원글 reply = false로 바꿔주기
	// 댓글을 삭제 시 원글의 originalNo를 qnaNo와 같도록 세팅
	public ResponseEntity<?> deleteQna(int qnaNo) {
		ResponseEntity<?> entity = null;
		try {
			QnaVO res = qnaMapper.getQna(qnaNo);
			// 문의 삭제
			int resQna = qnaMapper.deleteQna(qnaNo);
			if (resQna == 0)
				return new ResponseEntity<>(resQna, HttpStatus.INTERNAL_SERVER_ERROR);

			// 댓글 삭제
			if (res.isReply() == true) {
				int resDelReply = qnaMapper.deleteReply(qnaNo);
				if (resDelReply == 0)
					return new ResponseEntity<>(resDelReply, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				// 답글이 삭제될때 원글의 reply가 false로 바꾸기
				// reply글의 originalNo를 받아와서 그 originalNo의 reply를 false로 바꿔주기
				// qnaNo != originalNo 일 때는 답글없다는 의미 -> updateReplyFalse()
				if (res.getQnaNo() != res.getOriginalNo()) {
					// qna가 삭제될때 reply도 같이 삭제하기
					int resReply = qnaMapper.updateReplyFalse(res.getOriginalNo());
					if (resReply == 0)
						return new ResponseEntity<>(resReply, HttpStatus.INTERNAL_SERVER_ERROR);
					else
						return new ResponseEntity<>(resReply, HttpStatus.OK);
				}
			}

			File file;
			File[] underDir;

			file = new File("./images/qna/" + qnaNo + "/");
			file.mkdir();
			underDir = file.listFiles();
			if (underDir != null) {
				for (int i = 0; i < underDir.length; i++) {
					underDir[i].delete();
				}
			}
			entity = new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}

	// 아이디로 문의 검색
	public ResponseEntity<?> searchQnaById(String id) {
		List<QnaVO> res = qnaMapper.searchQnaById(id);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 내용으로 문의 검색
	public ResponseEntity<?> searchQnaByContent(String content) {
		List<QnaVO> res = qnaMapper.searchQnaByContent(content);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 1개 찾기
	public ResponseEntity<?> getQnaByQnaNo(int qnaNo) {
		// System.out.println(qnaNo);
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
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("qnaList", qnaList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);

	}

	// originalNo로 조회
	public ResponseEntity<?> getQnaByOriginalNo(int originalNo) {
		int res = qnaMapper.getQnaByOriginalNo(originalNo);
		if (res == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
	}

	// productDetail qna 불러오기
	public ResponseEntity<?> getQnaList(int page, int perPage, String search, String searchWord, int productNo,
			String id) {
		int start = (page - 1) * perPage;
		List<QnaAndProductVO> productQnaList = qnaMapper.getQnaList(start, perPage, search, searchWord, productNo, id);
		int count = qnaMapper.getQnaCount(search, searchWord, productNo, id);

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("productQnaList", productQnaList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);

	}


}
