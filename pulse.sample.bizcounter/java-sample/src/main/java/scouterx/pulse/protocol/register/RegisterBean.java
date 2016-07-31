package scouterx.pulse.protocol.register;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 30.
 */
@Data
public class RegisterBean {
    ObjectDef object;
    private List<CounterDef> counters = new ArrayList<>();

    private RegisterBean(Builder builder) {
        this.object = builder.object;
        this.counters = builder.counters;
    }

    public static class Builder {
        private ObjectDef object;
        private List<CounterDef> counters = new ArrayList<>();

        public Builder setObjectSpec(ObjectDef objectSpec) {
            this.object = objectSpec;
            return this;
        }

        public Builder addCounterSpec(CounterDef counterSpec) {
            this.counters.add(counterSpec);
            return this;
        }

        public RegisterBean build() {
            return new RegisterBean(this);
        }
    }
}

//{
//        "object" : {
//        "type" : "redis",
//        "display" : "Redis"
//        },
//        "counters" : [
//        {"name" : "aof_rewrite_scheduled",
//        "unit" : "cnt",
//        "display" : "AofRewriteScheduled",
//        "total" : true,
//        "all" : true
//        },
//        {"name" : "client_longest_output_list",
//        "unit" : "cnt",
//        "display" : "ClientLongOutList",
//        },
//        {"name" : "used_cpu_user",
//        "unit" : "cnt",
//        "display" : "UsedCpuUser",
//        "total" : false
//        },
//        ]
//        }

