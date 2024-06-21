package com.banking.system.request;

import java.security.SecureRandom;

import lombok.Data;

public @Data class Helper {

	 private  String  search  =  ""  ; 
	 private  String  searchBy  =  ""  ; 
	 private  int  currentPage  = 0; 
	 private  int  itemsPerPage  = 0; 
	 private  String  sortBy  ; 
	 private  String  sortOrder  ;
	 
		public String getOtp() {
			String otp = "";
			SecureRandom rand = new SecureRandom();
			for (int i = 0; i < 6; i++) {
				int n = rand.nextInt(9) ;
				if(i==0&&n==0) {
					n=1;
				}
				otp = otp.concat(String.valueOf(n));
			}
			return otp;
		}
		
		public String getAccountNo() {
			String accountNO = "";
			SecureRandom r = new SecureRandom();
			for(int i = 0; i < 12; i++) {
				int n = r.nextInt(9);
				if(i == 0 && n == 0) {
					n=1;
				}
				accountNO = accountNO.concat(String.valueOf(n));
			}
			return accountNO;
		}
}