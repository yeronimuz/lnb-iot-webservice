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

package com.lankheet.iot.webservice.dao;

import java.util.List;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.datatypes.entities.Measurement;

public interface DaoListener {
    /**
     * A client requests all measurements by sensor from the database
     * 
     * @param dUser The user that requests the data
     * @param sensorId The sensorId of which the measurements to get
     * 
     * @return The measurements from the database.
     */
    List<Measurement> getMeasurementsBySensor(DomoticsUser dUser, int sensorId);

    /**
     * Get measurements by sensor AND by type.
     * 
     * @param dUser The user requesting the measurements
     * @param sensorId The sensor that is the origin of the measurement
     * @param type the type of the measurement
     * @return The measurements from the database.
     */
    List<Measurement> getMeasurementsBySensorAndType(DomoticsUser dUser, int sensorId, int type);

    /**
     * Get the latest measurement in the database. <BR>
     * The user requesting the latest entry should be admin.
     * 
     * @param dUser The user requesting the data
     * @return The latest measurement
     */
    Measurement getLatestMeasurement(DomoticsUser dUser);
}
