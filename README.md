# scouter-pulse
Pulse is the platform for building lightweight agent program for many types of data you want to enrich with Scouter. Whether you’re interested in system performance metrics.

### Quick Start
1. Start up scouter server with http enabled option
 > net_http_server_enabled=true

2. Register object type and counter definition list by json once. It should be returned 201.
 > http://server_ip:6180/register
	```json
	{
		"object" : {
			"type" : "type_name",
			"display" : "DisplayName"
		},
		"counters" : [
			{"name" : "counter1",
			 "unit" : "cnt",
			 "display" : "Counter1",
			},
			{"name" : "counter2",
			 "unit" : "cnt",
			 "display" : "Counter2",
			},
			{"name" : "counter3",
			 "unit" : "cnt",
			 "display" : "Counter3",
			 "total" : false
			},
		]
	}
	```
	
3. Send object heartbeat and counter value by json periodically(recommended 2~5sec).
  > http://server_ip:6180/counters
	```json
	[
		 {
		 	"object" : {
		 	  "host" : "host1",
		 		"name" : "name1",
		 		"type" : "type_name",
		 		"address" : "10.10.10.10"
		 	},
			"counters" : [
				{"name" : "counter1", "value" : 55},
				{"name" : "counter2", "value" : 245},
				{"name" : "counter3", "value" : 4245}
			]
		 },
		 {
		 	"object" : {
		 	    "host" : "host2",
		 		"name" : "name2",
		 		"type" : "type_name",
		 		"address" : "10.10.10.11"
		 	},
			"counters" : [
				{"name" : "counter1", "value" : 35},
				{"name" : "counter2", "value" : 65},
				{"name" : "counter3", "value" : 8888}
			]
		 }
	 ]
	 ```




