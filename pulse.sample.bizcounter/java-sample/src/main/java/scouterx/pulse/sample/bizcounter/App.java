package scouterx.pulse.sample.bizcounter;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
public class App {
    public static void main(String[] args) {

        BizSampleAgent agent = new BizSampleAgent();
        agent.start();

        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        CounterDef[] counters = new CounterDef[2];
//        counters[0] = new CounterDef("cpu", "tick", "CPU", true, false);
//        counters[1] = new CounterDef("mem", "pct", "Memory", true, false);
//
//        RegisterBean type = new RegisterBean.Builder()
//                .setObjectSpec(new ObjectDef("redis", "myredis"))
//                .addCounterSpec(new CounterDef("cpu", "tick", "CPU", true, false))
//                .addCounterSpec(new CounterDef("mem", "pct", "Memory", true, false))
//                .build();
//
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(type));
//
//
//        ObjectCounterBean[] beans = {
//                new ObjectCounterBean.Builder()
//                        .setObject(new ObjectValue("gunhost", "agent1", "redis", "127.0.0.1"))
//                        .addCounterValue(new CounterValue("cpu", 100.23))
//                        .addCounterValue(new CounterValue("mem", 1004830))
//                        .build(),
//                new ObjectCounterBean.Builder()
//                        .setObject(new ObjectValue("gunhost", "agent2", "redis", "127.0.0.2"))
//                        .addCounterValue(new CounterValue("cpu", 100.2223))
//                        .addCounterValue(new CounterValue("mem", 444830))
//                        .addCounterValue(new CounterValue("disk", 230))
//                        .build(),
//        };
//        System.out.println(gson.toJson(beans));

    }
}
