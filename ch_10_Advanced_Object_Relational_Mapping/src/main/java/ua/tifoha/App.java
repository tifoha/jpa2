package ua.tifoha;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
//        ExecutorService e = Executors.newCachedThreadPool(r -> {
//            Thread t = new Thread(r);
//            t.setDaemon(true);
//            return t;
//        });
//        e.execute(() -> {
//            throw new RuntimeException();
//        });
        final AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
                .setKeepAlive(true)
//                .setMaxConnectionsPerHost(thirdPartyS2SIntegrationConfig.getMaxActiveRequestsPerDsp())
//                .setMaxConnections(thirdPartyS2SIntegrationConfig.getMaxDspAuctionActiveRequests())
//                .setReadTimeout(thirdPartyS2SIntegrationConfig.getReadTimeout())
//                .setPooledConnectionIdleTimeout(thirdPartyS2SIntegrationConfig.getIdleTimeout())
                .build();

        DefaultAsyncHttpClient dspClient = new DefaultAsyncHttpClient(config);
        dspClient.close();
    }
}
