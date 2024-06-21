package com.banking.system.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Transaction {
	  @Id 
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String type;
	    private BigDecimal amount;
	    private LocalDateTime timestamp;
	    
	    @ManyToOne
	    @JoinColumn(name = "account_id")
	    private Account account;
}
