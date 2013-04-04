package de.paluch.configserver.model.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 04.04.13 07:58
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigEncryption {

    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlElement(name = "cipher", required = true)
    private String cipher;

    @XmlElement(name = "key", required = true)
    private String key;

    @XmlElement(name = "ivSpec")
    private String ivSpec;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIvSpec() {
        return ivSpec;
    }

    public void setIvSpec(String ivSpec) {
        this.ivSpec = ivSpec;
    }
}
