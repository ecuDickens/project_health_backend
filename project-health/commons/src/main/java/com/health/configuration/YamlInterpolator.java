package com.health.configuration;

import com.health.Env;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YamlInterpolator {
    
    /** Logger */
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(YamlInterpolator.class);
    
    /** FreeMarker configuration  */
    private static final Configuration CONFIGURATION = createConfiguration();

    /**
     * Creates and returns a basic FreeMarker ConfigurationLoader
     * 
     * @return ConfigurationLoader
     */
    private static Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setLocale(Locale.US);
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        
        return configuration;
    }
    
    /** Name of the FreeMarker Template, typically the service YML file */
    private final String name;

    private final Env env;

    public YamlInterpolator(String name, Env env) {
        this.name = name;
        this.env = env;
    }

    /**
     * Creates a basic Data Model and loads external data from XML file
     *
     * @return Map<String, Object> the root node of the data model
     */
    private Map<String, Object> loadDataModel() {
        Map<String, Object> modelRoot = new HashMap<String, Object>();

        // load dynamic and/or system data
        modelRoot.put("username", env.getCurrentUserName().toLowerCase());
        modelRoot.put("environment", env.getEnvironment());
        modelRoot.put("domain", env.getDomain());
        modelRoot.put("version", env.getVersion());

        // load static data from file
        // not needed now

        LOGGER.debug("FreeMarker DataModel: {}", modelRoot);
        return modelRoot;
    }

    /**
     * Processes the raw input stream for any embedded markers 
     * and returns the result in a character-stream reader
     * 
     * @param reader
     *      The reader pointing to the configuration file.
     * @param instanceDataModel
     *      An optional data model to evaluate per processing
     *      instance invocation.
     * @return Reader
     *      A reader pointing to the evaluated and processed
     *      configuration file.
     * @throws IOException if error in read/write from/to streams
     */
    public Reader process(final Reader reader, final Map<String, Object> instanceDataModel) throws IOException {
        LOGGER.entry(this.name, reader);

        try {
            // blend the global data model with any instance data model entries
            Map<String, Object> rootMap = loadDataModel();
            if (instanceDataModel != null && !instanceDataModel.isEmpty()) {
                rootMap.putAll(instanceDataModel);
            }

            // evaluate the template using the blended root map data model
            StringWriter stringWriter = new StringWriter(4096);
            Template template = new Template(name, reader, CONFIGURATION);
            template.process(rootMap, stringWriter);

            return new StringReader(stringWriter.getBuffer().toString());
        }
        catch (TemplateException ex) {
            String errMsg = "Failed processing template " + name;
            LOGGER.error(errMsg, ex);
            throw new IOException(errMsg, ex);
        }
    }

}
