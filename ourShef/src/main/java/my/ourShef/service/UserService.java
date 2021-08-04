package my.ourShef.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ourShef.domain.UploadFileInfo;
import my.ourShef.domain.User;
import my.ourShef.repository.UploadFileInfoRepository;
import my.ourShef.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final UploadFileInfoRepository uploadFileInfoRepository;
	
	
		@Transactional
		public Long join(User user) throws Exception{
			validateDuplicateAccountId(user.getAccountId());
			
		
			userRepository.save(user);
	
			return user.getId();
			
		}
		
		public void validateDuplicateAccountId(String accountId) throws Exception{
			Optional<User> findUser = userRepository.findByAccountId(accountId);
			if(!findUser.isEmpty()) {
				throw new Exception(new IllegalStateException("이미 존재하는 계정아이디입니다."));
			}
		}
		
		public List<User> findAll() {
			return userRepository.findAll();
		}
		
		public Optional<User> findById(Long userId) {
			return userRepository.findById(userId);
		}
		
		public Optional<User> findByAccountId(String userAccountId) {
			return userRepository.findByAccountId(userAccountId);
		}
		

		@Transactional
		public void update(Long id, String nickName, String password, UploadFileInfo profileImgInfo ) {
			try {
			User findUser = userRepository.findById(id).orElseThrow(()->(new IllegalStateException("존재하지 않는 ID입니다.")));
			findUser.setNickName(nickName);
			findUser.setPassword(password);
			findUser.setProfileImgInfo(profileImgInfo);
			
			}
			catch (IllegalStateException e) {
				
				log.info("변경된 유저가 없습니다. {}", e.getMessage());
				return;
			}
	
		}
	

}
