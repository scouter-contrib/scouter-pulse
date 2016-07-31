package scouterx.pulse.protocol.counter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
@Data
public class ObjectCounterBean {
    private ObjectValue object;
    private List<CounterValue> counters;

    private ObjectCounterBean(ObjectValue object, List<CounterValue> counters) {
        this.object = object;
        this.counters = counters;
    }

    public static class Builder {
        private ObjectValue object;
        private List<CounterValue> counters = new ArrayList<>();

        public Builder setObject(ObjectValue object) {
            this.object = object;
            return this;
        }

        public Builder addCounterValue(CounterValue counter) {
            this.counters.add(counter);
            return this;
        }

        public ObjectCounterBean build() {
            return new ObjectCounterBean(this.object, this.counters);
        }
    }
}


/*
 [
 {
 	"object" : {
 	    "host" : "VM123",
 		"name" : "my_server1",
 		"type" : "redis",
 		"address" : "10.10.10.10"
 	},
	"counters" : [
		{"name" : "aof_rewrite_scheduled", "value" : 55},
		{"name" : "client_longest_output_list", "value" : 245},
		{"name" : "used_cpu_user", "value" : 4245}
	]
 },
 {
 	"object" : {
 	    "host" : "VM123",
 		"name" : "my_server2",
 		"type" : "redis",
 		"address" : "10.10.10.10"
 	},
	"counters" : [
		{"name" : "aof_rewrite_scheduled", "value" : 35},
		{"name" : "client_longest_output_list", "value" : 65},
		{"name" : "used_cpu_user", "value" : 8888}
	]
 }

 ]
 */