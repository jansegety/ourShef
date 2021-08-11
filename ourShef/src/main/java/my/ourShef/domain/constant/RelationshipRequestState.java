package my.ourShef.domain.constant;

public enum RelationshipRequestState implements CodeValue{

	BEFORE_CONFIRMATION("BEFORE_CONFIRMATION","확인전"), ACCEPTED("ACCEPTED","수락됨"), REJECTED("REJECTED","거절됨");

	private String code;
	private String value;
	
	private RelationshipRequestState(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	
	
	
}
