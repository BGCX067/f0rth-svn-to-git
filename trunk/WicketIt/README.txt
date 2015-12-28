Maven
==========

Download and install Maven 2 from http://maven.apache.org/.

JTA is not available on maven reopsitory, you can download JTA from http://java.sun.com/products/jta/,
or use the jta.jar comes with Hibernate(http://www.hibernate.org/):
mvn install:install-file -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar -Dfile=/path/to/jta.jar

Proxool 0.9.0RC3(http://proxool.xf.net/) also need to be installed manually:
mvn install:install-file -DgroupId=proxool -DartifactId=proxool -Dversion=0.9.0RC3 -Dpackaging=jar -Dfile=/path/to/proxool-0.9.0RC3.jar

To start Jetty web server with Maven, run:
mvn jetty:run

use browser to open http://localhost:8080/app.

If you are behind a firewall, you can specify a proxy in /path/to/.m2/settings.xml:
<settings>
  <proxies>
   <proxy>
      <active>true</active>
      <protocol>http</protocol>
      <host>192.168.1.1</host>
      <port>80</port>
    </proxy>
  </proxies>
</settings>

Eclipse
==========

Added M2_REPO to Eclipse, inside the workspace directory, there should have a .metadata directory:
mvn -Declipse.workspace=/path/to/workspace eclipse:add-maven-repo

Generate .project and .classpath for Eclipse:
mvn eclipse:eclipse

Start Eclipse and import the project. When coding inside Eclipse, Jetty web server can be started by
running util.JettyServer, and modifying codes requires no web server restart.

Jetty
==========

Copy src/config/jetty.xml to $JETTY_HOME/contexts/ and correct the resourceBase
in the XML file. When remote debugging, run:

java -jar start.jar -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n

Tomcat
==========

Copy src/config/tomcat.xml to $CATALINA_HOME/conf/Catalina/localhost/ and use
context name as file name then correct the docBase in the XML file. When remote debugging,
set environment variables JPDA_TRANSPORT to dt_socket and JPDA_ADDRESS to 8000 then
run:

catalina jpda start

Database
==========

The default database configuration is for PostgreSQL. To use other DBMS, please
modify config.properties.

The hibernate.cfg.xml is used so that Eclipse user can use Hibernate Tools.