package my.ourShef.controller.dto;

import lombok.Data;
import my.ourShef.domain.constant.RelationshipRequestState;

@Data
public class SendedRelationshipRequestDto implements Comparable<SendedRelationshipRequestDto>{

	String id;
	String toUserNickName;
	RelationshipRequestState state;
	
	@Override
	public int compareTo(SendedRelationshipRequestDto srrd) {
		
		if(srrd.state.ordinal() < this.state.ordinal())
		{
			return 1;
		}
		else if(srrd.state.ordinal() > this.state.ordinal())
		{
			return -1;
		}
		else
		{
			return 0;
		}
		
	}
	
}
