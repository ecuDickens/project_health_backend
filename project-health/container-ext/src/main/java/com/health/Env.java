package com.health;


import com.google.common.base.Strings;

public class Env {

    /** GWAPP_ENV_TYPE */
    private final String environment;

    /** GWAPP_DOMAIN_TYPE */
    private final String domain;

    private final String version;

    /** configuration root */
    private final String configRoot;

    /** Templates root */
    private final String templatesRoot;

    /** Developer email group */
    private final String developerEmailGroup;

    /** Default external from email */
    private final String defaultExternalFromEmail;

    /** Default base path for templates on S3 */
    private final String templateBasePath;

    /** Top level maven project */
    private final String parentMavenProject;

    /** Config files project */
    private final String configFilesMavenProject;

    private final String currentUserName;

    public Env(String environment, String domain, String version, String configRoot, String templatesRoot, String developerEmailGroup, String defaultExternalFromEmail, String templateBasePath, String parentMavenProject, String configFilesMavenProject, String currentUserName) {
        this.environment = environment;
        this.domain = domain;
        this.version = version;
        this.configRoot = configRoot;
        this.templatesRoot = templatesRoot;
        this.developerEmailGroup = developerEmailGroup;
        this.defaultExternalFromEmail = defaultExternalFromEmail;
        this.templateBasePath = templateBasePath;
        this.parentMavenProject = parentMavenProject;
        this.configFilesMavenProject = configFilesMavenProject;
        this.currentUserName = currentUserName;
    }

    public Env withEnvironment(String override) {
        return new Env(override, domain, version, configRoot, templatesRoot, developerEmailGroup, defaultExternalFromEmail, templateBasePath, parentMavenProject, configFilesMavenProject, currentUserName);
    }

    public String getConfigFilesMavenProject() {
        return configFilesMavenProject;
    }

    public String getConfigRootBase() {
        return "/config/";
    }

    public String getDomain() {
        return domain;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getParentMavenProject() {
        return parentMavenProject;
    }

    public String getVersion() {
        return version;
    }

    // Returns true if we are running in a standalone JVM
    public boolean isStandaloneJvm() {
        final String property = System.getProperty("health.standalone", "true");
        return ("true".equals(property));
    }

    //Returns true if a domain variable has been set.
    public boolean isDomainSet() {
        return !Strings.isNullOrEmpty(getDomain());
    }

    // Returns the current user name taken from the standard Java system property <code>user.name</code>.
    public String getCurrentUserName() {
        return currentUserName;
    }

    // Returns the root directory we are executing in
    public String getHealthRoot() {
        final String root;
        if (isStandaloneJvm()) {
            // if the user dir ends with project-health, use the config files
            final String userDir = System.getProperty("user.dir");
            if (userDir.endsWith(getParentMavenProject())) {
                root = userDir + '/' + getConfigFilesMavenProject() + "/src/site";
            } else {
                root = userDir + "/src/main/resources";
            }
        } else {
            root = System.getProperty("health.root");
        }
        return root;
    }

    // Returns our configuration root
    public String getConfigRoot() {
        return getHealthRoot() + "/config";
    }
}
