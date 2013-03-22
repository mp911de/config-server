package de.paluch.configserver.spring;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import de.paluch.configserver.model.config.ConfigRoot;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 13:50
 */
public class CustomSimpleTriggerFactory extends SimpleTriggerFactoryBean {

    private ConfigRoot configuration;

    @Override
    public void afterPropertiesSet() throws ParseException {

        long refresh = configuration.getConfigServerRepositories().getRefresh();
        TimeUnit timeUnit = configuration.getConfigServerRepositories().getRefreshUnit();
        setRepeatInterval(TimeUnit.MILLISECONDS.convert(refresh, timeUnit));
        super.afterPropertiesSet();
    }

    public ConfigRoot getConfiguration() {
        return configuration;
    }

    @Required
    public void setConfiguration(ConfigRoot configuration) {
        this.configuration = configuration;
    }
}
