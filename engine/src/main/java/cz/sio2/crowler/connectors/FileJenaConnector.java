package cz.sio2.crowler.connectors;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.sio2.crowler.JenaConnector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileJenaConnector implements JenaConnector {

    public static final String DEFAULT_CONTEXT_FILE_NAME = "___DEFAULT___";
    private File directory;

    private OntModel allModel;
    private Ontology allModelOntology;

    private Map<String, OntModel> models;

    private boolean shallWriteCatalog;

    private Document doc;
    private Element catalog;

    public FileJenaConnector(final File directory, final boolean shallWriteCatalog) {
        this.directory = directory;
        this.shallWriteCatalog = shallWriteCatalog;
        this.models = new HashMap<String, OntModel>();
    }

    @Override
    public void connect() {
        if (directory.exists()) {
            directory.delete();
        }
        directory.mkdirs();

        allModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        allModelOntology = allModel.createOntology(directory.toURI().toString());

        if (shallWriteCatalog) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                catalog = doc.createElement("catalog".intern());
                doc.appendChild(catalog);
                catalog.setAttribute("prefer".intern(), "public".intern());
                catalog.setAttribute("xmlns".intern(), "urn:oasis:names:tc:entity:xmlns:xml:catalog".intern());
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public Model getDefaultModel() {
        return getInternalModel(null, DEFAULT_CONTEXT_FILE_NAME, shallWriteCatalog);
    }

    private String getFileNameForContext(String context) {
        return context.replaceAll("http:","http").replaceAll("/", "_-_").concat(".rdf");
    }

    private String getContextForFileName(String fileName) {
        String x = fileName.replaceAll("http","http:").replaceAll("_-_", "/");

        x = x.substring(0,x.length()-5);

        return x;
    }

    public java.util.List<String> getContextIris() {
        final List<String> list = new ArrayList<String>();
        for( final File file: directory.listFiles()) {
            list.add(getContextForFileName(file.getName()));
        }

        return list;
    }


    @Override
    public Model getModel(String contextName) {
        final String fileName = getFileNameForContext(contextName);
        System.out.println(fileName);
        return getInternalModel(contextName, fileName, shallWriteCatalog);
    }

    public Model getInternalModel(String context, String fileName, boolean writecatalog) {
        OntModel model = models.get(fileName);

        if (model == null) {
            model = ModelFactory.createOntologyModel();

            models.put(fileName, model);

            if (writecatalog) {
                org.w3c.dom.Element eURI = doc.createElement("uri".intern());
                eURI.setAttribute("id".intern(), "User Entered Import Resolution".intern());
                eURI.setAttribute("name".intern(), context);
                eURI.setAttribute("uri".intern(), fileName.intern());
                catalog.appendChild(eURI);

                allModelOntology.addImport(model.createOntology(context));
            }
        }

        return model;
    }

    private String getFileNameForModel(Model m) {
        if (m == allModel) {
            return getFileNameForContext("all");
        }

        for (final String x : models.keySet()) {
            if (models.get(x) == m) {
                return x;
            }
        }

        return null;
    }

    @Override
    public void closeModel(Model model) {
        FileOutputStream fos = null;
        try {
            final File file = new File(directory +"/"+ getFileNameForModel(model));

            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            model.write(fos);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            model.close();
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }
    }

    @Override
    public void disconnect() {
        if (shallWriteCatalog) {
            OutputStream fos = null;
            OutputStream modelFos = null;
            try {
                modelFos =  new FileOutputStream(directory + "/" + getFileNameForModel(allModel));
                allModel.write(modelFos);
                fos = new FileOutputStream(new File(directory  + "/" + "catalog-v001.xml"));
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(fos));
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (TransformerException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                allModel.close();
                try {
                    modelFos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        models.clear();
        doc = null;
        catalog = null;
    }
}
