package my.ourShef.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;
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
	
	public Optional<Comment> findById(Long commentId){
		return commentRepository.findById(commentId);
	}
	
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}
	
	public List<Comment> getCommentListBySpot(Spot spot, Long limit, Long offset){
	
		return commentRepository.getCommentListBySpot(spot, limit, offset);
	}
	
	public Long getAllCommentsNumBySpot(Spot spot) {
		
		return commentRepository.getAllCommentsNumBySpot(spot);
	}
	
	/*
	 * true if there is no comment for the spot written by the user
	 */
	public boolean isNotPresentCommentOnSpotByUser(Spot commentedSpot, User commentUser) {
		return commentRepository.isNotPresentCommentOnSpotByUser(commentedSpot, commentUser);
	}
	
	
}
