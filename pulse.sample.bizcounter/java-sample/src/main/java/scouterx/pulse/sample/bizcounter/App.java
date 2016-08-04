package scouterx.pulse.sample.bizcounter;

import org.apache.commons.cli.*;
import scouterx.pulse.common.http.HttpTrain;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
public class App {
    public static String targetAddress = null;

    public static void main(String[] args) {
        Options options = new Options();

        Option address = new Option("t", "target", true, "target address, default => " + HttpTrain.DEFAULT_TARGETADDR);
        address.setRequired(false);
        options.addOption(address);

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        formatter.printHelp("options", options);

        try{
            cmd = parser.parse(options, args);
        } catch(ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
        System.setProperty(HttpTrain.ENV_KEY_TARGETADDR, cmd.getOptionValue("target", HttpTrain.DEFAULT_TARGETADDR));

        BizSampleAgent.getInstance().start();

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
