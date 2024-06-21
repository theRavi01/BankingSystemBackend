package com.banking.system.auth;

import com.banking.system.response.JwtAuthenticationResponse;
import com.banking.system.response.ResponseEntityObject;

import jakarta.mail.internet.MimeMessage;

import com.banking.system.request.Helper;
import com.banking.system.request.LoginRequest;
import com.banking.system.request.RegisterRequest;
import com.banking.system.request.UserReq;
import com.banking.system.mail.MailStructure;
import com.banking.system.mail.MailService;
import com.banking.system.entity.Role;
import com.banking.system.entity.User;
import com.banking.system.exception.UserException;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private JavaMailSender mailSender;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    public ResponseEntity<User> register(RegisterRequest registerRequest, String url) throws UserException {
        User user = new User();
        if(registerRequest.getId()!=0){
        	  user.setFirstName(registerRequest.getFirstName());
              user.setLastName(registerRequest.getLastName());
              user.setEmail(registerRequest.getEmail());
              LocalDateTime localDateTime = LocalDateTime.now();
              Date updatDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
              user.setUpdatedAt(updatDate);
              user.setAddress(registerRequest.getAddress());
              user.setState(registerRequest.getState());
              user.setCity(registerRequest.getCity());
              user.setPinCode(registerRequest.getPinCode());
        	userRepository.save(user);
        }
        Optional<User> isEmailExist = userRepository.findByEmail(registerRequest.getEmail());
		if(!isEmailExist.isEmpty()) {
			System.out.println(isEmailExist);
			throw new UserException("Email is already used with another account");
		}
		
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        LocalDateTime localDateTime = LocalDateTime.now();
        Date registrationDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date updatDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        user.setCreatedAt(registrationDate);
        user.setUpdatedAt(updatDate);
        user.setEnable(false);
        user.setOtp(UUID.randomUUID().toString());
        user.setAddress(registerRequest.getAddress());
        user.setState(registerRequest.getState());
        user.setCity(registerRequest.getCity());
        user.setPinCode(registerRequest.getPinCode());
        User newUser = userRepository.save(user);
        if(newUser!=null) {
        	sendEmail(newUser,url);
        }
        return new ResponseEntity<User> (user,HttpStatus.CREATED);
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword())
        );

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        var jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        return jwtAuthenticationResponse;
    }

	@Override
	public ResponseEntityObject forgotPassword(String mail) {
		try {
			Optional<User> user =userRepository.findByEmail(mail);
			User user1 = user.get();
			if(user.get().getEmail()==null) {
				return new ResponseEntityObject(false,"email not exist..");
			}
		
				Helper helper = new Helper();
				user1.setOtp(helper.getOtp()); 
				userRepository.save(user1);
				MailStructure mailStructure = new MailStructure();
				mailStructure.setSubject("Forget Password OTP");
				mailStructure.setMessage("Dear User You Banking System forget password OTP is: "+user1.getOtp());
				mailService.sendMail(mail,mailStructure);
				return new ResponseEntityObject(true,"successful");
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntityObject(false,"email not exist..");
		}
		
	}

	@Override
	public ResponseEntityObject resetPassword(String mail, String otp, UserReq req) {
		try {
	        Optional<User> userOptional = userRepository.findByEmail(mail);
	        if (userOptional.isPresent()) {
	            User user = userOptional.get(); // Retrieve the actual User object
	            if (user.getOtp().equals(otp)) {
	                user.setPassword(req.getPassword());
	                userRepository.save(user); // Assuming userRepo is meant to be userRepository
	                MailStructure mailStructure = new MailStructure();
	                mailStructure.setSubject("Alert Your Password Changed now");
	                mailStructure.setMessage("Dear User Banking System Account password has been changed now");
	                mailService.sendMail(mail, mailStructure);
	                return new ResponseEntityObject<>(true, "Success");
	            }
			}
			return new ResponseEntityObject<>(false,"Invalid otp");
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntityObject<>(false,"Invalid otp");
		}
	}

	@Override
	public void sendEmail(User user, String url) {
          String from ="ravikantpandey9125@gmail.com";
          String to = user.getEmail();
          String subject = "Account Verification";
          String content = "Dear [[name]],<br>"+"please click on link"
          +"f<h3><a href=\"[[URL]]\" target =\"_self\"> Verify</a></h3>"+"Thanks </br><p>Ravikant Pandey</>";
          
          try {
        	 MimeMessage message = mailSender.createMimeMessage();
        	 MimeMessageHelper helper = new MimeMessageHelper(message);
        	 
        	 helper.setFrom(from,"Ravi");
        	 helper.setTo(to);
        	 helper.setSubject(subject);
        	 
        	 content =content.replace("[[name]]",user.getFirstName()+" "+user.getLastName());
        	 String siteUrl = url + "/verify?code=" + user.getOtp();
        	 content = content.replace("[[URL]]",siteUrl);
        	 helper.setText(content,true);
        	 mailSender.send(message);
          }
          catch (Exception e) {
			
		}
	}

	@Override
	public boolean verifyAccount(String verificationCode) {
		User user = userRepository.findByOtp(verificationCode);
		if(user == null) {
			return false;
		}
		else {
		user.setEnable(true);
		user.setOtp(null);
		userRepository.save(user);
		return true;
		}
	}

}
