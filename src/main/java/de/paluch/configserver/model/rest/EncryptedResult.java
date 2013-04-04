package de.paluch.configserver.model.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 08.01.13 21:01
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EncryptedResult {

    private String result;

    public EncryptedResult() {
    }

    public EncryptedResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
