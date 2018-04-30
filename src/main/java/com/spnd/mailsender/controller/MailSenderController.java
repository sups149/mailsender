package com.spnd.mailsender.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.spnd.mailsender.dto.MailDetailsDTO;


//https://www.quickprogrammingtips.com/spring-boot/how-to-send-email-from-spring-boot-applications.html
@RestController
@RequestMapping("/mailsender")
public class MailSenderController {
	Logger logger = Logger.getLogger(this.getClass());
	//Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	JavaMailSender sender;
	
	@RequestMapping(method=RequestMethod.GET, value="/mails")
	public List<MailDetailsDTO> getAllMails() {
		if(logger.isInfoEnabled()) {
			logger.info("Inside getAllMailsMethod");
		}
		logger.error("Inside getAllMailsMethod");
		List<MailDetailsDTO> mailDetailsList = new ArrayList<>();
		MailDetailsDTO mailDetails = new MailDetailsDTO();
		mailDetails.setMailId("sups@gmail.com");
		mailDetails.setFromMailId("no-reply@gmail.com");
		mailDetails.setMailBody("Hello all");
		mailDetailsList.add(mailDetails);
		
		return mailDetailsList;
	}
	
	@RequestMapping(value="/send", method=RequestMethod.GET)
	public String sendMail() throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo("sups149@gmail.com");
		helper.setText("Hello");
		helper.setSubject("Test");
		
		sender.send(message);
		
		return "Mail sent";
	}
	
	@RequestMapping(value="/send", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String sendMail(@RequestBody MailDetailsDTO mailDetails) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		boolean multipartFlag=true;
		MimeMessageHelper helper = new MimeMessageHelper(message, multipartFlag);
		helper.setTo(mailDetails.getMailId());
		helper.setSubject(mailDetails.getMailSubject());
		helper.setText(mailDetails.getMailBody());
		helper.setFrom(mailDetails.getFromMailId());
		if(mailDetails.getAttachmentName()!= null) {
			File attachment = new File(mailDetails.getAttachmentPath());
			//String filePath = "C:\\Users\\sups\\Desktop";
			helper.addAttachment(mailDetails.getAttachmentName(), attachment);
		}
		
		sender.send(message);
		
		return message.getMessageID();
		
	}
}
