package cz.sio2.crowler;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public abstract class AbstractCrawler {
    protected String lang;

    protected String publisher;

    protected String baseURL;
    protected URL physicalURL;

    protected String baseOntoPrefix;

    protected Document doc;

    protected OntModel model;

    protected String encoding = "UTF-8";

    private Ontology parent;
    private String id;
    private String outputDir;

    public AbstractCrawler(String charset, Ontology parent, String id, String baseOntoPrefix, String lang, String baseURL, String physicalURL, String publisherIRI, String outputDir) {
        try {
            this.baseURL = baseURL;
            this.physicalURL = new URL(physicalURL);
            this.publisher = publisherIRI;
            this.id = id;
            this.parent = parent;
            this.lang = lang;
            this.baseOntoPrefix = baseOntoPrefix;
            this.encoding = charset;
            this.outputDir = outputDir;
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public boolean crawl() {
        try {

            doc = Jsoup.parse(this.physicalURL.openStream(), encoding, baseURL);
            doc.outputSettings().charset("UTF-8");

            model = ModelFactory.createOntologyModel();
            String ontoId = baseOntoPrefix + id;
            Ontology o = model.createOntology(ontoId);

            parent.addImport(o);

            compute();

            // TODO annotations

            model.write(new FileWriter(outputDir + id + ".rdf"), "RDF/XML-ABBREV");
            return true;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
    }

    protected abstract void compute();

    protected Individual create(OntClass clazz, String title) {
        return create(clazz, title, title);
    }

    protected Individual createEmpty(OntClass clazz, String idBase) {
        Individual i = model.createIndividual(Utils.getUniqueIRI(model, baseOntoPrefix, idBase), clazz);
        model.add(model.createStatement(i, DC.source, baseURL.toString()));
        model.add(model.createStatement(i, DC.publisher, model.createIndividual(publisher, model.createClass(Vocabulary.w3cOrganization))));
        model.add(model.createStatement(i, DC.publisher, model.createIndividual(publisher, model.createClass(Vocabulary.w3cOrganization))));
        return i;
    }

    protected Individual create(OntClass clazz, String idBase, String title) {
        Individual i = model.createIndividual(Utils.getUniqueIRI(model, baseOntoPrefix, idBase), clazz);
        model.add(model.createStatement(i, RDFS.label, model.createLiteral(title, lang)));
        model.add(model.createStatement(i, DC.source, baseURL.toString()));
        model.add(model.createStatement(i, DC.publisher, model.createIndividual(publisher, model.createClass(Vocabulary.w3cOrganization))));
        model.add(model.createStatement(i, DC.publisher, model.createIndividual(publisher, model.createClass(Vocabulary.w3cOrganization))));
        return i;
    }

    protected Individual create(OntProperty prop, Individual parent, OntClass clazz, String title) {
        Individual i = create(clazz, title, title);
        if (parent != null && prop != null) {
            model.add(model.createStatement(i, prop, parent));
        }
        return i;
    }

    protected Individual createWithDate(OntClass clazz, String year, String title) {
        Individual i = create(clazz, title + " " + year, title);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        i.addProperty(DC.date, year, XSDDatatype.XSDgYear);
        return i;
    }

    protected Individual createWithIdAndDate(OntClass clazz, String id, String year, String title) {
        Individual i = create(clazz, id, title);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        i.addProperty(DC.date, year, XSDDatatype.XSDgYear);
        return i;
    }
}
