import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

appender("STDOUT", ConsoleAppender) {
    filter(ThresholdFilter) {
        level = DEBUG
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %-5level [%thread] %logger{10} - [%mdc] - %msg%n"
    }
}

logger "org.springframework.boot", INFO
logger "org.eclipse.jetty", INFO
logger "org.glassfish.jersey", INFO

logger "com.github.robi42.boot", DEBUG

root WARN, ["STDOUT"]
