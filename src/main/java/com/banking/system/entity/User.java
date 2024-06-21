package com.banking.system.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class User implements UserDetails {
	
		@Id
		@Column(name = "user_id")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
		@GenericGenerator(strategy = "native", name = "native")
		private long id;
		
	    @Column(name = "first_name")
	    private String firstName;
		
		@Column(name = "last_name")
		private String lastName;

		@Column(nullable = false ,name = "email", unique = true)
		private String email;
		
		@Column(nullable = false ,name="password")
		private String password;
		
		@Column(name = "phone")
		private String phone;
		
		
		@Column(name = "profile_img_url")
		private String profileImgUrl;
		
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "created_at", length = 19)
		private Date createdAt;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "updated_at", length = 19)
		private Date updatedAt;
		
		private Role role;
		
		@Column(name ="gender")
		private String gender;
		
		@Column(name ="otp")
		private String otp;
		
		@Temporal(TemporalType.DATE)
		@Column(name = "dob", length = 19)
		private Date dob;
		private String state;
		private String city;
		private String address;
		private int pinCode;
		private boolean enable;
		
		public boolean isEnable() {
			return enable;
		}

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return List.of(new SimpleGrantedAuthority(role.name()));
	    }

	    @Override
	    public String getUsername() {
	        return email;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return isEnable();
	    }
	}

