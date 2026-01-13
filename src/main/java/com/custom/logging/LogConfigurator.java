package com.custom.logging;

import org.springframework.beans.factory.InitializingBean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LogConfigurator implements InitializingBean {

    private String packageName;
    private String logLevel;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        updateLogLevel(this.packageName, this.logLevel);
    }

    private void updateLogLevel(String loggerName, String levelName) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        
        LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
        
        // If no specific config exists for this logger, create one
        if (!loggerConfig.getName().equals(loggerName)) {
            loggerConfig = new LoggerConfig(loggerName, Level.valueOf(levelName.toUpperCase()), true);
            config.addLogger(loggerName, loggerConfig);
        } else {
            loggerConfig.setLevel(Level.valueOf(levelName.toUpperCase()));
        }
        
        ctx.updateLoggers(config);
        System.out.println("Mule Startup: Log level for '" + loggerName + "' set to " + levelName);
    }
}