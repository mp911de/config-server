package de.paluch.configserver.spring.jgroups;

import org.apache.log4j.Logger;
import org.jgroups.JChannel;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.jmx.JmxConfigurator;
import org.springframework.beans.factory.annotation.Required;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 19.09.12 15:32
 */
public class RpcDispatcherFactory extends ChannelBeanFactory<RpcDispatcher> {

    private final Logger log = Logger.getLogger(getClass());
    private Object serverObject;
    private JChannel channel;

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return RpcDispatcher.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected RpcDispatcher createInstance() throws Exception { // NOPMD

        if (serverObject == null) {
            throw new IllegalStateException("serverObject is null");
        }

        channel = new JChannel(getJgroupsconfig());

        RpcDispatcher rpcDispatcher = new RpcDispatcher(channel, null, null, serverObject);

        channel.connect(getClusterName());

        List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
        if (servers == null || servers.size() == 0) {
            log.warn("Cannot find MBean Server, no JMX Registration for " + channel.getName());
        } else {
            MBeanServer server = servers.get(0);
            JmxConfigurator.registerChannel(channel, server, getJMXDomain(), getClusterName(), true);
        }

        return rpcDispatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroyInstance(RpcDispatcher instance) throws Exception { // NOPMD

        if (instance.getChannel().isConnected()) {

            List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
            if (servers == null || servers.size() == 0) {
                log.warn("Cannot find MBean Server, no JMX De-Registration for " + channel.getName());
            } else {
                MBeanServer server = servers.get(0);
                JmxConfigurator.unregisterChannel(channel, server, getJMXDomain(), getClusterName());
            }

            instance.getChannel().disconnect();
            instance.getChannel().close();
        }
    }

    private String getJMXDomain() {
        return "config-server.jgroups";
    }

    public Object getServerObject() {
        return serverObject;
    }

    @Required
    public void setServerObject(Object serverObject) {
        this.serverObject = serverObject;
    }
}
