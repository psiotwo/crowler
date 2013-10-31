package cz.sio2.crowler.model;

import javax.xml.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "EnumeratedNextPageResolver")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EnumeratedNextPageResolver extends NextPageResolver {

    @XmlTransient
    private int i;

    @XmlElementWrapper(name = "iriList")
    @XmlElement(name = "iri")
    private List<String> iri;

    public EnumeratedNextPageResolver() {
    }

    public EnumeratedNextPageResolver(String... iri) {
        this.iri = Arrays.asList(iri);
    }

    @Override
    public boolean hasNext() {
        return i < iri.size();
    }

    @Override
    public String next() {
        return iri.get(i++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnumeratedNextPageResolver that = (EnumeratedNextPageResolver) o;

        if (i != that.i) return false;
        if (iri != null ? !iri.equals(that.iri) : that.iri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = i;
        result = 31 * result + (iri != null ? iri.hashCode() : 0);
        return result;
    }
}
