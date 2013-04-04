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
             <security>
                 <encryption id="aes1">
                     <cipher>AES/CBC/PKCS5Padding</cipher>
                     <key>BTwdBhcFJd5Ls7DW82oTuQ==</key>
                     <ivSpec>ZB0zQr9c9LbtMHS8fgkfKA==</ivSpec>
                 </encryption>
             </security>
         </repository>
     </repositories>
 </configServer>
```

* type: GIT or SVN
* refresh: Update interval for all repositories. Number of seconds/minutes/days, see refreshUnit
* refreshUnit: SECONDS, MINUTES, DAYS
* localWorkDirectory: the temporary, local storage
* remoteUrl: Remote repository URL

* security: Optional configuration for sensitive data encryption/decryption
* encryption: One or more encryption configurations
* encryption.id: Identifier (public reference id)
* encryption.cipher: Cipher spec (see security section)
* encryption.key: Cipher key/secret, Base64 encoded
* encryption.ivSpec: initialization vectors, optional

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


**GET /repositories/{repositoryId}/{artifactId}/{version}/{environment}/{filename}**

Static access to a file. This url points to the real resource and returns the file content.


**POST /repositories/{repositoryId}**

Force a repository update.

**POST repositories/{repositoryId}/encryptions/{encryptionId}**

Encrypt a value (POST either using Form/URL-Encoded, Field plaintext or JSON,
then the whole body is used as plaintext).


It's cluster
-------------
Since configuration data is somehow essential for applications you surely want to run multiple config-servers in
order to be failsafe. On the other hand, the config data is updated on a regular base. In case you force the update,
this usually would happen on one node, the data on the others would become stale. To avoid this,
the config-server incorporates a distributed updating based on JGroups. If you want,
you can set the multicast address/port setting the System Properties `jgroups.udp.mcast_addr` and `jgroups.udp.mcast_port`

It's secure
------------
This config server allows you to store and publish sensitive data. This is done by decryption on the fly. You simply
encrypt your data (posting it to the encryption resource), store the crypted result within your config files and your
SVN/GIT contains no human readable passwords etc. anymore.

The config server itself discovers on request, that you have encrypted values and tries to decrypt it.

You need to configure the `<security>` section within the config file in order to use encryption. You can define one
or more encryption elements (with different keys, ciphers) which are identified by the `id` attribute. This `id`
attribute is the reference for encryption and decryption. When encrypting,
the output is formatted as `{aes:aBe9q8rNeYDpps2GJ6ZeQg==}`. Just drop this as value into your config file (property
file format currently supported only). It's not possible to use this as part of a value,
the value must be **exactly** the encrypted result.

See http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#impl for possible ciphers,
modes and padding combinations.

The config server code contains also a key generator for the different encryption schemes,
see and run `CipherKeyGenTest`.




