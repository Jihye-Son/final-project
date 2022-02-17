package com.myspring.spring.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
	private NoticeMapper noticeMapper;
	
	@Autowired
	public NoticeService(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}
	
	//�������� ��� ���
	public List<NoticeVO> getAllMembers() {
		return noticeMapper.getAllMembers();
	}
	
	//�������� �Խù� ����
	public NoticeVO getMemberFindByID(int noticeNo) {
		return noticeMapper.getMemberFindByID(noticeNo);
	}

	//�������� ����
	public ResponseEntity<?> deleteMember(int noticeNo) {
		int res = noticeMapper.deleteMember(noticeNo);
		
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res,HttpStatus.OK);
	}

	//�������� �Խù� �ۼ�
	public ResponseEntity<?> insertMember(NoticeVO noticeVO) {
		return noticeMapper.insertMember(noticeVO);
	}

	//�������� ����
	public ResponseEntity<?> updateMember(int noticeNo, String title, String content, String image) {
		int res = noticeMapper.updateMember(noticeNo, title, content, image);
		
		if ( res == 0 )
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	
	
	
}
