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
    private static Map<String, Float> prev2 = new HashMap<>();

    static {
        prev.put("ops1", 100f);
        prev.put("ops2", 200f);
        prev.put("ops3", 300f);
        prev.put("ops4", 400f);
        prev.put("ops5", 500f);

        prev2.put("ops1", 100f);
        prev2.put("ops2", 200f);
        prev2.put("ops3", 300f);
        prev2.put("ops4", 400f);
        prev2.put("ops5", 500f);

        prev.put("onairopm", 700f);
        prev.put("onairapm", 200f);
        prev.put("onaircpm", 34.5f);
    }

    public List<ObjectCounterBean> getRealtimeOrderInfoAsObjectCounterBean() {
        List<ObjectCounterBean> list = new ArrayList<>();

        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1001", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops1")))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_DAYORDER, getRandomSample2("ops1d")))
                .build());
        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1021", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops2")))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_DAYORDER, getRandomSample2("ops2d")))
                .build());
        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1139", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops3")))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_DAYORDER, getRandomSample2("ops3d")))
                .build());
        list.add(new ObjectCounterBean.Builder()
                .setObject(new ObjectValue("product-info", "prod-1240", BizSampleAgent.OBJECT_TYPE, "noaddress"))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_OPM, getRandomSample("ops4")))
                .addCounterValue(new CounterValue(BizSampleAgent.COUNTER_DAYORDER, getRandomSample2("ops4d")))
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

        if(value <= 50) {
            value = value + ((float) Math.random() - 0.1f) * value / 20;
        } else if(value <= 200) {
            value = value + ((float) Math.random() - 0.3f) * value / 10;
        } else {
            value = value + ((float) Math.random() - 0.5f) * value / 5;
        }

        prev.put(type, value);

        return value;
    }

    private float getRandomSample2(String type) {
        Float value = prev2.get(type);
        if (value == null) {
            value = 10.0f;
            prev.put(type, value);
        }

        value = value + (float) Math.random() * value / 20;
        prev2.put(type, value);

        return value;
    }


}
