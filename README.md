Configuration Server
=============

This configuration server provides you an unified access to config files within a SCM repository (currently SVN and
Git). The config server identifies the caller/requestor by it's DNS (reverse) and expects a mapping.

The configuration has to be done within `config.xml` (your repositories).


config.xml
-------------

```xml
 <configServer>
     <repositories refresh="60" refreshUnit="MINUTES">
         <repository id="myrepo" type="SVN">
             <localWorkDirectory>/Entwicklung/cfs/myrepo</localWorkDirectory>
             <remoteUrl>https://svn.server.url/svn/repor</remoteUrl>
             <username>username</username>
             <password>password</password>
         </repository>
     </repositories>
 </configServer>
```

* type: GIT or SVN
* refresh: Update interval for all repositories. Number of seconds/minutes/days, see refreshUnit
* refreshUnit: SECONDS, MINUTES, DAYS
* localWorkDirectory: the temporary, local storage
* remoteUrl: Remote repository URL

Idea behind
-------------
Configuration of applications (especially the different environments) is often a pain. As soon as it comes to
packaged applications (EAR's, WAR's) you can either use System Properties, Databases or package the config within the
application. Not nice.

The config server is an approach to decouple configuration and provide a convenient way. The config server will allow
you to use a semi-static URL for your configuration-data, the real tweak is: The environment (Dev, QA,
Production) is determined by the config server itself. How? Address/hostname-mapping to environment. Afterwards the
config stuff is accessed by a simple HTTP GET.

Folder layout
-------------
You'll store your configuration within your SCM (SVN or Git) in a layout of
ARTIFACT/VERSION/ENVIRONMENT/(here come the files) and you configure a
set of hostnames to the environment. Now, I expect that a application system runs only one environment at once (QA,
Dev, Prod, UAT, ...). This allows an mapping between hostname and environment.

Now let's take a look at versions.
Usually your app knows it's version somehow. So this is aswell something, which is static for a certain release. So
you could build up nearly always an URL which containts your project-id, the version and the name of the config file
you need. Something like:

http://my.config.server/dns/{repositoryId}/{artifactId}/{version}/{filename}


It's HTTP
-------------
The config-server provides currently following paths for accessing:

**GET /dns/{repositoryId}/{artifactId}/{version}/{filename}**

Retrieval of a file by repository/artifact/version and filename. The environment will be pulled from the clients' IP
address. In a successful request the response performs a redirect to the real resource on the server.

**GET /dns**

Returns the caller/client IP address with hostname/FQDN. Very handy to find out your DNS reverse name.


**GET /repositories/{repositoryId}/{artifactId}/{environment}/{version}/{filename}**

Static access to a file. This url points to the real resource and returns the file content.


**POST /repositories/{repositoryId}**

Force a repository update.


It's cluster
-------------
Since configuration data is somehow essential for applications you surely want to run multiple config-servers in
order to be failsafe. On the other hand, the config data is updated on a regular base. In case you force the update,
this usually would happen on one node, the data on the others would become stale. To avoid this,
the config-server incorporates a distributed updating based on JGroups. If you want,
you can set the multicast address/port setting the System Properties `jgroups.udp.mcast_addr` and `jgroups.udp.mcast_port`


