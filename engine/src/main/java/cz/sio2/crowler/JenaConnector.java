package cz.sio2.crowler;

import com.hp.hpl.jena.rdf.model.Model;

public interface JenaConnector {

    public void connect();

    public Model getModel(String contextName);

    public void closeModel(Model model);

    public void disconnect();
}
