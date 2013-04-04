package de.paluch.configserver.service;

import de.paluch.configserver.model.config.ConfigEncryption;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 10.01.13 10:59
 */
public class OverridingPropertiesInputStreamBuilder {

    private Properties properties = new Properties();
    private List<ConfigEncryption> encryptions;

    public OverridingPropertiesInputStreamBuilder(String baseFileName, List<ConfigEncryption> encryptions,
                                                  File... directories) throws IOException {

        readFiles(baseFileName, directories);
        this.encryptions = encryptions;

    }

    private void readFiles(String baseFileName, File[] directories) throws IOException {
        for (File directory : directories) {
            File file = new File(directory, baseFileName);

            if (file.exists()) {
                readFile(file);
            }
        }
    }

    private void readFile(File file) throws IOException {
        InputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public InputStream getInputStream() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decryptValues(properties);

        properties.store(baos, null);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Decrypt Properties-File.
     * @param properties
     */
    private void decryptValues(Properties properties) {

        Set<String> names = properties.stringPropertyNames();
        for (String key : names) {
            String value = properties.getProperty(key);
            if (Encryption.isEncrypted(value)) {

                try {
                    String decrypted = Encryption.decrypt(value, encryptions);
                    properties.setProperty(key, decrypted);
                } catch (Exception e) {
                    properties.setProperty(key, "ERROR: " + e.getMessage());
                }
            }
        }
    }
}
