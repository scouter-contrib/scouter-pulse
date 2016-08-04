package scouterx.pulse.common.http;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import scouterx.pulse.common.protocol.counter.ObjectCounterBean;
import scouterx.pulse.common.protocol.register.RegisterBean;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 31.
 */
public class HttpTrain extends Thread {
    static final String TYPE_REGISTER = "R";
    static final String TYPE_COUNTER_ARRAY = "A";
    static final String TYPE_COUNTER_LIST = "L";
    public static final String ENV_KEY_TARGETADDR = "pulse.targetaddr";
    public static final String DEFAULT_TARGETADDR = "http://localhost:6180/";

    private static ScouterPulseService service;

    class Bucket {
        String type;
        Object obj;

        Bucket(String type, Object o) {
            this.type = type;
            this.obj = o;
        }
    }

    static BlockingQueue<Bucket> queue = new LinkedBlockingQueue<>(100);

    private static HttpTrain instance = null;

    public final static synchronized HttpTrain getInstance() {
        if (instance == null) {
            instance = new HttpTrain();
            instance.setDaemon(true);
            instance.setName(instance.getClass().getName());
            instance.start();
        }
        return instance;
    }

    private HttpTrain() {
    }

    @Override
    public void run() {
        while (true) {
            Object o = null;

            try {
                Bucket bucket = queue.take();
                switch(bucket.type) {
                    case TYPE_REGISTER:
                        getService().register((RegisterBean)bucket.obj).enqueue(callback);
                        break;
                    case TYPE_COUNTER_ARRAY:
                        getService().counter((ObjectCounterBean[]) bucket.obj).enqueue(callback);
                        break;
                    case TYPE_COUNTER_LIST:
                        getService().counter((List<ObjectCounterBean>) bucket.obj).enqueue(callback);
                        break;
                    default:
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    Callback callback = new Callback() {
        @Override
        public void onResponse(Call call, Response response) {
            //System.out.println("[SUCCESS]" + response.code());
        }

        @Override
        public void onFailure(Call call, Throwable throwable) {
            System.out.println("[FAIL]" + throwable.getMessage());
        }
    };

    private synchronized static ScouterPulseService getService() {
        if(service == null) {
            Retrofit client =new Retrofit.Builder()
                    .baseUrl(System.getProperty(ENV_KEY_TARGETADDR, DEFAULT_TARGETADDR))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = client.create(ScouterPulseService.class);
        }
        return service;
    }

    public boolean putRegisterBean(RegisterBean bean) {
        return queue.offer(new Bucket(TYPE_REGISTER, bean));
    }

    public boolean putObjectCounterBeans(ObjectCounterBean[] beans) {
        if(beans == null || beans.length == 0) return true;
        return queue.offer(new Bucket(TYPE_COUNTER_ARRAY, beans));
    }

    public boolean putObjectCounterBeans(List<ObjectCounterBean> beans) {
        if(beans == null || beans.size() == 0) return true;
        return queue.offer(new Bucket(TYPE_COUNTER_LIST, beans));
    }

}
