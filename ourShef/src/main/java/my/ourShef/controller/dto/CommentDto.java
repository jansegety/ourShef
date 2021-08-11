package my.ourShef.controller.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDto {
	String commentUserNickName;
	String commentUserProfileImgStoreName;
	float starPoint;
	String commentSummary;
	String commentDetail;
	private LocalDateTime registeredTime;
}
