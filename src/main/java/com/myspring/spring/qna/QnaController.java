package com.myspring.spring.qna;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/qna")
public class QnaController {
	private QnaService qnaService;

	@Autowired
	public QnaController(QnaService qnaService) {
		this.qnaService = qnaService;
	}

	// 문의게시판 목록 출력
	@GetMapping("/getQnaPage")
	public ResponseEntity<?> getQna(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return qnaService.getQnaWithSearch(page, perPage, search, searchWord);
	}

	// 문의 전체 조회
	@GetMapping("/getqnaAll")
	public ResponseEntity<?> getQnaAll() {
		return qnaService.getQnaAll();
	}

	// qnaNo로 문의 가져오기
	@GetMapping("/getQna/{qnaNo}")
	public ResponseEntity<?> getQnaByQnaNo(@PathVariable("qnaNo") int qnaNo) {
		return qnaService.getQnaByQnaNo(qnaNo);
	}

	// type별 문의 조회
	@GetMapping("/{type}")
	public ResponseEntity<?> getQnaByType(@PathVariable("type") String type) {
		return qnaService.getQnaByType(type);
	}

	// 카테고리별 목록 조회
	@GetMapping("/getQnaListByType")
	public ResponseEntity<?> getQnaListByType(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord,
			@RequestParam("type") String type) {
		return qnaService.getQnaListByType(page, perPage, search, searchWord, type);
	}

	// 카테고리별 목록 조회
	@GetMapping("/getQnaList")
	public ResponseEntity<?> getQnaList(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord,
			@RequestParam("productNo") int productNo, @RequestParam(value = "id", required = false) String id) {
		return qnaService.getQnaList(page, perPage, search, searchWord, productNo, id);
	}

	// originalNo로 조회
	@GetMapping("/getQnaByOriginalNo")
	public ResponseEntity<?> getQnaByOrignalNo(@RequestParam("originalNo") int originalNo) {
		return qnaService.getQnaByOriginalNo(originalNo);
	}

	// 문의 등록
	@PostMapping("/insertqna")
	public ResponseEntity<?> insertQna(@RequestPart(value = "data") QnaVO requestData,
			@RequestParam(value = "fileList", required = false) List<MultipartFile> fileList) throws NotFoundException {
		return qnaService.insertQna(requestData, fileList);
	}

	// 댓글 등록
	@PostMapping("/insertReply")
	public ResponseEntity<?> insertReply(@RequestBody QnaVO qnaVO) {
		return qnaService.insertReply(qnaVO);
	}

	// 문의글 수정 시 댓글이 있으면 수정 불가
	// 문의 수정 & 댓글 수정
	@PatchMapping("/updateqna")
	public ResponseEntity<?> updateQna(@RequestPart(value = "data") QnaVO requestData,
			@RequestParam(value = "fileList", required = false) List<MultipartFile> fileList) throws NotFoundException {
		return qnaService.updateQna(requestData, fileList);
	}

	// 문의 삭제 & 댓글 삭제
	// 댓글 삭제시 originalNo 없이 삭제할때
	// reply true -> false
	@DeleteMapping("/deleteqna/{qnaNo}")
	public ResponseEntity<?> deleteQna(@PathVariable("qnaNo") int qnaNo) {
		return qnaService.deleteQna(qnaNo);
	}

	// 아이디로 문의 검색
	@GetMapping("/searchQnaById")
	public ResponseEntity<?> searchQnaById(@RequestParam("id") String id) {
		return qnaService.searchQnaById(id);
	}

	// 내용으로 문의 검색
	@GetMapping("/searchQnaByContent")
	public ResponseEntity<?> searchQnaByContent(@RequestParam("content") String content) {
		return qnaService.searchQnaByContent(content);
	}

	// 서버에서 이미지 가져오기
	@GetMapping("/qnaImage/{qnaNo}/{image}")
	public ResponseEntity<?> productimage(@PathVariable("qnaNo") int qnaNo, @PathVariable("image") String image)
			throws IOException {
		InputStream imageStream;
		try {
			imageStream = new FileInputStream("./images/qna/" + qnaNo + "/" + image);
		} catch (FileNotFoundException e) {
			imageStream = new FileInputStream("./images/error.png");
		}
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();
		return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
	}

}
