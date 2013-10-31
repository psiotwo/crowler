package cz.sio2.crowler.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="classSpec")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ClassSpec {

    @XmlElement(name="iri")
    private String iri;

    @XmlElement(name = "property")
    private List<PropertySpec> specs;

    @XmlElementRef
    private List<PropertySpec> idSpecs;

    public ClassSpec() {
    }

    public ClassSpec(String iri) {
        this.iri = iri;
        this.specs = new ArrayList<PropertySpec>();
        this.idSpecs = new ArrayList<PropertySpec>();
    }

    public void addSpec(PropertySpec spec) {
        addSpec(false, spec);
    }

    public void addSpec(boolean isPartOfId, PropertySpec spec) {
        if (isPartOfId) {
            idSpecs.add(spec);
        }

        specs.add(spec);
    }

    public List<PropertySpec> getSpecs() {
        return specs;
    }

    public boolean isId(PropertySpec spec) {
        return idSpecs.contains(spec);
    }

    public String getIRI() {
        return iri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassSpec classSpec = (ClassSpec) o;

        if (iri != null ? !iri.equals(classSpec.iri) : classSpec.iri != null) return false;

        if (specs != null ? !specs.equals(classSpec.specs) : classSpec.specs != null) return false;
        if (idSpecs != null ? !idSpecs.equals(classSpec.idSpecs) : classSpec.idSpecs != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = iri != null ? iri.hashCode() : 0;

        result = 31 * result + (specs != null ? specs.hashCode() : 0);
        result = 31 * result + (idSpecs != null ? idSpecs.hashCode() : 0);
        return result;
    }
}
