package de.paluch.configserver.service;

import java.io.*;
import java.util.Properties;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 10.01.13 10:59
 */
public class OverridingPropertiesInputStreamBuilder {

    private Properties properties = new Properties();

    public OverridingPropertiesInputStreamBuilder(String baseFileName, File... directories) throws IOException {

        readFiles(baseFileName, directories);

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
        properties.store(baos, null);

        return new ByteArrayInputStream(baos.toByteArray());
    }
}
