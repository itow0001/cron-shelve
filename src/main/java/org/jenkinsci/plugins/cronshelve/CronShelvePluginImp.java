package org.jenkinsci.plugins.cronshelve;
import java.util.logging.Logger;

import hudson.Plugin;
/**
 * Responsible for Plugin setup and persistance of values in CronShelveLink
 */
public class CronShelvePluginImp extends Plugin{
	private static final Logger LOGGER = Logger.getLogger(CronShelvePluginImp.class.getName());
	private boolean enable;
	private boolean email;
	private String cron;
	private String regex;
	private int days;
	private static CronShelvePluginImp instance = null;
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
    cronListener.setEmail(email);
    cronListener.setCron(cron);
    cronListener.setRegex(regex);
    cronListener.setDays(days);
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
  
  //getters
  public boolean getEnable()
  {
	  return enable;
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
}
