package cz.sio2.crowler.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PropertySpec<T> {

    @XmlElement(name = "propertyType")
    private PropertyType type;

    @XmlElement(name = "iri")
    private String iri;

    @XmlElement(name = "selector")
    private Selector selector;

    @XmlElement(name = "objectSpec")
    private T objectSpec;

    public PropertySpec() {
    }

    public PropertySpec(Selector selector, PropertyType type, String iri, T objectSpec) {
        this.selector = selector;
        this.type = type;
        this.iri = iri;
        this.objectSpec = objectSpec;
    }

    public PropertyType getType() {
        return type;
    }

    public String getIri() {
        return iri;
    }

    public Selector getSelector() {
        return selector;
    }

    public T getObjectSpec() {
        return objectSpec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertySpec that = (PropertySpec) o;

//        System.out.println(hashCode() + " : " + that.hashCode());
//        System.out.println(type + " : " + that.type);

        if (type != that.type) return false;
        if (iri != null ? !iri.equals(that.iri) : that.iri != null) return false;
        if (objectSpec != null ? !objectSpec.equals(that.objectSpec) : that.objectSpec != null) return false;
        if (selector != null ? !selector.equals(that.selector) : that.selector != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;

        result = type != null ? type.hashCode() : 0;
//        System.out.println(type + " : " + result);
        result = 31 * result + (iri != null ? iri.hashCode() : 0);
        result = 31 * result + (objectSpec != null ? objectSpec.hashCode() : 0);
        result = 31 * result + (selector != null ? selector.hashCode() : 0);

//        System.out.println("Hashcode: " + super.hashCode());
//        System.out.println("   iri: " + iri);
//        System.out.println("   type: " + type);
//        System.out.println("   objectSpec: " + objectSpec);
//        System.out.println("   selector: " + selector);


//        System.out.println(result +  " : " + super.hashCode());


        return result;
//        return super.hashCode();
    }
}
