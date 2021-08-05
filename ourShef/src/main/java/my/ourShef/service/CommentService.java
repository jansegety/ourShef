package my.ourShef.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Comment;
import my.ourShef.repository.CommentRepository;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	
	@Transactional
	public Long save(Comment comment) {

		return commentRepository.save(comment);
	}
	
	
}
