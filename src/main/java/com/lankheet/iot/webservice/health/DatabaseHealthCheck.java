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

package com.lankheet.iot.webservice.health;

import java.util.Date;
import com.codahale.metrics.health.HealthCheck;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.webservice.DatabaseManager;

/**
 * Healthcheck for the database.
 */
public class DatabaseHealthCheck extends HealthCheck {
    private static Date previousLatestMeasurement;
    private DatabaseManager dbManager;

    /**
     * Constructor.
     * 
     * @param dbManager The DatabaseManager that is consulted.
     */
    public DatabaseHealthCheck(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        previousLatestMeasurement = new Date();
        previousLatestMeasurement.setTime(1L);
    }

    @Override
    protected Result check() {
        Result result = Result.unhealthy("Nothing retrieved from database");
        Measurement measurement = dbManager.getLatestMeasurement(new DomoticsUser("user", "password"));
        if (measurement != null) {
            long diff = measurement.getTimeStamp().getTime() - previousLatestMeasurement.getTime();
            previousLatestMeasurement = measurement.getTimeStamp();
            result = Result.healthy("Healthy; " + diff + " ms");
        }
        return result;
    }
}
