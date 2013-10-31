package cz.sio2.crowler.model;

import cz.sio2.crowler.Crowler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.annotation.*;
import java.util.WeakHashMap;

@XmlRootElement(name = "newDocumentSelector")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class NewDocumentSelector extends Selector {

    @XmlTransient
    private WeakHashMap<String,Document> cache = new WeakHashMap<String, Document>();

    @XmlElement(name = "encoding")
    private String encoding;

    @XmlElement(name = "urlCreator")
    private URLCreator generator;

    public NewDocumentSelector() {
    }

    public NewDocumentSelector(String encoding, final URLCreator generator) {
        this.encoding = encoding;
        this.generator = generator;
    }

    public NewDocumentSelector(String encoding, final String url) {
        this.encoding = encoding;
        this.generator = new URLCreator() {
            @Override
            public String generate(Element e) {
                return url;
            }
        };
    }

    public Elements resolve(final Element e) {
        String newURL = generator.generate(e);
        Document d = cache.get(newURL);
        if (d==null) {
            d = Crowler.parse(encoding, newURL, newURL);
            cache.put(newURL,d);
        }
        return new Elements(d);
    }

    public void clear() {
        cache.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewDocumentSelector that = (NewDocumentSelector) o;

        if (encoding != null ? !encoding.equals(that.encoding) : that.encoding != null) return false;
        if (generator != null ? !generator.equals(that.generator) : that.generator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = encoding != null ? encoding.hashCode() : 0;
        result = 31 * result + (generator != null ? generator.hashCode() : 0);
        return result;
    }
}
