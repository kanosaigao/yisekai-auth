package com.yisekai.manager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    public HttpHelper() {
    }

    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            InputStream inputStream = request.getInputStream();
            Throwable var4 = null;

            try {
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line = "";

                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Throwable var28) {
                var4 = var28;
                throw var28;
            } finally {
                if (inputStream != null) {
                    if (var4 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var27) {
                            var4.addSuppressed(var27);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }
        } catch (IOException var30) {
            LOGGER.warn("getBodyString出现问题！");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var26) {
                    LOGGER.error(ExceptionUtils.getMessage(var26));
                }
            }

        }

        return sb.toString();
    }
}
