package de.paluch.configserver.model.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:25
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigServerRepository {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "type")
    private RepositoryType type;

    @XmlElement(name = "localWorkDirectory")
    private String localWorkDirectory;

    @XmlElement(name = "remoteUrl")
    private String remoteUrl;

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "password")
    private String password;

    @XmlElement(name = "encryption")
    @XmlElementWrapper(name = "security")
    private List<ConfigEncryption> encryptions;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RepositoryType getType() {
        return type;
    }

    public void setType(RepositoryType type) {
        this.type = type;
    }

    public String getLocalWorkDirectory() {
        return localWorkDirectory;
    }

    public void setLocalWorkDirectory(String localWorkDirectory) {
        this.localWorkDirectory = localWorkDirectory;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ConfigEncryption> getEncryptions() {
        return encryptions;
    }

    public void setEncryptions(List<ConfigEncryption> encryptions) {
        this.encryptions = encryptions;
    }
}
