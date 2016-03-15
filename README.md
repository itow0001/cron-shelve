##Cron-Shelve Plugin##
###Management Page###
This plugin exposes a new option in "Manage Jenkins" section called "Cron-Shelve". This plugin is great option in cases where the [Job DSL Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin) is used 
and a consistent naming convention is used to deploy several jobs.
*             __enable__: The ability to toggle execution of the plugin.
*              __debug__: Prints all actions out to the system log.
*        __email owner__: If an owner is assigned to the given job an email will be sent out, this option requires the [Ownership Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Ownership+Plugin) is installed.
*      __Cron Schedule__: The given time period the shelving will begin.
*         __Jobs Regex__: This provides a regex method to harvest a given set of jobs.
* __Job Expiration day__: If the job has gone stale by a user, this is the given number of days before its considered for shelving.
![Alt text](docs/management.png?raw=true "Management Page")
