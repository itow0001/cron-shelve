package org.jenkinsci.plugins.cronshelve;
import org.jvnet.hudson.plugins.shelveproject.ShelveProjectTask;

import hudson.model.AbstractProject;
import hudson.model.Queue.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import jenkins.model.Jenkins;

public class ShelveExecutor {
	private static final Logger LOGGER = Logger.getLogger(ShelveExecutor.class.getName());
	private String regex;
	private int    days;
	private boolean email;
	private boolean isRunning;
	private String excludes;
	private Task parentTask;
	private boolean debug;
	
	public ShelveExecutor(String regex, int days,boolean email,String excludes,boolean debug)
	{
		this.regex    = regex;
		this.days     = days;
		this.email    = email;
		this.excludes = excludes;
		this.debug    = debug; 
		
	}
	
	public void shelveJob(AbstractProject job)
	{
		if(this.debug)
		{
			LOGGER.warning("[shelving] job "+job.getName());
		}
		Jenkins.getInstance().getQueue().schedule(new ShelveProjectTask(job), 0);

	}
	
	public int getJobDurationDays(AbstractProject job)
	{
		if (job.getLastCompletedBuild() == null || job.isBuilding())
		{
			return -1;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date today         = new Date();
		Date lastBuildTime = job.getLastCompletedBuild().getTime();
		Long _duration     = getDuration(today,lastBuildTime); 
		int  duration      = Integer.valueOf(_duration.intValue());
		return duration;
		
	}
	public static long getDuration(Date today,Date lastBuildTime)
	{
		long diff = today.getTime() - lastBuildTime.getTime();
		
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public boolean isExpired(int days, int duration)
	{
		if(duration >= days)
		{
			return true;
		}
		return false;
	}
	
	public boolean isExcludes(AbstractProject job)
	{
		String lines[] = this.excludes.split("\\r?\\n");
		for(int i=0; i < lines.length; i++)
		{
			if(job.getName().equals(lines[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isShelveable(AbstractProject job)
	{
		int duration = getJobDurationDays(job);
		boolean expired = isExpired(this.days,duration);
		boolean exclude = isExcludes(job);
		Matcher match = Pattern.compile(this.regex).matcher(job.getName());
		
		/*
		if(this.debug)
		{
			LOGGER.warning("[isShelveable] "+job.getName()
					+"\n    expired: "+Boolean.toString(expired)
					+"\n   duration: "+Integer.toString(duration)
					+"\n    exclude: "+Boolean.toString(exclude)
					+"\nregex match: "+Boolean.toString(match.matches()));
		}
		*/
		
		if(exclude== false && expired==true && match.matches()==true)
		{
			return true;
		}
		return false;
	}
	
	public void run()
	{
		Jenkins inst = Jenkins.getInstance();
		List<AbstractProject> projects = inst.getItems(AbstractProject.class);
        for (AbstractProject project : projects) {
        		try{
        			    boolean shelveable = isShelveable(project);
        			    if(this.debug)
        			    {
        			    	LOGGER.warning("isShelveable: "+Boolean.toString(shelveable));
        			    }
		            	if(shelveable)
		            	{
			            	shelveJob(project);
			            	if(this.email)
			            	{
			            		Email email = new Email(project,this.days);
			            		email.sendOwnerEmail();
			            	}
		            	}           
        		   }
        		catch (Exception e) {
	        			if(this.debug)
	        			{
	        			   LOGGER.warning("[ShelveExecutor] [Error] " + e.getMessage());
	        			}
        		   }	
        }
	}
}













