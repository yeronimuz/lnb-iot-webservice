/**
 * MIT License
 * 
 * Copyright (c) 2017 Lankheet Software and System Solutions
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.lankheet.iot.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import com.lankheet.iot.webservice.config.MqttConfig;
import com.lankheet.iot.webservice.dao.DaoListener;
import io.dropwizard.lifecycle.Managed;

/**
 * MQTT client manager that subscribes to the domotics topics.
 *
 */
public class MqttClientManager implements Managed {
    private static final Logger LOG = LoggerFactory.getLogger(MqttClientManager.class);

    private MqttClient client;
    private final MqttConnectOptions options = new MqttConnectOptions();;

    public MqttClientManager(MqttConfig mqttConfig, DaoListener dao) throws MqttException {
        String userName = mqttConfig.getUserName();
        String password = mqttConfig.getPassword();
        client = new MqttClient(mqttConfig.getUrl(), MqttClient.generateClientId());

        client.setCallback(new NewMeasurementCallback(this, dao));
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
    }

    /**
     * TODO: This method should be triggered by the database command table change in order to configure
     * clients.<BR>
     * Possible commands: Command to switch something, Config to set a (set of) parameter(s), Measure:
     * Invoke a sensor to generate a new measurement
     * 
     * @param topic The command topic {CMD, CONFIG, MEASURE}
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(String topic, MqttMessage message) throws MqttPersistenceException, MqttException {
        client.publish(topic, message);
    }

    @Override
    public void start() throws Exception {
        LOG.info("Connecting mqtt broker with options: {}", options);
        client.connect(options);
        client.subscribe("#", 0);
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Closing mqtt connection...");
        client.disconnect();
    }

    public MqttClient getClient() {
        return client;
    }
}
