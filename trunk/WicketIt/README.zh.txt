Maven
==========

下載並安裝Maven 2(http://maven.apache.org/).

Maven檔案庫沒有JTA，必須從http://java.sun.com/products/jta/下載，或用Hibernate
(http://www.hibernate.org/)裡的jta.jar：
mvn install:install-file -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar -Dfile=/path/to/jta.jar

Proxool 0.9.0RC3(http://proxool.xf.net/)也要自行下載及安裝:
mvn install:install-file -DgroupId=proxool -DartifactId=proxool -Dversion=0.9.0RC3 -Dpackaging=jar -Dfile=/path/to/proxool-0.9.0RC3.jar

用Maven啟動Jetty web伺服器：
mvn jetty:run

用瀏覽器開啟http://localhost:8080/app。

如果你在firewall後面，可以在/path/to/.m2/settings.xml設定代理伺服器：
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

將M2_REPO加入Eclipse，在workspace目錄中應該有.metadata目錄：
mvn -Declipse.workspace=/path/to/workspace eclipse:add-maven-repo

產生Eclipse需要的.project和.classpath：
mvn eclipse:eclipse

執行Eclipse並匯入專案。在Eclipse裡寫程式時，可以執行util.JettyServer啟動Jetty web伺服器，這樣就不用為了測試修改的程式
重新啟動web伺服器。

Jetty
==========

複製src/config/jetty.xml至$JETTY_HOME/contexts/並將XML檔中的resourceBase改成正確路徑。遠端除錯時執行：

java -jar start.jar -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n

Tomcat
==========

複製src/config/tomcat.xml至$CATALINA_HOME/conf/Catalina/localhost/，並以context名稱為檔名，然後將XML檔中的docBase
改成正確路徑。遠端除錯時將環境變數JPDA_TRANSPORT設為dt_socket、JPDA_ADDRESS設為8000，然後執行：

catalina jpda start

Database
==========

資料庫預設使用PostgreSQL，若要用其它DBMS，請修改config.properties。

Hibernate相關設定儲存於hibernate.cfg.xml，以便讓Eclipse使用者可以用Hibernate Tools，
