package com.myspring.spring.qna;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QnaMapper {
	// 전체 개수 가져오기
	@SelectProvider(type = QnaUtils.class, method = "getQnaCountByType")
	int getQnaCountByType(String search, String searchWord, String type);

	// 카테고리별 글목록조회
	@SelectProvider(type = QnaUtils.class, method = "getQnaListByType")
	List<QnaAndProductVO> getQnaListByType(int start, int perPage, String search, String searchWord, String type);

	// 게시판 목록 출력
	@Select("select * from qnatable where qnaNo = #{qnaNo}")
	QnaVO getQna(@Param("qnaNo") int qnaNo);

	// 문의 게시판 목록 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') by originalNo desc, qnaNo asc limit #{start}, #{perPage}")
	List<QnaVO> getQnaWithSearch(@Param("start") int start, @Param("perPage") int perPage,
			@Param("search") String search, @Param("searchWord") String searchWord);

	// 문의 전체 조회
	@Select("select * from qnatable order by qnaNo desc")
	List<QnaVO> getQnaAll();

	// qnaNo로 1개 문의 가져오기
	@Select("select * from qnatable where qnaNo = #{qnaNo}")
	QnaVO getQnaByQnaNo(@Param("qnaNo") int qnaNo);

	// type별 문의 조회
	@Select("select * from qnatable where type = #{type} order by qnaNo desc")
	List<QnaVO> getQnaByType(@Param("type") String type);

	// originalNo로 조회
	@Select("select qnaNo from qnatable where originalNo = #{originalNo} AND reply = false")
	int getQnaByOriginalNo(@Param("originalNo") int originalNo);

	// 문의 등록
//	@Insert("call autoQuestion(#{in.productNo}, #{in.type}, #{in.reply}, #{in.content}, #{in.id}, #{in.secret}, #{in.image})")
	@Insert("insert into qnatable(qnaNo, productNo, type, originalNo, reply, content, id, secret, image) "
			+ "values((select A.num from (SELECT MAX(qnaNo)+1 as num FROM qnatable) A), #{in.productNo}, #{in.type}, "
			+ "(select A.num from (SELECT MAX(qnaNo)+1 as num FROM qnatable) A), #{in.reply}, #{in.content}, #{in.id}, #{in.secret}, #{in.image})")
	@Options(useGeneratedKeys = true, keyProperty = "result.qnaNo", keyColumn = "qnaNo")
	int insertQna(@Param("in") QnaVO in, @Param("result") QnaVO result);

	// 댓글 등록
	@Insert("call autoReply(#{in.productNo}, #{in.type}, #{in.originalNo}, #{in.content}, #{in.id}, #{in.secret}, #{in.image})")
	int insertReply(@Param("in") QnaVO qnaVO);

	// 댓글 등록시 reply 업데이트
	@Update("update qnatable set reply = true where qnaNo = #{originalNo}")
	int updateReplyTrue(int originalNo);

	// 문의 수정 & 댓글 수정
	@Update("update qnatable set type=#{in.type}, content = #{in.content}, secret = #{in.secret}, image = #{in.image} where qnaNo=#{in.qnaNo}")
	int updateQna(@Param("in") QnaVO in);
	
	// 문의 삭제 & 댓글 삭제
	@Delete("delete from qnatable where qnaNo = #{qnaNo}")
	int deleteQna(@Param("qnaNo") int qnaNo);

	// 댓글 삭제시 reply 업데이트
	@Update("update qnatable set reply = false where qnaNo = #{originalNo}")
	int updateReplyFalse(@Param("originalNo") int originalNo);

	// 아이디로 문의 검색
	@Select("select * from qnatable where id = #{id} order by qnaNo desc")
	List<QnaVO> searchQnaById(@Param("id") String id);

	// 내용으로 문의 검색
	@Select("select * from qnatable where content like CONCAT('%', #{content}, '%') order by qnaNo desc")
	List<QnaVO> searchQnaByContent(@Param("content") String content);

	// 원글삭제시 댓글도 삭제
	@Delete("delete from qnatable where originalNo = #{qnaNo}")
	int deleteReply(@Param("qnaNo") int qnaNo);

	// 상품관련 qna리스트 가져오기
	@SelectProvider(type = QnaUtils.class, method = "getQnaList")
	List<QnaAndProductVO> getQnaList(int start, int perPage, String search, String searchWord, int productNo, String id);

	// 전체 개수 가져오기
	@SelectProvider(type = QnaUtils.class, method = "getQnaCount")
	int getQnaCount(String search, String searchWord, int productNo, String id);
	

}
