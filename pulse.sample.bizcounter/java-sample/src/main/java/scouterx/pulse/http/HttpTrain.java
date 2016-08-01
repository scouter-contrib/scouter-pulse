package scouterx.pulse.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import scouterx.pulse.protocol.counter.ObjectCounterBean;
import scouterx.pulse.protocol.register.RegisterBean;
import scouterx.pulse.sample.bizcounter.App;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 31.
 */
public class HttpTrain extends Thread {
    static final String TYPE_REGISTER = "R";
    static final String TYPE_COUNTER_ARRAY = "A";
    static final String TYPE_COUNTER_LIST = "L";

    class Bucket {
        String type;
        Object obj;

        Bucket(String type, Object o) {
            this.type = type;
            this.obj = o;
        }
    }

    static BlockingQueue<Bucket> queue = new LinkedBlockingDeque<>(100);

    static Retrofit retrofit = null;

    private static HttpTrain instance = null;

    public final static synchronized HttpTrain getInstance() {
        if (instance == null) {
            retrofit =new Retrofit.Builder()
                    .baseUrl(App.targetAddress)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
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
            queue.stream().forEach(bucket -> {
                switch(bucket.type) {
                    case TYPE_REGISTER:
                        break;
                    case TYPE_COUNTER_ARRAY:
                        break;
                    case TYPE_COUNTER_LIST:
                        break;
                    default:
                        break;
                }
            });
        }
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
