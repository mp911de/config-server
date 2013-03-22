package de.paluch.configserver.model.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:25
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigServerRepositories {

    @XmlAttribute(name = "refresh")
    private int refresh = 60;

    @XmlAttribute(name = "refreshUnit")
    private TimeUnit refreshUnit = TimeUnit.MINUTES;

    @XmlElement(name = "repository")
    private List<ConfigServerRepository> repositories = new ArrayList<ConfigServerRepository>();

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public List<ConfigServerRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<ConfigServerRepository> repositories) {
        this.repositories = repositories;
    }

    public TimeUnit getRefreshUnit() {
        return refreshUnit;
    }

    public void setRefreshUnit(TimeUnit refreshUnit) {
        this.refreshUnit = refreshUnit;
    }
}
