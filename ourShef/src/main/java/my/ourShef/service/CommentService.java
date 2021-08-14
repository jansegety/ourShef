package my.ourShef.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.repository.CommentRepository;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	
	
	public Long save(Comment comment) {

		return commentRepository.save(comment);
	}
	
	public List<Comment> getCommentListBySpot(Spot spot, Long limit, Long offset){
	
		return commentRepository.getCommentListBySpot(spot, limit, offset);
	}
	
	public Long getAllConmentsNumBySpot(Spot spot) {
		
		return commentRepository.getAllConmentsNumBySpot(spot);
	}
	
}
