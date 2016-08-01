package scouterx.pulse.sample.bizcounter;

import scouterx.pulse.protocol.counter.CounterValue;
import scouterx.pulse.protocol.counter.ObjectCounterBean;
import scouterx.pulse.protocol.counter.ObjectValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
public class BizDataController {
    private static Map<String, Float> prev = new HashMap<>();

    static {
        prev.put("ops1", 100f);
        prev.put("ops2", 200f);
        prev.put("ops3", 300f);
        prev.put("ops4", 400f);
        prev.put("ops5", 500f);

        prev.put("onairopm", 700f);
        prev.put("onairapm", 200f);
        prev.put("onaircpm", 34.5f);
    }

    public List<ObjectCounterBean> getRealtimeOrderInfoAsObjectCounterBean() {
        List<ObjectCounterBean> list = new ArrayList<>();

        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1001", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops1")))
                .build());
        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1021", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops2")))
                .build());
        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1139", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops3")))
                .build());
        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1240", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops4")))
                .build());

        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("onair-info", "onair", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_ONAIROPM, getRandomSample("onairopm")))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_ONAIRAPM, getRandomSample("onairapm")))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_ONAIROSR, getRandomSample("onaircpm")))
                .build());

        return list;
    }

    private float getRandomSample(String type) {
        Float value = prev.get(type);
        if (value == null) {
            value = 10.0f;
            prev.put(type, value);
        }

        value = value + ((float) Math.random() - 0.5f) * value / 30;

        return value;
    }


}
