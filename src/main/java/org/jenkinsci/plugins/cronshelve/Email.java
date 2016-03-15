package org.jenkinsci.plugins.cronshelve;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;
import hudson.model.User;

import java.util.*;
import java.util.logging.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import jenkins.model.*;


public class Email {
	private static final Logger LOGGER = Logger.getLogger(Email.class.getName());
	
	private AbstractProject job;
	private int days;
	
	public Email(AbstractProject job,int days){
		this.job = job;
		this.days= days;
		
	}
	
	public boolean sendOwnerEmail()
	{
		String jobUrl = null;
		String to     = null;
		try {
			jobUrl = this.job.getLastCompletedBuild().getEnvironment().get("JOB_URL").toString();
			to     = this.job.getLastCompletedBuild().getEnvironment().get("BUILD_USER_EMAIL").toString();
		} catch (IOException e) {
			LOGGER.warning("[cron-shelve] [Email] [error] "+ this.job.getName());

		} catch (InterruptedException e) {
			LOGGER.warning("[cron-shelve] [Email] [error] "+ this.job.getName());
		}	
		JenkinsLocationConfiguration location = JenkinsLocationConfiguration.get();
		String shelveUrl = location.getUrl()+"/shelvedProjects/";
		String host      = location.getUrl();
		String port      = "25";
		String from      = location.getAdminAddress();
		String subject   = "Jenkins Archived your job "+this.job.getName()+"\n";
		String message   = "Jenkins shelved job "+this.job.getName()+"\n"
				          +"last built by "+to+" "+this.days+" days ago. \n"
				          +"To un-shelve "+this.job.getName()+" please do the following:\n"
				          +"1) go to this url "+shelveUrl+"\n"
				          +"2) [check] the desired job to unshelve the project. \n"
				          +"3) [push] Unshelve Project button.\n"
				          +"4) go to your job "+jobUrl+"\n"
				          +"5) [push] Build Now \n"
				          +"Done!\n\n"
				          +"Jenkins will re-shelve this job if it hasn`t been built in "+this.days+" days\n";
		
		LOGGER.warning(message);
		if(to != null)
		{
			this.sendEmail(host, port, to, from, subject, message);
			return true;
		}
		return false;     
	}

	public void sendEmail(String host, String port, String to, String from, String subject, String message)
	{
		Properties mprops = new Properties();
		mprops.setProperty("mail.transport.protocol","smtp");
		mprops.setProperty("mail.host",host);
		mprops.setProperty("mail.smtp.port",port);
		Session lSession = Session.getDefaultInstance(mprops,null);
		MimeMessage msg = new MimeMessage(lSession);
		try {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setFrom(new InternetAddress(from));
			msg.setSubject(subject);
			msg.setText(message);
	        Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
