package de.paluch.configserver.service;

import com.trilead.ssh2.crypto.cipher.AES;
import de.paluch.configserver.model.config.ConfigEncryption;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 04.04.13 08:51
 */
public class EncryptionTest {

    @Test
    public void testIsEncrypted() throws Exception {
        String input = "{des1:fsdfa34342e==}";
        boolean result = Encryption.isEncrypted(input);
        assertTrue(result);
    }

    @Test
    public void testIsNotEncrypted() throws Exception {
        String input = "des1:fsdfa34342e==}";
        boolean result = Encryption.isEncrypted(input);
        assertFalse(result);
    }

    @Test
    public void testIsNotEncrypted2() throws Exception {
        String input = "blubb";
        boolean result = Encryption.isEncrypted(input);
        assertFalse(result);
    }


    @Test
    public void testEncryptDecryptAES() throws Exception {
        String input = "blubb";

        ConfigEncryption encryption = new ConfigEncryption();
        encryption.setCipher("AES/CBC/PKCS5Padding");
        encryption.setKey("BTwdBhcFJd5Ls7DW82oTuQ==");
        encryption.setIvSpec("ZB0zQr9c9LbtMHS8fgkfKA==");
        encryption.setId("aes1");


        String result = Encryption.encrypt("aes1", input, Arrays.asList(encryption));
        assertTrue(Encryption.isEncrypted(result));

        String plain = Encryption.decrypt(result, Arrays.asList(encryption));
        assertEquals(input, plain);
    }

    @Test
    public void testEncryptDecryptBlowfish() throws Exception {
        String input = "blubb";

        ConfigEncryption encryption = new ConfigEncryption();
        encryption.setCipher("Blowfish");
        encryption.setKey("ZB0zQr9c9LbtMHS8fgkfKA==");
        encryption.setId("bwf");


        String result = Encryption.encrypt("bwf", input, Arrays.asList(encryption));
        assertTrue(Encryption.isEncrypted(result));

        String plain = Encryption.decrypt(result, Arrays.asList(encryption));
        assertEquals(input, plain);
    }
}
