package my.ourShef.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import my.ourShef.domain.RelationshipRequest;
import my.ourShef.domain.User;

@Repository
@RequiredArgsConstructor
public class RelationshipRequestRepository {

	private final EntityManager em;
	
	public String save(RelationshipRequest relationshipRequest) {
		em.persist(relationshipRequest);
		return relationshipRequest.getId();
	}
	
	public Optional<RelationshipRequest> findById(String UUID){
		return Optional.ofNullable(em.find(RelationshipRequest.class, UUID));
	}
	
	public List<RelationshipRequest> findByOwnerAndFromUserAndToUser(User owner, User fromUser, User toUser) {
		
		List<RelationshipRequest> resultList = em.createQuery("SELECT rr FROM RelationshipRequest rr WHERE rr.owner=:owner AND rr.fromUser=:fromUser AND rr.toUser=:toUser",RelationshipRequest.class)
		.setParameter("owner", owner)
		.setParameter("fromUser", fromUser)
		.setParameter("toUser", toUser)
		.getResultList();
		
		return resultList;
	}
	
	public void delete(RelationshipRequest relationshipRequest) {
		em.remove(relationshipRequest);
	}
	
	public List<RelationshipRequest> getSendedRelationshipRequest(User owner, User fromUser){
		return em.createQuery("SELECT rr FROM RelationshipRequest rr WHERE rr.owner=:owner AND rr.fromUser=:fromUser",RelationshipRequest.class)
		.setParameter("owner", owner)
		.setParameter("fromUser", fromUser)
		.getResultList();
	}
	
	public List<RelationshipRequest> getReceivedRelationshipRequest(User owner, User toUser){
		return em.createQuery("SELECT rr FROM RelationshipRequest rr WHERE rr.owner=:owner AND rr.toUser=:toUser",RelationshipRequest.class)
		.setParameter("owner", owner)
		.setParameter("toUser", toUser)
		.getResultList();
	}
	
}
