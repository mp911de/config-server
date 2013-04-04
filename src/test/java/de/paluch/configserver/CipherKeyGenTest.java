package de.paluch.configserver;


import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 04.04.13 08:21
 */
@RunWith(Parameterized.class)
public class CipherKeyGenTest {

    private String algorithm;
    private Provider provider;

    @Parameterized.Parameters(name = "{index}: Algorithm({0})")
    public static Iterable<Object[]> data() {

        List<String> algorithms = new ArrayList<String>();
        List<Provider> providers = new ArrayList<Provider>();

        for (Provider provider : Security.getProviders()) {
            for (Provider.Service service : provider.getServices()) {
                if (service.getType().equals("KeyGenerator") && !service.getAlgorithm().contains("Tls")) {
                    algorithms.add(service.getAlgorithm());
                    providers.add(provider);
                }

            }
        }

        List<Object[]> result = new ArrayList<Object[]>();


        for (int i = 0; i < algorithms.size(); i++) {
            result.add(new Object[] { algorithms.get(i), providers.get(i) });
        }
        return result;
    }

    public CipherKeyGenTest(String algorithm, Provider provider) {
        this.algorithm = algorithm;
        this.provider = provider;
    }

    @Test
    public void createKey() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm, provider);
            keyGen.init(new SecureRandom());
            SecretKey key = keyGen.generateKey();
            System.out.println(algorithm + ": " + new String(Base64.encodeBase64(key.getEncoded())));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
