package my.ourShef.controller.dto;



import lombok.Data;


@Data
public class LoginUserDto {

	private Long id;
	private String nickName;
	private String introduction;
	private float reliability;
	
}
