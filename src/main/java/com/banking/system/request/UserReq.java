package com.banking.system.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserReq {

	private String firstName;
	private String lastName;
	
	@Email
	private String email;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$" ,message="please enter password which have atleast 8 characters including a lowercase and a uppercase and a special letter..")
	private String password;
	private String otp;
	private String phone;
	private String profileImgUrl;
}
