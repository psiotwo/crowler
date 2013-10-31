package cz.sio2.crowler;

import cz.sio2.crowler.connectors.FileJenaConnector;
import cz.sio2.crowler.connectors.SesameJenaConnector;
import cz.sio2.crowler.model.ConfigurationFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class Runner {

    public static void main(String[] args) {

        if (!Arrays.asList(new String[]{"file", "sesame"}).contains(args[1])) {
            System.out.println("Usage: Runner <CONFIGURATION_CLASS> (file filename) | (sesame serverurl repositoryid)>");
            System.exit(0);
        }

        JenaConnector c = null;

        if ("file".equals(args[1])) {
            c = new FileJenaConnector(new File(args[1]), false);
        } else if ("sesame".equals(args[1])) {
            XTrustProvider.install();
            final SesameJenaConnector cx = new SesameJenaConnector();
            cx.setServerUrl(args[2]);
            cx.setRepositoryId(args[3]);

//            cx.setServerUrl("http://onto.mondis.cz/openrdf-sesame");//args[1]);
//            cx.setRepositoryId("mondis-webdata");//args[2]);//id+"-"+Calendar.getInstance());
//            cx.setServerUrl("https://dev.sio2.cz/openrdf-sesame");//args[1]);
//            cx.setRepositoryId("monumnet-webdata");//args[2]);//id+"-"+Calendar.getInstance());
            c = cx;
        }
        try {
            final ConfigurationFactory f = (ConfigurationFactory) Class.forName(args[0]).newInstance();
            new FullCrawler(c).run(f.getConfiguration(new HashMap(System.getProperties())));
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
