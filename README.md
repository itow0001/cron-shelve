##Cron-Shelve Plugin##
###Management Page###
This plugin exposes a a new option in "Manage Jenkins" section.
The following options allow you to shelve jobs based on the last time a user has built a given job.
*             _enable_: The ability to toggle execution of the plugin.
*              _debug_: Prints all actions out to the system log.
*        _email owner_: If an owner is assigned to the given job an email will be sent out, this option requires the Ownership plugin is installed and ldap is enabled.
*      _Cron Schedule_: The given time period the shelving will begin.
*         _Jobs Regex_: This provides a regex method to harvest a given set of jobs.
* _Job Expiration day_: If the job has gone stale by a user, this is the given number of days before its considered for shelving.
![Alt text](docs/management.png?raw=true "Management Page")
