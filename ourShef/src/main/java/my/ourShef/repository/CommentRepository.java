package my.ourShef.repository;

import java.util.List;
import java.util.Optional;

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
	
	public Optional<Comment> findById(Long commentId){
		Comment findComment = em.find(Comment.class, commentId);
		return Optional.ofNullable(findComment);
	}
	
	public void delete(Comment comment) {
		em.remove(comment);
	}
	
	public List<Comment> getCommentListBySpot(Spot spot, Long limit, Long offset){
		Query query = em.createNativeQuery("select * from comment c where c.commented_spot_id = ? order by c.comment_id desc limit ? offset ?", Comment.class);
		query.setParameter(1, spot.getId()).setParameter(2, limit).setParameter(3, offset);
		List<Comment> resultList = query.getResultList();
		return resultList;
	}
	
	public Long getAllCommentsNumBySpot(Spot spot) {
		Query query = em.createQuery("select count(cm.id) from Comment cm where cm.commentedSpot = :spot");
		query.setParameter("spot", spot);
		List<Object> resultList = query.getResultList();
		
		return (Long)resultList.get(0);
	}
	
	
}
