call c:\apache-tomcat-7.0.42\bin\shutdown.bat
call mvn clean package -pl robotics-lib,robotics-nlp,robotics-web -Dmaven.test.skip
rmdir /s /q c:\apache-tomcat-7.0.42\webapps\root
copy robotics-web\target\robotics-web.war c:\apache-tomcat-7.0.42\webapps\ROOT.war
call c:\apache-tomcat-7.0.42\bin\startup.bat