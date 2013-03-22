package de.paluch.configserver.spring.jgroups;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.net.URL;

/**
 * Abstract Factory to create JChannel-Objects.
 * 
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @param <T>
 *            Result-Type.
 */
public abstract class ChannelBeanFactory<T> extends AbstractFactoryBean<T> implements DisposableBean {

    private URL jgroupsconfig;
    private String clusterName;

    public URL getJgroupsconfig() {
        return jgroupsconfig;
    }

    @Required
    public void setJgroupsconfig(URL jgroupsconfig) {
        this.jgroupsconfig = jgroupsconfig;
    }

    public String getClusterName() {
        return clusterName;
    }

    @Required
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
