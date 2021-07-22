package my.ourShef.domain.bridge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import my.ourShef.domain.Spot;
import my.ourShef.domain.UploadFileInfo;

@Table(name="added_spot_img")
@Entity
@Getter
public class AddedSpotImg {

	@Id @GeneratedValue
	@Column(name="added_spot_img_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "spot_id")
	private Spot spot;
	
	@OneToOne
	@JoinColumn(name ="upload_file_info_id")
	private UploadFileInfo uploadFileInfo;
	
	
	protected AddedSpotImg() {
		
	}

	public AddedSpotImg(Spot spot, UploadFileInfo uploadFileInfo) {
		this.spot = spot;
		this.uploadFileInfo = uploadFileInfo;
	}
	
	
	
	
}
