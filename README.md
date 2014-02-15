Boot REST
=========

Ensure having JDK 8 (preview) installed.

Get it here: https://jdk8.java.net/download.html

Then, e.g.:

    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

To build & run via command line shell:

    ./gradlew clean build && java -server -jar build/libs/boot-rest.jar

Or simply run `ApplicationInitializer.main()` via IDEA (min. 13.1 EAP, BTW).

An endpoint to play with:

    curl localhost:8888/api/greetings

Note: this thing is ready to be deployed on Heroku (and verified to run packaged as WAR in Tomcat 8, too)...
