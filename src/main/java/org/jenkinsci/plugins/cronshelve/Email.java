package org.jenkinsci.plugins.cronshelve;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import hudson.model.AbstractProject;
import hudson.tasks.Mailer;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import jenkins.model.*;

public class Email {
	private static final Logger LOGGER = Logger.getLogger(Email.class.getName());
	
	private AbstractProject job;
	private int days;
	
	public Email(AbstractProject job,int days){
		this.job = job;
		this.days= days;
	}
	public boolean sendOwnerEmail() throws UnknownHostException, MessagingException
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
		String shelveUrl = location.getUrl()+"shelvedProjects/";
		String subject   = "Jenkins Archived your job "+this.job.getName();
		String message   = "Jenkins shelved job "+this.job.getName()+"<BR/>"
				          +"last built by "+to+" "+this.days+" days ago. <BR/>"
				          +"To un-shelve "+this.job.getName()+" please do the following:<BR/>"
				          +"1) go to this url "+shelveUrl+"<BR/>"
				          +"2) [check] the desired job to unshelve the project. <BR/>"
				          +"3) [push] Unshelve Project button.<BR/>"
				          +"4) go to your job "+jobUrl+"<BR/>"
				          +"5) [push] Build Now <BR/>"
				          +"Done!<BR/><BR/>"
				          +"Jenkins will re-shelve this job if it hasn`t been built in "+this.days+" days<BR/>";
		LOGGER.warning(message);
		if(to != null)
		{
			MimeMessage msg = new MimeMessage(Mailer.descriptor().createSession());
			Set<InternetAddress> rcp = new LinkedHashSet<InternetAddress>();
			rcp.add(new InternetAddress(to));
	        msg.setContent(message, "text/html"); 
	        msg.setFrom(new InternetAddress(Mailer.descriptor().getAdminAddress()));
	        msg.setRecipients(Message.RecipientType.TO, rcp.toArray(new InternetAddress[rcp.size()]));
	        msg.setSentDate(new Date());
	        msg.setSubject(subject);
	        Transport.send(msg);
	        return true;
		}
		return false;
	}
}