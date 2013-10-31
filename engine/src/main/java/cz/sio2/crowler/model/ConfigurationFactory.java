package cz.sio2.crowler.model;

import java.util.Map;

public interface ConfigurationFactory {

    public Configuration getConfiguration(final Map<String,String> properties );

}
