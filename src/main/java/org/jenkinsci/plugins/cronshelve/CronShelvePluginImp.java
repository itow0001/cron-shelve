package org.jenkinsci.plugins.cronshelve;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import hudson.model.AbstractProject;
import jenkins.model.Jenkins;

import hudson.Plugin;
/**
 * Responsible for plugin setup and persistence of values in CronShelveLink
 */
public class CronShelvePluginImp extends Plugin{
	private static final Logger LOGGER = Logger.getLogger(CronShelvePluginImp.class.getName());
	private boolean enable;
	private boolean debug;
	private boolean email;
	private String cron;
	private String regex;
	private int days;
	private String excludes;
	private static CronShelvePluginImp instance = null;
	private List<String> regexJobs;
  // constructor sets instance
  public CronShelvePluginImp() {
	    instance = this;
	  }
  
  @Override
  public void start() throws Exception {
    super.start();
    load();
    // at startup initialize listener with values
    CronExecutor cronListener = getCronExecutor();
    cronListener.setEnable(enable);
    cronListener.setDebug(debug);
    cronListener.setEmail(email);
    cronListener.setCron(cron);
    cronListener.setRegex(regex);
    cronListener.setDays(days);
    cronListener.setExcludes(excludes);
    setRegexJobs(regex,days,email,excludes,debug);
    LOGGER.warning("'Cron-Shelve' plugin initialized.");
  }
  // Provides a easy means to update values in cron listener/
  public CronExecutor getCronExecutor()
  {
  	return CronExecutor.getInstance();
  }
  
  // get the instance later
  public static CronShelvePluginImp getInstance() {
	    return instance;
	  }
  
  //setters
  public void setEnable(boolean enable)
  {
	  this.enable = enable;
  }
  public void setDebug(boolean debug)
  {
	  this.debug = debug;
  }
  public void setEmail(boolean email)
  {
	  this.email = email;
  }
  
  public void setCron(String cron)
  {
	  this.cron = cron;
  }
  
  public void setRegex(String regex)
  {
	  this.regex = regex;
  }
  
  public void setDays(int days)
  {
	  this.days = days;
  }
  
  public void setExcludes(String excludes)
  {
	  this.excludes = excludes;
  }
  
  public void setRegexJobs(String regex, int days,boolean email,String excludes,boolean debug)
  {
	List<String> jobs = new ArrayList<String>();
	Jenkins inst = Jenkins.getInstance();
	List<? extends AbstractProject> projects= inst.getItems(AbstractProject.class);
	ShelveExecutor shelver = new ShelveExecutor(regex,days,email,excludes,debug);
      for (AbstractProject project: projects)
      { 
    	  try{
			    boolean shelveable = shelver.isShelveable(project);
		        if(shelveable)
		        {
		        	jobs.add(freeStyleProject.getName());
		        }
    	     }
  		catch (Exception e) 
    	  {
				if(this.debug)
				{
				   LOGGER.warning("[ShelveExecutor] [Error] " + e.getMessage());
				}
    	  }
      }
	this.regexJobs = jobs;
  }
  
  //getters
  public boolean getEnable()
  {
	  return enable;
  }
  public boolean getDebug()
  {
	  return debug;
  }
  public boolean getEmail()
  {
	  return email;
  }
  public String getCron()
  {
	  return cron;
  }
  public String getRegex()
  {
	  return regex;
  }
  public int getDays()
  {
	  return days;
  }
  public String getExcludes()
  {
	  return excludes;
  }
  public List<String> getRegexJobs()
  {
	  return regexJobs;
  }
}
