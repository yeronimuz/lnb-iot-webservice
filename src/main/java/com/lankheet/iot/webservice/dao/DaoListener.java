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
