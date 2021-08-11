package my.ourShef.converter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

import my.ourShef.domain.constant.RelationshipRequestState;

@Converter(autoApply = true)
public class RelationshipRequestStateConverter implements AttributeConverter<RelationshipRequestState, String>{

	@Override
	public String convertToDatabaseColumn(RelationshipRequestState attribute) {
		return attribute.getCode();
	}

	@Override
	public RelationshipRequestState convertToEntityAttribute(String dbData) {
		
		return EnumSet.allOf(RelationshipRequestState.class)
				.stream()
				.filter(e->e.getCode().equals(dbData))
				.findFirst()
				.orElseThrow(()->new NoSuchElementException());
	}

	
}
