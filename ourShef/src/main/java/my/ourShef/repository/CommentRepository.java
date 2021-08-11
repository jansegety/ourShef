package my.ourShef.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {

	private final EntityManager em;
	
	public Long save(Comment comment) {
		em.persist(comment);
		
		return comment.getId(); 
	}
	
	public List<Comment> getCommentListBySpot(Spot spot, int limit, int offset){
		Query query = em.createNativeQuery("select * from comment c where c.commented_spot_id = ? limit ? offset ?", Comment.class);
		query.setParameter(1, spot.getId()).setParameter(2, limit).setParameter(3, offset);
		List<Comment> resultList = query.getResultList();
		return resultList;
	}
	
	
}
