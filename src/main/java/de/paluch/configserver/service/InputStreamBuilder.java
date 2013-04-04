package de.paluch.configserver.service;

import de.paluch.configserver.model.config.ConfigEncryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 10.01.13 10:53
 */
public class InputStreamBuilder {

    public static final String TYPE_PROPERTIES = ".properties";
    public static final String TYPE_GENERIC = ".";

    private File artifact;
    private File environment;
    private File version;
    private File file;

    private String type = TYPE_GENERIC;

    private List<ConfigEncryption> encryptions;

    public static InputStreamBuilder newInstance() {

        return new InputStreamBuilder();
    }

    public InputStreamBuilder artifact(File file) {

        artifact = file;
        return this;
    }

    public InputStreamBuilder environment(File file) {

        environment = file;
        return this;
    }

    public InputStreamBuilder version(File file) {

        version = file;
        return this;
    }

    public InputStreamBuilder file(File file) {
        this.file = file;
        return this;
    }

    public InputStreamBuilder guessType() {

        if (file == null) {
            throw new IllegalStateException("file not set");
        }

        type = TYPE_GENERIC;

        if (file.getName().toLowerCase().endsWith(TYPE_PROPERTIES)) {
            type = TYPE_PROPERTIES;
        }

        return this;
    }

    public InputStreamBuilder withEncryptions(List<ConfigEncryption> encryptions) {
        this.encryptions = encryptions;
        return this;
    }

    public InputStream build() throws IOException {

        if (version == null) {
            throw new IllegalStateException("version not set");
        }

        if (artifact == null) {
            throw new IllegalStateException("artifact not set");
        }

        if (environment == null) {
            throw new IllegalStateException("environment not set");
        }

        if (file == null) {
            throw new IllegalStateException("file not set");
        }

        if (type.equals(TYPE_PROPERTIES)) {
            return new OverridingPropertiesInputStreamBuilder(file.getName(), encryptions, artifact, version, environment).getInputStream();
        }
        return new FileInputStream(file);

    }


}
