package cz.sio2.crowler.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "selector")
public abstract class Selector {
    public abstract Elements resolve(Element e);
}
