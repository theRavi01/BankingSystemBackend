package com.banking.system.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.system.account.AccountService;
import com.banking.system.auth.IAuthenticationService;
import com.banking.system.auth.IJwtService;
import com.banking.system.entity.Account;
import com.banking.system.entity.Role;
import com.banking.system.entity.Transaction;
import com.banking.system.entity.User;
import com.banking.system.exception.UserException;
import com.banking.system.request.RegisterRequest;
import com.banking.system.request.TransferRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    
	@Autowired
	private AccountService accountService;
	
    private final IAuthenticationService authenticationService;
	   
//    create new Account by SuperAdmin
    @PostMapping("create/account")
    public ResponseEntity<Account> newAccount(@RequestParam String email,@RequestBody Account account
    		) throws UserException {
    
        return ResponseEntity.ok(accountService.createAccount(email,account));
    }
   
// Transfer money     
        @PostMapping("/transfer")
        public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
            accountService.transferMoney(transferRequest);
            return ResponseEntity.ok("Transfer successful");
    }

    
        @GetMapping("/checkBalance")
        public ResponseEntity<?> checkBalance(@RequestParam String accountNumber) {
            BigDecimal balance = accountService.checkBalance(accountNumber);
            return ResponseEntity.ok(balance);
        }
        
        @GetMapping("/transactionHistory")
        public ResponseEntity<?> getTransactionHistory(@RequestParam String accountNumber) {
            List<Transaction> transactions = accountService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(transactions);
        }
    
        
        @GetMapping("path")
        public List<Account> customerAllAcounts(@RequestParam String email) {
            return accountService.customerAllAccount(email);
        }
        
    
}
