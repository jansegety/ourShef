package my.ourShef.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Comment;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {

	private final EntityManager em;
	
	public Long save(Comment comment) {
		em.persist(comment);
		
		return comment.getId(); 
	}
	
	
}
