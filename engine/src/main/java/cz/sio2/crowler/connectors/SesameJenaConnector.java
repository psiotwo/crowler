package cz.sio2.crowler.connectors;

import com.hp.hpl.jena.rdf.model.Model;
import cz.sio2.crowler.JenaConnector;
import org.openjena.jenasesame.JenaSesame;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SesameJenaConnector implements JenaConnector {

    private final static Logger logger = LoggerFactory.getLogger(SesameJenaConnector.class.getName());
    
    private RepositoryConnection repoConnection = null;
    
    private String serverUrl;
    private String repositoryId;


    @Override
    public Model getModel(String ctxURI) {
        logger.debug("Getting model '" + ctxURI + "'");
        final ValueFactory f = repoConnection.getValueFactory();
        final org.openrdf.model.URI contextURI = f.createURI(ctxURI);
        final Model model = JenaSesame.createModel(repoConnection, contextURI);
        logger.debug("model "+model.hashCode());
        return model;
    }

    @Override
    public void closeModel(Model model) {
        if (model == null) {
            return;
        }
        logger.debug("Closing model " + model.hashCode());
        model.close();
    }

    @Override
    public void connect() {
        try {
            RepositoryManager repoManager = new RemoteRepositoryManager(serverUrl);
            repoManager.initialize();

            Repository repo = repoManager.getRepository(repositoryId);

            repoConnection = repo.getConnection();
            repoConnection.setAutoCommit(true);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to connect", ex);
        }

        logger.debug("Connected.");
    }

    @Override
    public void disconnect() {
        if (repoConnection != null) {
            try {
                repoConnection.close();
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
        logger.debug("Disconnected.");
    }

    
    
    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

}
