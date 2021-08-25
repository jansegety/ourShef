package my.ourShef.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.Comment;
import my.ourShef.domain.Spot;
import my.ourShef.domain.User;

@Slf4j
@Repository
@Transactional
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
	
	public List<Comment> getAllCommentListByUser(User user){
		return em.createQuery("SELECT cm FROM Comment cm WHERE cm.commentUser =:user").setParameter("user", user).getResultList();
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
	
	/*
	 * true if there is no comment for the spot written by the user
	 */
	public boolean isNotPresentCommentOnSpotByUser(Spot commentedSpot, User commentUser) {
		
		 TypedQuery<Comment> query = em.createQuery("select cm from Comment cm where cm.commentedSpot =:commentedSpot and cm.commentUser =:commentUser", Comment.class);
		
		 List<Comment> resultList = query
		 .setParameter("commentedSpot", commentedSpot)
		 .setParameter("commentUser", commentUser)
		 .getResultList();
		 

		 if(resultList.size() == 0)
			 return true;
		 
		return false;
	}
	
	
}
