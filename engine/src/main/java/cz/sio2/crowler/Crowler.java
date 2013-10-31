package cz.sio2.crowler;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import cz.sio2.crowler.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Crowler {

    private static final Logger logger = LoggerFactory.getLogger(Crowler.class.getName());

    private Map<String,OntClass> classCache = new WeakHashMap<String, OntClass>();
    private Map<String,OntProperty> propertyCache = new WeakHashMap<String, OntProperty>();

    private OntModel model;
    private Configuration conf;
    private String baseURL;
    private String physicalURL;
    private InitialDefinition initialDefinitionIF;

    public Crowler(OntModel model, Configuration conf, String baseURL, String physicalURL, InitialDefinition initialDefinitionIF) {
        this.model = model;
        this.conf = conf;
        this.initialDefinitionIF = initialDefinitionIF;
        this.baseURL = baseURL;
        this.physicalURL = physicalURL;
    }

    public static Document parse(String encoding, String baseURL, String physicalURL) {
        try {
            Document d = Jsoup.parse(new URL(physicalURL).openStream(), encoding, baseURL);
            d.outputSettings().charset("UTF-8".intern());
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OntModel crawl() {
        try {
            final Document doc = parse(conf.getEncoding(),baseURL,physicalURL);
            final Elements el = initialDefinitionIF.getSelector().resolve(doc);
            for (Iterator<Element> i = el.iterator(); i.hasNext(); ) {
                resolve(baseURL, i.next(), initialDefinitionIF.getClassSpec());
            }

            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Individual createEmpty(OntClass clazz, String baseURLX, String idBase) {
        Individual i = model.createIndividual(Utils.getUniqueIRI(model, conf.getBaseOntoPrefix(), idBase), clazz);

        return i;
    }

    private OntClass getOntClass(final String iri) {
        OntClass c = classCache.get(iri);
        if ( c == null ) {
            c = model.createClass(iri);
            classCache.put(iri,c);
        }
        return c;
    }

    private OntProperty getOntProperty(final String iri, final PropertyType type) {
        OntProperty c = propertyCache.get(iri);
        if ( c == null ) {
            switch (type) {
                case ANNOTATION: c = model.createAnnotationProperty(iri); break;
                case DATA: c = model.createDatatypeProperty(iri); break;
                case OBJECT: c = model.createObjectProperty(iri); break;
            }

            propertyCache.put(iri,c);
        }
        return c;
    }

    // 'e' represents a single individual
    private Individual resolve(String baseURL, Element eClass, ClassSpec specClassIF) {
        logger.trace("class spec: " + specClassIF.getIRI());
        final StringBuilder id = new StringBuilder().append(getOntClass(specClassIF.getIRI()).getLocalName());
        final Map<Property, Set<RDFNode>> values = new HashMap<Property, Set<RDFNode>>();
        for (final PropertySpec propertySpecIF : specClassIF.getSpecs()) {
            logger.trace("   prop spec: " + propertySpecIF.getIri());
            final Selector propSelector = propertySpecIF.getSelector();
            for (final Iterator<Element> itProperties = propSelector.resolve(eClass).iterator(); itProperties.hasNext(); ) {
                Element eValue = itProperties.next();
                logger.trace("   value spec: " + eValue);

                OntProperty p = getOntProperty(propertySpecIF.getIri(), propertySpecIF.getType());

                Set<RDFNode> set = values.get(p);
                if (set == null) {
                    set = new HashSet<RDFNode>();
                    values.put(p, set);
                }

                if (specClassIF.isId(propertySpecIF)) {
                    id.append("-".intern()).append(eValue.text().intern());
                }

                if ( PropertyType.OBJECT.equals(propertySpecIF.getType())) {
                    set.add(resolve(baseURL, eValue, (ClassSpec) propertySpecIF.getObjectSpec()));
                } else if (PropertyType.DATA.equals(propertySpecIF.getType())) {
                    set.add(create((String) propertySpecIF.getObjectSpec(),eValue.text()));
                } else if (PropertyType.ANNOTATION.equals(propertySpecIF.getType())) {
                    set.add(create((String) propertySpecIF.getObjectSpec(), eValue.text()));
                }
            }
        }
        final Individual ind = createEmpty(getOntClass(specClassIF.getIRI()), baseURL, id.toString());
        for (Map.Entry<Property, Set<RDFNode>> indx : values.entrySet()) {
            for (RDFNode n : indx.getValue()) {
                ind.addProperty(indx.getKey(), n);
            }
        }
        return ind;
    }

    private Literal create(String rdfDataTypeIRI, String text) {
        if ( rdfDataTypeIRI == null ) {
            return model.createLiteral(text, conf.getLang());
        } else {
            if ( rdfDataTypeIRI.startsWith("http://www.w3.org/2001/XMLSchema") ) {
                return model.createTypedLiteral(text, new XSDDatatype(rdfDataTypeIRI.substring("http://www.w3.org/2001/XMLSchema#".length())));
            } else {
                return model.createTypedLiteral(text, new BaseDatatype(rdfDataTypeIRI));
            }
        }
    }
}
