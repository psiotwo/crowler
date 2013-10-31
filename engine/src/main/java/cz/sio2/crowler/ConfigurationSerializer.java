package cz.sio2.crowler;

import cz.sio2.crowler.model.*;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class ConfigurationSerializer {

    private javax.xml.bind.JAXBContext jaxbContext;

    private String schemaFile="schema1.xsd";
{
    try {
        jaxbContext = javax.xml.bind.JAXBContext.newInstance(new Class[]{Configuration.class, InitialDefinition.class, ClassSpec.class, PropertySpec.class, JSoupSelector.class, IdentitySelector.class, TableRecordsNextPageResolver.class, EnumeratedNextPageResolver.class, NewDocumentSelector.class, ChainedFirstElementSelector.class, AttributePatternMatchingURLCreator.class});
    } catch (JAXBException e) {
        e.printStackTrace();
    }
}
    public void writeConf(final Configuration objectToMarshal, String fileName) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaFile);
            marshaller.marshal(objectToMarshal, new java.io.FileOutputStream(fileName));
        } catch (javax.xml.bind.JAXBException je) {
            je.printStackTrace();
        } catch (java.io.FileNotFoundException io) {
            io.printStackTrace();
        }
    }

    public Configuration readConf(final String fileName) {
        try {
            javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
             return (Configuration) unmarshaller.unmarshal(new java.io.FileInputStream(fileName));
        } catch (javax.xml.bind.JAXBException je) {
            je.printStackTrace();
        } catch (java.io.FileNotFoundException io) {
            io.printStackTrace();
        }
        return null;
    }
}
