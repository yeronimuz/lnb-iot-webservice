package com.lankheet.iot.webservice.resources;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.codahale.metrics.annotation.Timed;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.webservice.DatabaseManager;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.BasicAuthDefinition;
import io.swagger.annotations.OAuth2Definition;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;


@Api("/")
@Path("/")
/*
@SwaggerDefinition(
    securityDefinition =
        @SecurityDefinition(
            basicAuthDefinitions = {@BasicAuthDefinition(key = "basic")},
            oAuth2Definitions = {
              @OAuth2Definition(
                  flow = OAuth2Definition.Flow.IMPLICIT,
                  key = "oauth2",
                  authorizationUrl = "/oauth2/auth")
            }))*/
public class MeasurementsResource {

    private DatabaseManager dbManager;

    public MeasurementsResource(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @GET
    @Path("/measurements")
    @Produces(MediaType.APPLICATION_JSON)
    // @RolesAllowed("ADMIN")
    @Timed
    @ApiOperation(value = "Measurements", notes = "Get measurement by sensor id", response = List.class
            /*authorizations = {@Authorization("basic"), @Authorization("oauth2")}*/)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "The measurement"),
            @ApiResponse(code = 400, message = "Sensor not found")})
    public List<Measurement> getMeasurements(@Auth DomoticsUser dUser, @QueryParam("sensorId") int sensorId) {
        return dbManager.getMeasurementsBySensor(dUser, sensorId);
    }
}
