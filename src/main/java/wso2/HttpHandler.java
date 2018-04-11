package wso2;


import com.google.common.io.BaseEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/*
 * Handle get post request to backend
 */
public class HttpHandler {
    private static final Logger logger = Logger.getLogger(HttpHandler.class);
    private static final PropertyReader propertyReader = new PropertyReader();
    private String backendPassword;
    private String backendUsername;
    private String backendUrl;
    private String trustStorePassword;


    public HttpHandler() {
        this.backendPassword = propertyReader.getBackendPassword();
        this.backendUsername = propertyReader.getBackendUsername();
        this.backendUrl = propertyReader.getBackendUrl();
        this.trustStorePassword = propertyReader.getTrustStorePassword();
    }

    public String httpsGet(String url) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
////        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

//        System.setProperty("javax.net.ssl.trustStore", propertyReader.getTrustStoreFile());
//        System.setProperty("javax.net.ssl.trustStorePassword", propertyReader.getTrustStorePassword());
//        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
//        System.setProperty("carbon.repo.write.mode", "true");

//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        InputStream file = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(propertyReader.getTrustStoreFile());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        keyStore.load(file, propertyReader.getTrustStorePassword().toCharArray());
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStore,null).build();
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,allowAllHosts);

//        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
//                .loadTrustMaterial(keyStore,null).build()));
//        CloseableHttpClient httpClient = httpClientBuilder.build();


//        SSLContext sslContext = null;
//        try{
//            sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
//        } catch (NoSuchAlgorithmException  e){
//            logger.error(e);
//        } catch (KeyStoreException e){
//            logger.error(e);
//        } catch (KeyManagementException e){
//            logger.error(e);
//        }
//        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
//        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,allowAllHosts);
//
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();


        HttpGet request = new HttpGet(this.backendUrl + url);
        request.addHeader("Accept", "application/json");
        String encodedCredentials = this.encode(this.backendUsername + ":" + this.backendPassword);
        request.addHeader("Authorization", "Basic " + encodedCredentials);
        String responseString = null;

        try {

            HttpResponse response = httpClient.execute(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Request successful for " + url);
            }
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (IllegalStateException e) {
            logger.error("The response is empty ");
        } catch (NullPointerException e) {
            logger.error("Bad request to the URL");
        } catch (IOException e) {
            logger.error("mke");
        }
        return responseString;
    }


    private String encode(String text) {
        String returnString = null;
        try {
            returnString = BaseEncoding.base64().encode(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }

}
