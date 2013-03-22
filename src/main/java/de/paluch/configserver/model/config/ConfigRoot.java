package de.paluch.configserver.model.config;


import javax.xml.bind.annotation.*;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:23
 */
@XmlRootElement(name="configServer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigRoot {


    @XmlElement(name="repositories")
    private ConfigServerRepositories configServerRepositories;


    public ConfigServerRepositories getConfigServerRepositories() {
        return configServerRepositories;
    }

    public void setConfigServerRepositories(ConfigServerRepositories configServerRepositories) {
        this.configServerRepositories = configServerRepositories;
    }
}
