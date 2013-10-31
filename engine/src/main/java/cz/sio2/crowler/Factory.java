package cz.sio2.crowler;

import cz.sio2.crowler.model.*;

public class Factory {


    public static ClassSpec createClassSpec(String iri) {
        return new ClassSpec(iri);
    }

    public static InitialDefinition createInitialDefinition(ClassSpec classSpec, Selector selector) {
        return new InitialDefinition(classSpec, selector);
    }

    public static JSoupSelector createJSoupSelector(String selectionString) {
        return new JSoupSelector(selectionString);
    }

    public static JSoupSelector createJSoupSelector(int i, String selectionString) {
        return new JSoupSelector(i, selectionString);
    }

    public static PropertySpec<String> createAPSpec(Selector selector, String iri) {
        return new PropertySpec<String>(selector, PropertyType.ANNOTATION, iri, null);
    }

    public static PropertySpec<String> createDPSpec(String iri) {
        return createDPSpec(IdentitySelector.getInstance(), iri);
    }

    public static PropertySpec<String> createDPSpec(Selector selector, String iri) {
        return createDPSpec(selector, iri, null, true);
    }

    public static PropertySpec<String> createDPSpec(Selector selector, String iri, String dataTypeIRI, boolean allowEmptyValues) {
        return new PropertySpec<String>(selector, PropertyType.DATA, iri, dataTypeIRI);
    }

    public static PropertySpec<ClassSpec> createOPSpec(Selector selector, String iri, ClassSpec spec) {
        return new PropertySpec<ClassSpec>(selector, PropertyType.OBJECT, iri, spec);
    }

    public static ChainedFirstElementSelector createChainedFirstElementSelector(Selector... selectors) {
        return new ChainedFirstElementSelector(selectors);
    }

    public static AttributePatternMatchingURLCreator createAttributePatternMatchingURLCreator(String attributeName, String pattern, String goToURLTemplate) {
        return new AttributePatternMatchingURLCreator(attributeName, pattern, goToURLTemplate);
    }

    public static NewDocumentSelector createNewDocumentSelector(String encoding, final URLCreator generator) {
        return new NewDocumentSelector(encoding, generator);
    }

    public static EnumeratedNextPageResolver createEnumeratedNextPageResolver(String... iri) {
        return new EnumeratedNextPageResolver(iri);
    }

    public static NextPageResolver createTableNextPageResolver( final String baseIRI, final int pageCount, final int pageLength ) {
        return new TableRecordsNextPageResolver(baseIRI, 0, pageCount, pageLength);
    }

    public static NextPageResolver createIteratedNextPageResolver(final String baseIRI, final int firstPage, final int lastPage) {
        return new NextPageResolver() {
            int current=firstPage;

            @Override
            public boolean hasNext() {
                return current < lastPage;
            }

            @Override
            public String next() {
                return baseIRI+current++;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static NextPageResolver createSequencedNextPageResolver(final NextPageResolver... resolvers) {
        return new NextPageResolver() {
            int i = 0 ;

            @Override
            public boolean hasNext() {
                if ( i >= resolvers.length ) {
                    return false;
                } else if ( resolvers[i].hasNext() ) {
                    return true;
                } else {
                    i++;
                    return hasNext();
                }
            }

            @Override
            public String next() {
                hasNext();
                return resolvers[i].next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
