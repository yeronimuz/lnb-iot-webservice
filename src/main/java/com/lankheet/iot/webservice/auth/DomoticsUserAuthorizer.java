package com.lankheet.iot.webservice.auth;

import com.lankheet.iot.datatypes.entities.DomoticsUser;
import io.dropwizard.auth.Authorizer;

/** 
 * Authorizer for the Domotics webservice.
 * 
 */
public class DomoticsUserAuthorizer implements Authorizer<DomoticsUser> {
    @Override
    public boolean authorize(DomoticsUser user, String role) {
        return user.getName().equals("good-guy") && role.equals("ADMIN");
    }
}