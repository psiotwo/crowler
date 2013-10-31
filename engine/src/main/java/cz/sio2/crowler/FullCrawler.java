package cz.sio2.crowler;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.DC;
import cz.sio2.crowler.model.Configuration;
import cz.sio2.crowler.model.InitialDefinition;
import cz.sio2.crowler.model.NextPageResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class FullCrawler {

    private Logger logger = LoggerFactory.getLogger(FullCrawler.class.getName());

    private JenaConnector connector;

    public FullCrawler(JenaConnector connector) {
        this.connector = connector;
    }

    public void run(Configuration configuration) {
        try {
            this.connector.connect();
            final NextPageResolver resolver = configuration.getNextPageResolver();

            int i = 0;
            while (resolver.hasNext()) {
                final String currentId = configuration.getId() + "-" + String.format("%04d", i);
                final String ontoId = configuration.getBaseOntoPrefix() + currentId;

                final String input = resolver.next();
                logger.info("Page " + i+" : "+input);

                final OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
                final Ontology ont = model.createOntology(ontoId);
                for (final String imp : configuration.getSchemas()) {
                    ont.addImport(ModelFactory.createOntologyModel().createOntology(imp));
                }

                Individual org = model.createIndividual(configuration.getPublisher(), model.createClass(Vocabulary.w3cOrganization));
                model.add(ont, DC.source, input);
                model.add(ont, DC.publisher, org);
                model.add(ont, DC.date, model.createLiteral(new Date().toString()));

                logger.debug("   parsing definitions ...");

                for (InitialDefinition entry : configuration.getInitialDefinitions()) {
                    new Crowler(model, configuration, input, input, entry).crawl();
                }

                logger.debug("   opening persist model...");
                final Model persistModel = connector.getModel(ontoId);
                logger.debug("   saving ...");
                persistModel.add(model);
                logger.debug("   closing...");
                connector.closeModel(persistModel);
                logger.debug("   done.");

                i++;
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("Disconnecting backend.");
            connector.disconnect();
        }
    }
}
