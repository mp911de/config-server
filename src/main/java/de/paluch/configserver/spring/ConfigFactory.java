package de.paluch.configserver.spring;

import de.paluch.configserver.model.config.ConfigRoot;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.xml.bind.JAXB;
import java.net.URL;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:28
 */
public class ConfigFactory extends AbstractFactoryBean<ConfigRoot> {

    private URL configLocation;

    @Override
    public Class<?> getObjectType() {
        return ConfigRoot.class;
    }

    @Override
    protected ConfigRoot createInstance() throws Exception {
        return JAXB.unmarshal(getConfigLocation(), ConfigRoot.class);
    }

    public URL getConfigLocation() {
        return configLocation;
    }

    @Required
    public void setConfigLocation(URL configLocation) {
        this.configLocation = configLocation;
    }
}
