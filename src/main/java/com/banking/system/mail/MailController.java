package com.banking.system.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mail")
public class MailController {

	@Autowired
	private MailService mailService;
	
	@PostMapping("/sendmail/{mail}")
	public String sendMail(@PathVariable String mail,@RequestBody MailStructure mailStructure) {
		System.out.println("mail "+mail +" subject "+mailStructure.getSubject());
		mailService.sendMail(mail, mailStructure);
		return "done";
	}
}
