call c:\apache-tomcat-7.0.42\bin\shutdown.bat
call mvn clean package -pl train-robots-core,train-robots-nlp,train-robots-web
rmdir /s /q c:\apache-tomcat-7.0.42\webapps\root
copy train-robots-web\target\train-robots-web.war c:\apache-tomcat-7.0.42\webapps\ROOT.war
call c:\apache-tomcat-7.0.42\bin\startup.bat