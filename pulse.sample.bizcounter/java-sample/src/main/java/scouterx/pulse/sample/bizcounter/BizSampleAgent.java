package scouterx.pulse.sample.bizcounter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
public class BizSampleAgent {
    private static Retrofit retrofit = null;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
            (r) -> {
                Thread t = new Thread(r, "BizSampleAgentScheduledThread");
                t.setDaemon(true);
                return t;
            }
    );

    public void start() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://127.0.0.1:6180")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        executor.scheduleAtFixedRate(new BizSendTask(), 2000, 2000, TimeUnit.MILLISECONDS);
    }

    private class BizSendTask implements Runnable {
        @Override
        public void run() {
            System.out.println("gogogo");
        }
    }


}
