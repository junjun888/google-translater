package org.hwj;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Proxy;
import java.net.URI;
import java.util.Map;

public class HttpClientUtil {

    public static final int SO_TIMEOUT_MS = 8000;
    public static final int CONNECTION_TIMEOUT_MS = 1000;

    public static String doGet(String url, Map<String, String> param) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, (String) param.get(key));
                }
            }
            URI uri = builder.build();

            HttpGet httpGet = new HttpGet(uri);

            response = httpclient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200)
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url, Map<String, String> param, Proxy proxy) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, (String) param.get(key));
                }
            }
            URI uri = builder.build();

            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(buildRequestConfig(proxy));

            response = httpclient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200)
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGetWithProxy(String url, Proxy proxy) throws Exception {
        return doGet(url, null, proxy);
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static RequestConfig buildRequestConfig(Proxy proxy) {
        String address = proxy.address().toString();
        String[] addressArr = address.replace("/", "").split(":");
        String ip = addressArr[0].trim();
        String host = addressArr[1].trim();

        if ((proxy != null) && (!(StringUtils.isBlank(ip))) && (!(StringUtils.isBlank(host)))) {
            HttpHost httpHost = new HttpHost(ip, Integer.parseInt(host));
            RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost)
                    .setSocketTimeout(SO_TIMEOUT_MS)
                    .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
            return requestConfig;
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }
}
