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

def isTestEnv = System.getProperty("spring.profiles.active") == 'test'

logger "org.springframework.boot", isTestEnv ? WARN : INFO
logger "org.eclipse.jetty", isTestEnv ? WARN : INFO
logger "org.glassfish.jersey", isTestEnv ? WARN : INFO

logger "com.github.robi42.boot", isTestEnv ? WARN : DEBUG

root WARN, ["STDOUT"]
