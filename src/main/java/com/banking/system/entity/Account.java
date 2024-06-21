package com.banking.system.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class Account {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String accountNumber;
	    private BigDecimal balance;
	    private String accountType;
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "created_at", length = 19)
		private Date createdAt;
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "updated_at", length = 19)
		private Date updatedAt;
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
}
