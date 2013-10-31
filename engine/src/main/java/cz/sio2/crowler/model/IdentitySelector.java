package cz.sio2.crowler.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "identitySelector")
public class IdentitySelector extends Selector {

    private static IdentitySelector selector = new IdentitySelector();

    IdentitySelector() {
    }

    public static IdentitySelector getInstance() {
        return selector;
    }

    @Override
    public Elements resolve(Element e) {
        return new Elements(e);
    }
}
