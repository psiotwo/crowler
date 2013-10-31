package cz.sio2.crowler.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "configuration")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Configuration {

    @XmlElement(name = "lang")
    private String lang;

    @XmlElement(name = "publisher")
    private String publisher;

    @XmlElement(name = "baseOntoPrefix")
    private String baseOntoPrefix;

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "encoding")
    private String encoding;

    @XmlElementWrapper(name = "initialSelectors")
    @XmlElement(name = "initialSelector")
    private List<InitialDefinition> initialSelectors = new ArrayList<InitialDefinition>();

    @XmlElement(name = "nextPageResolver")
    private NextPageResolver nextPageResolver;

    @XmlElement(name = "schemas")
    private String[] schemas;

    public String[] getSchemas() {
        return schemas;
    }

    public void setSchemas(String[] schemas) {
        this.schemas = schemas;
    }

    public NextPageResolver getNextPageResolver() {
        return nextPageResolver;
    }

    public void setNextPageResolver(NextPageResolver nextPageResolver) {
        this.nextPageResolver = nextPageResolver;
    }

    public List<InitialDefinition> getInitialDefinitions() {
        return initialSelectors;
    }

    public void addInitialDefinition(InitialDefinition def) {
        initialSelectors.add(def);
    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBaseOntoPrefix() {
        return baseOntoPrefix;
    }

    public void setBaseOntoPrefix(String baseOntoPrefix) {
        this.baseOntoPrefix = baseOntoPrefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (baseOntoPrefix != null ? !baseOntoPrefix.equals(that.baseOntoPrefix) : that.baseOntoPrefix != null)
            return false;
        if (encoding != null ? !encoding.equals(that.encoding) : that.encoding != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (initialSelectors != null ? !initialSelectors.equals(that.initialSelectors) : that.initialSelectors != null)
            return false;
        if (lang != null ? !lang.equals(that.lang) : that.lang != null) return false;
        if (nextPageResolver != null ? !nextPageResolver.equals(that.nextPageResolver) : that.nextPageResolver != null)
            return false;
        if (publisher != null ? !publisher.equals(that.publisher) : that.publisher != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lang != null ? lang.hashCode() : 0;
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (baseOntoPrefix != null ? baseOntoPrefix.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        result = 31 * result + (initialSelectors != null ? initialSelectors.hashCode() : 0);
        result = 31 * result + (nextPageResolver != null ? nextPageResolver.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigurationFactory{" +
                "lang='" + lang + '\'' +
                ", publisher='" + publisher + '\'' +
                ", baseOntoPrefix='" + baseOntoPrefix + '\'' +
                ", id='" + id + '\'' +
                ", encoding='" + encoding + '\'' +
                ", initialSelectors=" + initialSelectors +
                ", nextPageResolver=" + nextPageResolver +
                '}';
    }
}
