package cz.sio2.crowler.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "ChainedFirstElementSelector")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ChainedFirstElementSelector extends Selector {

//    @XmlElements({
//            @XmlElement(type=ChainedFirstElementSelector.class),
//            @XmlElement(type=NewDocumentSelector.class),
//            @XmlElement(type=JSoupSelector.class),
//            @XmlElement(type=IdentitySelector.class)
//    })

    @XmlElementWrapper(name = "selectorsInChain")
    @XmlElement(name = "selector")
    private List<Selector> selectors;

    public ChainedFirstElementSelector() {
    }

    public ChainedFirstElementSelector(Selector... selectors) {
        this.selectors = Arrays.asList(selectors);
    }

    public Elements resolve(Element e) {
        Element ex = e;
        Elements ec = null;
        for (Selector s : selectors) {
            ec = s.resolve(ex);
            if (ec.isEmpty()) {
                break;
            }
            ex = ec.first();
        }
        return ec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChainedFirstElementSelector that = (ChainedFirstElementSelector) o;

        if (selectors != null ? !selectors.equals(that.selectors) : that.selectors != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return selectors != null ? selectors.hashCode() : 0;
    }
}
