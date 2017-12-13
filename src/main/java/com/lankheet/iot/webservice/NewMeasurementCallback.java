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
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.iot.webservice.dao.DaoListener;
import com.lankheet.utils.JsonUtil;

public class NewMeasurementCallback implements MqttCallback {
    private static final Logger LOG = LoggerFactory.getLogger(NewMeasurementCallback.class);
    private MqttClientManager mqttClientManager;
    private DaoListener daoListener;

    public NewMeasurementCallback(MqttClientManager mqttClientManager, DaoListener dao) {
        this.mqttClientManager = mqttClientManager;
        this.daoListener = dao;
    }

    @Override
    public void connectionLost(Throwable cause) {
        LOG.warn("Connection lost: {}", cause.getMessage());
        try {
            mqttClientManager.getClient().reconnect();
        } catch (MqttException e) {
            LOG.error("Could not reconnect: {}", e.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        LOG.info("Topic: {}, message: {}", topic, message.toString());
        String payload = new String(message.getPayload());
        LOG.info("Payload: {}", payload);
        Measurement newMmeasurement = null;
        try {
            newMmeasurement = JsonUtil.measurementFromJson(payload);
        } catch(JsonProcessingException e ) {
            LOG.error(e.getMessage());
        }
        LOG.info("Starting store action");
        daoListener.newMeasurement(newMmeasurement);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOG.info("Delivery complete: {}", token);
    }
}
