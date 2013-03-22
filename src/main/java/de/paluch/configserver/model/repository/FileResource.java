package de.paluch.configserver.model.repository;

import java.io.File;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 11.12.12 15:40
 */
public class FileResource {

    private File file;

    private String artifactId;
    private String version;
    private String environment;
    private String relativeName;

    public FileResource() {
    }

    public FileResource(String artifactId, String version, String environment, String relativeName) {
        this.artifactId = artifactId;
        this.version = version;
        this.environment = environment;
        this.relativeName = relativeName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getRelativeName() {
        return relativeName;
    }

    public void setRelativeName(String relativeName) {
        this.relativeName = relativeName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(getClass().getSimpleName());
        sb.append(" [file=").append(file);
        sb.append(", artifactId='").append(artifactId).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", environment='").append(environment).append('\'');
        sb.append(", relativeName='").append(relativeName).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
