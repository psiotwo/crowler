package cz.sio2.crowler.model;

import org.jsoup.nodes.Element;

public abstract class URLCreator {
    public abstract String generate(final Element e);
}
