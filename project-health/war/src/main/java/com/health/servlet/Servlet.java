package com.health.servlet;

import net.sf.ehcache.CacheManager;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Top level Servlet
 */
public final class Servlet extends HttpServlet {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(Servlet.class);

    /** Serialization UID */
    private static final long serialVersionUID = -3563060017478604261L;

    public Servlet() {
    }

    /**
     * Responsible for initializing/starting all components
     * 
     * @throws ServletException
     *             Error during initialization of a service, this
     *             will force Health to not start.
     */
    @Override
    public void init() throws ServletException {
        LOGGER.entry();

        super.init();

        initSystemProperties();

        initCache();

        LOGGER.exit();
    }

    /**
     * Used to shutdown components
     */
    @Override
    public void destroy() {
        LOGGER.entry();

        super.destroy();

        // Shut down the cache manager
        CacheManager.getInstance().shutdown();

        LOGGER.exit();
    }

    /**
     * Initializes the necessary system properties.
     */
    private void initSystemProperties() {
        LOGGER.entry();

        System.setProperty("com.health.root", this.getServletContext().getRealPath("/"));
        System.setProperty("com.health.standalone", "false");
        LOGGER.info("Initialized system properties.");

        LOGGER.exit();
    }

    /**
     * Initializes the cache management mechanism.
     */
    @SuppressWarnings("unused")
    private void initCache() {
        LOGGER.entry();
        
        // Start up the cache manager
        final CacheManager cacheManager = CacheManager.getInstance();
        LOGGER.info("Initialized the cache manager.");

        LOGGER.exit();
    }
}