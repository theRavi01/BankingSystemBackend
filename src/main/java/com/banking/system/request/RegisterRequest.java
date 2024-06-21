package com.banking.system.request;



import com.banking.system.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
	private String state;
	private String city;
	private String address;
	private int pinCode;
}
