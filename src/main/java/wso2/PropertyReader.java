package wso2;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * Read the properties of the application
 */
public class PropertyReader {

    private final static Logger logger = Logger.getLogger(PropertyReader.class);
    private final static String CONFIG_FILE = "config.properties";
    private String backendUrl;
    private String backendPassword;
    private String backendUsername;
    private String ssoKeyStoreName;
    private String ssoKeyStorePassword;
    private String ssoCertAlias;
    private String ssoRedirectUrl;
    private String trustStoreFile;
    private String trustStorePassword;


    public PropertyReader() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        loadConfigs(inputStream);
    }


    /**
     * Load configs from the file
     *
     * @param input - input stream of the file
     */
    private void loadConfigs(InputStream input) {
        Properties prop = new Properties();
        try {
            prop.load(input);
            this.backendUrl = prop.getProperty("backend_url");
            this.backendPassword = prop.getProperty("backend_password");
            this.backendUsername = prop.getProperty("backend_username");
            this.ssoKeyStoreName = prop.getProperty("sso_keystore_file_name");
            this.ssoKeyStorePassword = prop.getProperty("sso_keystore_password");
            this.ssoCertAlias = prop.getProperty("sso_certificate_alias");
            this.ssoRedirectUrl = prop.getProperty("sso_redirect_url");
            this.trustStoreFile = prop.getProperty("trust_store_file_name");
            this.trustStorePassword = prop.getProperty("trust_store_password");


        } catch (FileNotFoundException e) {
            logger.error("The configuration file is not found");
        } catch (IOException e) {
            logger.error("The File cannot be read");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("The File InputStream is not closed");
                }
            }
        }

    }

    String getBackendUrl() {
        return this.backendUrl;
    }

    String getBackendUsername() {
        return this.backendUsername;
    }

    String getBackendPassword() {
        return this.backendPassword;
    }

    public String getSsoKeyStoreName() {
        return this.ssoKeyStoreName;
    }

    public String getSsoKeyStorePassword() {
        return this.ssoKeyStorePassword;
    }

    public String getSsoCertAlias() {
        return this.ssoCertAlias;
    }

    public String getSsoRedirectUrl() {
        return this.ssoRedirectUrl;
    }

    public String getTrustStoreFile() { return this.trustStoreFile; }

    public String getTrustStorePassword() { return this.trustStorePassword; }
}

