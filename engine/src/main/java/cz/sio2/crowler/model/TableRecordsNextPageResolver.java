package cz.sio2.crowler.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "TableRecordsNextPageResolver")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TableRecordsNextPageResolver extends NextPageResolver {

    @XmlTransient
    private int currentIndex;

    @XmlElement(name = "maxPage")
    private int maxPage;

    @XmlElement(name = "minPage")
    private int minPage;

    @XmlElement(name = "recordsPerPage")
    private int recordsPerPage;

    @XmlElement(name = "iri")
    private String iri;

    public TableRecordsNextPageResolver() {
    }

    public TableRecordsNextPageResolver(String iri, int from, int to, int recordsPerPage) {
        this.minPage= from;
        this.maxPage = to;
        this.recordsPerPage = recordsPerPage;
        this.currentIndex = minPage;

        this.iri = iri;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < maxPage;
    }

    @Override
    public String next() {
        return iri + (currentIndex++) * recordsPerPage;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableRecordsNextPageResolver that = (TableRecordsNextPageResolver) o;

        if (currentIndex != that.currentIndex) return false;
        if (minPage != that.minPage) return false;
        if (maxPage != that.maxPage) return false;
        if (recordsPerPage != that.recordsPerPage) return false;
        if (iri != null ? !iri.equals(that.iri) : that.iri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currentIndex;
        result = 31 * result + minPage;
        result = 31 * result + maxPage;
        result = 31 * result + recordsPerPage;
        result = 31 * result + (iri != null ? iri.hashCode() : 0);
        return result;
    }
}
