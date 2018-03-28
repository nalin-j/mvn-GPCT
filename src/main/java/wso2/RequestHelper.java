package wso2;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/*
 * Common helper methods for servlets
 */
public class RequestHelper {
    private static final Logger logger = Logger.getLogger(RequestHelper.class);
    private static BufferedReader reader = null;

    public static String getRequestBody(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        try {
            reader = request.getReader();
            if (reader == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Request body could not be read because it's empty.");
                }
                return null;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (final Exception e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failed to close the buffered reader");
                    }
                }
            }
        }
    }
}
