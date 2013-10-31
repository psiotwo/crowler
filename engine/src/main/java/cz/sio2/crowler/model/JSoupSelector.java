package cz.sio2.crowler.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "jSoupSelector")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class JSoupSelector extends Selector {

    @XmlElement(name = "selectionString", required = true)
    private String selectionString;

    @XmlElement(name = "backtrackToLevel", defaultValue = "0", required = false)
    private int i = 0;

    public JSoupSelector() {
    }

    public JSoupSelector(String selectionString) {
        this(0, selectionString);
    }

    public JSoupSelector(int i, String selectionString) {
        this.selectionString = selectionString;
        this.i = i;
    }

    public Elements resolve(Element e) {
        int j = i;
        while (j-- > 0) {
            e = e.parent();
        }

        return e.select(selectionString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JSoupSelector that = (JSoupSelector) o;

        if (i != that.i) return false;
        if (selectionString != null ? !selectionString.equals(that.selectionString) : that.selectionString != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = selectionString != null ? selectionString.hashCode() : 0;
        result = 31 * result + i;
        return result;
    }
}
