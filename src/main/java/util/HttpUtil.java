package util;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HttpUtil {

    private static SSLContext sslContext;

    static {
        try {
            sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                    }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getJson(String url, Map<String,String> headers){
        Unirest.config().connectTimeout(5000).socketTimeout(10000).sslContext(sslContext).hostnameVerifier((hostname, session) -> true);
        HttpResponse<JsonNode> json;
        try{
            if (headers == null){
                json = Unirest.get(url).asJson();
            }else {
                json = Unirest.get(url).headers(headers).asJson();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Unirest.shutDown();
        return json.getBody().getObject();
    }

    public static HttpResponse<String> getString(String url,Map<String,String> headers){
        Unirest.config().connectTimeout(5000).socketTimeout(10000).sslContext(sslContext).hostnameVerifier((hostname, session) -> true);
        HttpResponse<String> response;
        try{
            if (headers == null){
                response = Unirest.get(url).asString();
            }else {
                response = Unirest.get(url).headers(headers).asString();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Unirest.shutDown();
        return response;
    }

    public static HttpResponse<String> postString(String url,Map<String,String> headers,String body){
        Unirest.config().connectTimeout(5000).socketTimeout(10000).sslContext(sslContext).hostnameVerifier((hostname, session) -> true);
        HttpResponse<String> response;
        try{
            if (headers == null){
                response = Unirest.post(url).body(body).asString();
            }else {
                response = Unirest.post(url).headers(headers).body(body).asString();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Unirest.shutDown();
        return response;
    }

    public static HttpResponse<String> postString(String url,Map<String,String> headers,byte[] body){
        Unirest.config().connectTimeout(60000).socketTimeout(60000).sslContext(sslContext).hostnameVerifier((hostname, session) -> true);
        HttpResponse<String> response;
        try{
            if (headers == null){
                response = Unirest.post(url).body(body).asString();
            }else {
                response = Unirest.post(url).headers(headers).body(body).asString();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Unirest.shutDown();
        return response;
    }

    public static JSONObject postJson(String url,Map<String,String> headers,String body){
        Unirest.config().connectTimeout(5000).socketTimeout(10000).sslContext(sslContext).hostnameVerifier((hostname, session) -> true);
        HttpResponse<JsonNode> json;
        try{
            if (headers == null){
                json = Unirest.post(url).body(body).asJson();
            }else {
                json = Unirest.post(url).headers(headers).body(body).asJson();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Unirest.shutDown();
        return json.getBody().getObject();
    }

}
