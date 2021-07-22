package my.ourShef.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name="upload_file_info")
@Getter
public class UploadFileInfo {

	@Id @Column(name="upload_file_info_id", length = 36)
	private String id;
	
	@Column(name="upload_file_name")
	private String uploadFileName;
	
	@Column(name="store_file_name")
	private String storeFileName;
	
	
	protected UploadFileInfo() {
		
	}

	public UploadFileInfo(String uploadFileName, String storeFileName) {
		id = UUID.randomUUID().toString();
		this.uploadFileName = uploadFileName;
		this.storeFileName = storeFileName;
	}

}
