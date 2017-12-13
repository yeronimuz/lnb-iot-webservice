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

package com.lankheet.iot.webservice.testutils;

import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.utils.JsonUtil;

public class TestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

    public static void sendMqttMeasurements(MqttClient mqttClient, List<Measurement> measurements) throws Exception {
         for (Measurement measurement : measurements) {
            LOG.info(measurement.toString());
            sendMqttMeasurement(mqttClient, measurement);
        }
    }

    public static void sendMqttMeasurement(MqttClient mqttClient, Measurement measurement) throws Exception {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(JsonUtil.toJson(measurement).getBytes());
        mqttClient.publish("test", msg);
    }

    public static MqttClient createMqttClientConnection() throws MqttSecurityException, MqttException {
        MqttClient mqttClient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        options.setUserName("testuser");
        options.setPassword("testpassword".toCharArray());
        mqttClient.connect(options);
        return mqttClient;
    }
}
