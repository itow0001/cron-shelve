package org.jenkinsci.plugins.cronshelve;
//import org.jvnet.hudson.plugins.shelveproject.ShelveProjectExecutable;
import org.jvnet.hudson.plugins.shelveproject.ShelveProjectTask;
//import org.jenkins-ci.plugins.shelveproject.ShelveProjectTask;
import hudson.model.Queue;
import hudson.model.Hudson;
import hudson.model.AbstractProject;
import hudson.model.Queue.Task;
import hudson.model.FreeStyleProject;
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
	private Task parentTask;
	
	public ShelveExecutor(String regex, int days,boolean email)
	{
		this.regex = regex;
		this.days  = days;
		this.email = email;
	}
	
	public void shelveJob(AbstractProject job)
	{
		LOGGER.warning("[shelving] job "+job.getName());
		//ShelveProjectExecutable shelveIt = new ShelveProjectExecutable (parentTask,job);
		Jenkins.getInstance().getQueue().schedule(new ShelveProjectTask(job), 0);
		//shelveIt.run();
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
		LOGGER.warning("[getJobDurationDays]");
		LOGGER.warning("         [today] "+dateFormat.format(today));
		LOGGER.warning(" [lastBuildTime] "+dateFormat.format(lastBuildTime)+" of job " + job.getName());
		LOGGER.warning("      [duration] "+Integer.toString(duration));
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
	
	public void run()
	{
		Hudson inst =Hudson.getInstance();
		List<FreeStyleProject> freeStyleProjects = inst.getItems(FreeStyleProject.class);
		LOGGER.warning("[ShelveExecutor] running");
        for (FreeStyleProject freeStyleProject : freeStyleProjects) { 
        		try{
        			int duration = getJobDurationDays(freeStyleProject);
        			boolean expired = isExpired(this.days,duration);
        			LOGGER.warning("[ShelveExecutor] "+freeStyleProject.getName()+" isExpired: "+Boolean.toString(expired)+" duration: "+Integer.toString(duration));
	        			if(expired)
	        			{
			        		Matcher m = Pattern.compile(this.regex).matcher(freeStyleProject.getName());
				            if (m.matches()) { 
				            	LOGGER.warning("[ShelveExecutor] regex match found job"+freeStyleProject.getName());
				            	shelveJob(freeStyleProject);
				            	if(email)
				            	{
				            		Email email = new Email(freeStyleProject,this.days);
				            		email.sendOwnerEmail();
				            	}
				            }
	        			}
        		   }
        		catch (Exception e) {
        			   LOGGER.warning("[ShelveExecutor] [Error] " + e.getMessage());
        		   }	
        }
	}
}













