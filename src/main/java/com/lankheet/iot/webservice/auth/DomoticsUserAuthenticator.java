package com.lankheet.iot.webservice.auth;

import java.util.Optional;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * description.
 *
 */
public class DomoticsUserAuthenticator implements Authenticator<BasicCredentials, DomoticsUser> {
    @Override
    public Optional<DomoticsUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword()) && "userName".equals(credentials.getUsername())) {
            return Optional.of(new DomoticsUser(credentials.getUsername(), credentials.getPassword()));
        }
        return Optional.empty();
    }
}
