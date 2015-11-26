package org.jenkinsci.plugins.cronshelve;
import hudson.Extension;
import hudson.model.AsyncPeriodicWork;
import hudson.model.TaskListener;

import java.util.logging.Logger;

import hudson.scheduler.CronTab;
import antlr.ANTLRException;
import hudson.Extension;
import hudson.model.AsyncPeriodicWork;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.logging.Logger;

import hudson.scheduler.CronTab;
/**
 * Responsible for performing periodic shelving of jobs via cron
 */
@Extension
public class CronExecutor extends AsyncPeriodicWork {

    private static final Logger LOGGER = Logger.getLogger(CronExecutor.class.getName());
    private static boolean enable;
    private static boolean debug;
    private static boolean email;
    private static String cron;
    private static String regex;
    private static int    days;
    private static String excludes;
    public CronExecutor() {
        super("CronExecutor");
    }

    @Override
    protected void execute(TaskListener taskListener) {
        String daysStr = Integer.toString(days);
        String enableStr = Boolean.toString(enable);
        if(debug)
        {
            LOGGER.warning("[CronExecutor] listener polling");
            LOGGER.warning("[CronExecutor] values -->\n cron: "+cron
            		+"\n regex: "+regex
            		+"\n days: "+daysStr
            		+"\n enable: "+enableStr
            		+"\n excludes: "+excludes);
        }
        if(cron != null && regex != null && enable) {
            try {
            	LOGGER.warning("[running] cron daemon");
                CronTab cronTab = new CronTab(cron);
                long currentTime = System.currentTimeMillis();
                if ((cronTab.ceil(currentTime).getTimeInMillis() - currentTime) == 0) {
                	ShelveExecutor shelveExec = new ShelveExecutor(regex,days,email,excludes,debug);
                	shelveExec.run();
                }
            } catch (ANTLRException e) {
                LOGGER.warning("[error] Could not parse provided cron tab" + e.getMessage());
            }
        }
        else {
            LOGGER.warning("[CronExecutor] all values not satisfied passing");
        }
    }
    
    // synchronized setters
    public synchronized static void setEnable(boolean aenable)
    {
    	enable = aenable;
    }
    public synchronized static void setDebug(boolean adebug)
    {
    	debug = adebug;
    }
    public synchronized static void setEmail(boolean aemail)
    {
    	email = aemail;
    }
    public synchronized static void setCron(String acron)
    {
    	cron = acron;
    }
    public synchronized static void setRegex(String aregex)
    {
    	regex = aregex;
    }
    public synchronized static void setDays(int aday)
    {
    	days = aday;
    }
    public synchronized static void setExcludes(String aexcludes)
    {
    	excludes = aexcludes;
    }
    
    @Override
    public long getRecurrencePeriod() {
        // Manages AsyncPeriodicWork polling options: MIN || HOUR || DAY
        return MIN;
    }
    // grab this obj if we need it
    public static CronExecutor getInstance() {
        return AsyncPeriodicWork.all().get(CronExecutor.class);
    }
}