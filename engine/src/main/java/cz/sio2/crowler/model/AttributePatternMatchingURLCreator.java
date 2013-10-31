package cz.sio2.crowler.model;

import org.jsoup.nodes.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class AttributePatternMatchingURLCreator extends URLCreator {

    private String attributeName;
    private String pattern;
    private String goToURLTemplate;

    public AttributePatternMatchingURLCreator() {
    }

    public AttributePatternMatchingURLCreator(String attributeName, String pattern, String goToURLTemplate) {
        this.attributeName = attributeName;
        this.pattern = pattern;
        this.goToURLTemplate = goToURLTemplate;
    }

    @Override
    public String generate(final Element e) {
        Matcher n = Pattern.compile(pattern).matcher(e.attr(attributeName));
        n.find();
        return goToURLTemplate.replace("{0}",n.group(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttributePatternMatchingURLCreator that = (AttributePatternMatchingURLCreator) o;

        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        if (goToURLTemplate != null ? !goToURLTemplate.equals(that.goToURLTemplate) : that.goToURLTemplate != null)
            return false;
        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attributeName != null ? attributeName.hashCode() : 0;
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        result = 31 * result + (goToURLTemplate != null ? goToURLTemplate.hashCode() : 0);
        return result;
    }
}
