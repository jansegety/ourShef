package my.ourShef.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import my.ourShef.domain.RelationshipRequest;
import my.ourShef.domain.User;
import my.ourShef.repository.RelationshipRequestRepository;


@Service
@RequiredArgsConstructor
public class RelationshipRequestService {
	
	private final RelationshipRequestRepository relationshipRequestRepository;

	public String save(RelationshipRequest relationshipRequest) {
		return relationshipRequestRepository.save(relationshipRequest);
	}
	
	public Optional<RelationshipRequest> findById(String UUID){
		return relationshipRequestRepository.findById(UUID);
	}
	
	public List<RelationshipRequest> findByOwnerAndFromUserAndToUser(User owner, User fromUser, User toUser) {
		
		return relationshipRequestRepository.findByOwnerAndFromUserAndToUser(owner, fromUser, toUser);
	}
	
	public void delete(RelationshipRequest relationshipRequest) {
		relationshipRequestRepository.delete(relationshipRequest);
	}
	
	public List<RelationshipRequest> getSendedRelationshipRequest(User owner, User fromUser){
		return relationshipRequestRepository.getSendedRelationshipRequest(owner, fromUser);
	}
	
	public List<RelationshipRequest> getReceivedRelationshipRequest(User owner, User toUser){
		return relationshipRequestRepository.getReceivedRelationshipRequest(owner, toUser);
	}
	
}
