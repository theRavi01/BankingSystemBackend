package com.banking.system.account;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.system.auth.IUserRepository;
import com.banking.system.entity.Account;
import com.banking.system.entity.Transaction;
import com.banking.system.entity.User;
import com.banking.system.rantsaction.TransactionRepository;
import com.banking.system.request.Helper;
import com.banking.system.request.TransferRequest;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public Account createAccount(String email, Account requestAccount) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Account account = new Account();
        Helper helper = new Helper();
        account.setAccountNumber(helper.getAccountNo());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountType(requestAccount.getAccountType());
        LocalDateTime localDateTime = LocalDateTime.now();
        Date createdDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date updatDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        account.setCreatedAt(createdDate);
        account.setUpdatedAt(updatDate);
        account.setUser(user);
        return accountRepository.save(account);
    }

    public BigDecimal checkBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account);
    }
    
    @Transactional
    public void transferMoney(TransferRequest transferRequest) {
        // Fetch source and destination accounts
        Account fromAccount = accountRepository.findByAccountNumber(transferRequest.getFromAccount())
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumber(transferRequest.getToAccount())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // Validate sufficient balance
        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Perform the transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequest.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transferRequest.getAmount()));

        // Save the updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create transaction records
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setType("DEBIT");
        debitTransaction.setAmount(transferRequest.getAmount());
        debitTransaction.setTimestamp(LocalDateTime.now());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccount(toAccount);
        creditTransaction.setType("CREDIT");
        creditTransaction.setAmount(transferRequest.getAmount());
        creditTransaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }
    
    public List<Account> customerAllAccount(String email){
    	Optional<User> user = userRepository.findByEmail(email);
    	long  id=user.get().getId();
    	return accountRepository.findByUserId(id);
    }

}
