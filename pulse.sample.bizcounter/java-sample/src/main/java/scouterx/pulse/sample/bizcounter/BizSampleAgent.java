package scouterx.pulse.sample.bizcounter;

import scouterx.pulse.http.HttpTrain;
import scouterx.pulse.protocol.register.CounterDef;
import scouterx.pulse.protocol.register.ObjectDef;
import scouterx.pulse.protocol.register.RegisterBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
public class BizSampleAgent {
    public static final String OBJECT_TYPE = "scouterbizsample1";
    public static final String COUNTER_OPM = "OPM";
    public static final String COUNTER_DAYORDER = "DayOrder";
    public static final String COUNTER_ONAIROPM = "OnAirOPM";
    public static final String COUNTER_ONAIRAPM = "OnAirAPM";
    public static final String COUNTER_ONAIROSR = "OnAirOSR";

    static BizSampleAgent instance = new BizSampleAgent();

    private BizSampleAgent() {}

    public static BizSampleAgent getInstance() {
        return instance;
    }

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
            (r) -> {
                Thread t = new Thread(r, "BizSampleAgentScheduledThread");
                t.setDaemon(true);
                return t;
            }
    );

    public void start() {
        executor.scheduleAtFixedRate(new RegisterTask(), 500, 5*60*1000, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(new BizSendTask(), 4000, 2000, TimeUnit.MILLISECONDS);
    }

    private class RegisterTask implements Runnable {
        @Override
        public void run() {
            RegisterBean rbean = new RegisterBean.Builder()
                    .setObjectSpec(new ObjectDef(OBJECT_TYPE, "Biz Sample 1"))
                    .addCounterSpec(new CounterDef(COUNTER_OPM, "o/m", "order/minute", true, true))
                    .addCounterSpec(new CounterDef(COUNTER_DAYORDER, "ea", "Daily Order Count", true, true))
                    .addCounterSpec(new CounterDef(COUNTER_ONAIROPM, "o/m", "On Air order/minute", true, true))
                    .addCounterSpec(new CounterDef(COUNTER_ONAIRAPM, "o/m", "On Air abandon/minute", true, true))
                    .addCounterSpec(new CounterDef(COUNTER_ONAIROSR, "%", "On Air order success rate", true, true))
                    .build();

            HttpTrain.getInstance().putRegisterBean(rbean);
        }
    }

    private class BizSendTask implements Runnable {
        @Override
        public void run() {
            HttpTrain.getInstance().putObjectCounterBeans(new BizDataController().getRealtimeOrderInfoAsObjectCounterBean());
        }
    }


}
