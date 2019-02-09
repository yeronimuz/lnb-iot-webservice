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
     * @param measurementType The type of measurement to get
     * @param startTime Start time of the measurements, startTime <= measurement.timeStamp
     * @param endTime End time of the measurements, end time > measurement.timeStamp
     * 
     * @return The measurements from the database.
     */
    List<Measurement> getMeasurements(DomoticsUser dUser, int sensorId, int measurementType, String startTime, String endTime);

    /**
     * Get the latest measurement in the database. <BR>
     * The user requesting the latest entry should be admin.
     * 
     * @param dUser The user requesting the data
     * @return The latest measurement
     */
    Measurement getLatestMeasurement(DomoticsUser dUser);
}
