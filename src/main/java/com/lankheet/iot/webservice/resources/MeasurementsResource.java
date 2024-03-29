package com.lankheet.iot.webservice.resources;

import com.codahale.metrics.annotation.Timed;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.webservice.DatabaseManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/measurements")
@Produces(MediaType.APPLICATION_JSON)
public class MeasurementsResource {

    private final DatabaseManager dbManager;
    
    public MeasurementsResource(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    @GET
    @Timed
    public List<Measurement> getMeasurements(@PathParam("sensorId") int sensorId) {
        return dbManager.getMeasurementsBySensor(sensorId);
    }
}
