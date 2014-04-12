Boot REST
=========

Ensure having JDK 8 installed.

Get it here:<br>
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Then, e.g.:

    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

To build & run via command line shell:

    ./gradlew clean build && java -server -jar build/libs/boot-rest.jar

Or simply run `ApplicationInitializer.main()` via IDEA (min. 13.1 with installed Lombok plugin recommended).

An endpoint to play with:

    curl localhost:8888/api/messages

SPA frontend UI resides at root URL, BTW.<br>
Admin endpoints, powered by Spring Boot, at:<br>
`/admin/*` (auth-protected)
REST API docs, powered by Swagger (UI), at:<br>
`/api-docs/index.html`

Note: this thing is ready to be deployed on Heroku (and verified to run packaged as WAR in Tomcat 8 as `ROOT` context).

FYI: using Spring Data Elasticsearch as embedded data source provider for fun.

PS: Bootstrap/Angular SPA is developed & built with the help of Yeoman, Grunt, and Bower...
    So, you'll need to have Node.js/NPM as well as Bower and Grunt CLI (globally) installed.
