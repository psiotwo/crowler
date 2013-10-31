package cz.sio2.crowler;

import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FullCrawlerTest {

    private JenaConnector c;

    @Before
    public void connect() {
        try {
            c = new FileJenaConnector(Files.createTempDirectory("crowler-test").toFile(),false);
            c.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrawl() {
        Configuration conf = new Configuration();
        conf.setId(Utils.getFullId("test-sio2-publications"));
        conf.setBaseOntoPrefix("http://sio2.cz/onto/test/");
        conf.setEncoding("utf-8");
        conf.setLang("en");
        conf.setPublisher("http://sio2.cz/psiotwo");
        conf.setNextPageResolver(new EnumeratedNextPageResolver(getClass().getClassLoader().getResource("html/sio2-publications.html").toString()));
        ClassSpec cs = Factory.createClassSpec(Vocabulary.biboArticle);
        InitialDefinition id = new InitialDefinition(cs,new JSoupSelector("div.journal-content-article ul li"));
        conf.addInitialDefinition(id);
        conf.setSchemas(new String[]{});
        PropertySpec ps = Factory.createAPSpec(new JSoupSelector("a"),Vocabulary.RDFS_LABEL);
        cs.addSpec(true,ps);

        new FullCrawler(c).run(conf);
    }

    @After
    public void disconnect() {
        c.disconnect();
    }
}
