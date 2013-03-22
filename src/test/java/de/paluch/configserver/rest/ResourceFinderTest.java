package de.paluch.configserver.rest;

import de.paluch.configserver.model.repository.FileResource;
import de.paluch.configserver.service.ResourceFinder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.12.12 12:50
 */
public class ResourceFinderTest {

    @Test
    public void testFindResource() throws Exception {


        FileResource f1 = new FileResource("", "1.0.0", "", "");
        FileResource f2 = new FileResource("", "1.1.0", "", "");
        FileResource f3 = new FileResource("", "1.2.0", "", "");
        FileResource f4 = new FileResource("", "1.2.1", "", "");

        List<FileResource> resources = Arrays.asList(f1, f2, f3, f4);

        ResourceFinder rf = new ResourceFinder();
        FileResource result = rf.findResource(resources, "", "1.1.1", "");

        assertSame(f2, result);


    }

    @Test
    public void testFindResource2() throws Exception {


        FileResource f1 = new FileResource("", "1.0.0", "", "");
        FileResource f2 = new FileResource("", "1.1.0", "", "");
        FileResource f3 = new FileResource("", "1.2.0", "", "");
        FileResource f4 = new FileResource("", "1.2.1", "", "");

        List<FileResource> resources = Arrays.asList(f1, f2, f3, f4);

        ResourceFinder rf = new ResourceFinder();
        FileResource result = rf.findResource(resources, "", "0.1.1", "");

        assertSame(f1, result);


    }

    @Test
    public void testFindResource3() throws Exception {


        FileResource f1 = new FileResource("", "1.0.0", "", "");
        FileResource f2 = new FileResource("", "1.1.0", "", "");
        FileResource f3 = new FileResource("", "1.2.0", "", "");
        FileResource f4 = new FileResource("", "1.2.1", "", "");

        List<FileResource> resources = Arrays.asList(f1, f2, f3, f4);

        ResourceFinder rf = new ResourceFinder();
        FileResource result = rf.findResource(resources, "", "1.3.0", "");

        assertSame(f4, result);


    }
}
