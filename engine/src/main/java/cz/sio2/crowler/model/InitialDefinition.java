package cz.sio2.crowler.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class InitialDefinition {

    @XmlElement(name = "classSpec")
    private ClassSpec classSpec;

    @XmlElement(name = "selector")
    private Selector selector;

    public InitialDefinition() {
    }

    public InitialDefinition(ClassSpec classSpec, Selector selector) {
        this.classSpec = classSpec;
        this.selector = selector;
    }

    public ClassSpec getClassSpec() {
        return classSpec;
    }

    public Selector getSelector() {
        return selector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InitialDefinition that = (InitialDefinition) o;

        if (classSpec != null ? !classSpec.equals(that.classSpec) : that.classSpec != null) return false;
        if (selector != null ? !selector.equals(that.selector) : that.selector != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0    ;
        result = classSpec != null ? classSpec.hashCode() : 0;
        result = 31 * result + (selector != null ? selector.hashCode() : 0);
        return result;
    }
}
