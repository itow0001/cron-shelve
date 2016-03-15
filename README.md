##Cron-Shelve Plugin##
###Management Page###
This plugin exposes a a new option in "Manage Jenkins" section.
The following options allow you to shelve jobs based on the last time a user has built a given job.
*             __enable__: The ability to toggle execution of the plugin.
*              __debug__: Prints all actions out to the system log.
*        __email owner__: If an owner is assigned to the given job an email will be sent out, this option requires the Ownership plugin is installed and ldap is enabled.
*      __Cron Schedule__: The given time period the shelving will begin.
*         __Jobs Regex__: This provides a regex method to harvest a given set of jobs.
* __Job Expiration day__: If the job has gone stale by a user, this is the given number of days before its considered for shelving.
![Alt text](docs/management.png?raw=true "Management Page")
