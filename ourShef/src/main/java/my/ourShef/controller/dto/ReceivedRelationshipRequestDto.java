package my.ourShef.controller.dto;

import lombok.Data;
import my.ourShef.domain.constant.RelationshipRequestState;

@Data
public class ReceivedRelationshipRequestDto implements Comparable<ReceivedRelationshipRequestDto>{

	
	String id;
	String fromUserNickName;
	RelationshipRequestState state;
	
	@Override
	public int compareTo(ReceivedRelationshipRequestDto rrrd) {
		
		if(rrrd.state.ordinal() < this.state.ordinal())
		{
			return 1;
		}
		else if(rrrd.state.ordinal() > this.state.ordinal())
		{
			return -1;
		}
		else
		{
			return 0;
		}
		
	}
	
	
}
