package my.ourShef.controller.dto;

import lombok.Data;
import my.ourShef.domain.constant.RelationshipRequestState;

@Data
public class SendedRelationRequestDto {

	String id;
	String toUserNickName;
	RelationshipRequestState state;
	
}
