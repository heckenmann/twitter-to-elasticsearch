# twitter-to-elasticsearch
Twitter Stream API to elasticsearch: https://dev.twitter.com/streaming/overview

Get the public twitter-stream for any interesting hitwords and pump it in an elasticsearch database to analyze it (for example with kibana).

## Usage
1. Install and run an elasticsearch instance or cluster. Install elasticsearch-head (optional).
2. Install template using curl / elasticsearch-head / kibana sense. Template is placed in ```elasticsearch/template.json```. Remember the index-prefix. Default is "twitter". This step is required to map the date field correctly.
3. Create a twitter consumer-key and consumer-secret, token and token-secret (https://apps.twitter.com).
4. Add a mission with curl / elasticsearch-head / kibana sense like in ```elasticsearch/add_mission.json```. In the mission you have to set the secrets and token you got from twitter. Remember the node-name.
5. Install Apache Maven and compile application in app-root with ```mvn clean install -Dmaven.test.skip=true```. The jar (target/TwitterToElasticsearch-jar-with-dependencies.jar) can be started with ```java -jar -DNODE_NAME=<YOUR_NODE_NAME> TwitterToElasticsearch-jar-with-dependencies.jar```. To change the elasticsearch url you must set the variable "TTE_ELASTICSEARCH_URL". The node-name must be equal to the node-name in your mission.

Would be happy about your feedback.
