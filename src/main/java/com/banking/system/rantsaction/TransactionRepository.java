package com.banking.system.rantsaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.system.entity.Account;
import com.banking.system.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	 List<Transaction> findByAccount(Account account);
}
