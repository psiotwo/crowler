package cz.sio2.crowler;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.vocabulary.RDFS;
import cz.sio2.crowler.model.PropertyType;
import org.jsoup.Jsoup;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Utils {

    public static String getFullId(String id) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        return format.format(Calendar.getInstance().getTime()) + "-"+id;
    }


    static String stripAccents(String s) {
        String sx = Normalizer.normalize(s, Normalizer.Form.NFD);
        sx = sx.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sx;
    }

    public static String[] getLines(String input) {
        String text = Jsoup.parse(input.replaceAll("(?i)<br[^>]*>", "br2n")).text();

        String[] s = text.split("br2n");

        return s;
    }

    public static String getUniqueIRI(OntModel model, String iri, String label) {
        try {
            String l;
            l = stripAccents(label.toLowerCase().trim().replaceAll("[\\s\\xA0]", "-").replaceAll("/", "_slash_"));
            l = java.net.URLEncoder.encode(l, "UTF-8");
            String newIri = iri + l;

//            int i = 0;
//            while (model.containsResource(ModelFactory.createOntologyModel().getResource(newIri))) {
//                newIri = Vocabulary.MONDIS_IRI + l;
//            }
//            java.net.URLEncoder.encode(label, "UTF-8")

            return newIri;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }

    }

    public static Integer getFirstInt(String s) {
        Scanner in = new Scanner(s).useDelimiter("[^0-9]+".intern());
        if (in.hasNext()) {
            return in.nextInt();
        } else {
            return null;
        }
    }


    public static OntClass c(final String id, final String language, final OntModel model, final String name) {
        OntClass c = model.createClass(id);
        c.addProperty(RDFS.label, model.createLiteral(name, language));
        return c;
    }

    public static OntProperty p(final String id, final String language, final OntModel model, final PropertyType type, final String name, final String origName) {
        OntProperty prop;
        String iri = id;
        switch (type) {
            case DATA:
                prop = model.createDatatypeProperty(iri);
                break;
            case OBJECT:
                prop = model.createObjectProperty(iri);
                break;
            case ANNOTATION:
                prop = model.createAnnotationProperty(iri);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        prop.addProperty(RDFS.label, model.createLiteral(name, language));
        prop.addProperty(RDFS.comment, model.createLiteral(origName, language));
        return prop;
    }
}
