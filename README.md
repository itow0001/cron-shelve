##Cron-Shelve Plugin##
###Management Page###
The cron-shelve plugin allows you to harvest hundreds of stale jobs in consistent automated manner.
This plugin exposes a new option in "Manage Jenkins" section called "Cron-Shelve". This plugin is a great option in cases where the [Job DSL Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin) is used 
and a consistent naming convention is maintained to deploy hundreds of jobs. 
*             __enable__: The ability to toggle execution of the plugin.
*              __debug__: Output plugin state to logs.
*        __email owner__: If an owner is assigned to the given job an email will be sent out, this option requires the [Ownership Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Ownership+Plugin) is installed.
*      __Cron Schedule__: The given time period the shelving will begin.
*         __Jobs Regex__: This provides a regex method to harvest a given set of jobs.
* __Job Expiration day__: Check the last build date of all jobs matching the regex value and if its equal or greater to job Expiration day shelve the job.
![Alt text](docs/management.png?raw=true "Management Page")

###Build Instructions###
* mvn dependency:copy-dependencies
* mvn clean install
* go here for the installation item: cron-shelve/target/cron-shelve.hpi 

###Further Info###
[Jira Tickets TBD](https://www.google.com/)
[MIT License](https://opensource.org/licenses/mit-license.php)
