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

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.webservice.auth.DomoticsUserAuthenticator;
import com.lankheet.iot.webservice.auth.DomoticsUserAuthorizer;
import com.lankheet.iot.webservice.config.WebServiceConfig;
import com.lankheet.iot.webservice.health.DatabaseHealthCheck;
import com.lankheet.iot.webservice.resources.MeasurementsResource;
import com.lankheet.iot.webservice.resources.WebServiceInfoResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * The web service has the following tasks:<BR>
 * <li>It accepts measurements from the message broker
 * <li>It saves the measurements in the database
 * <li>It serves as a resource for the measurements database
 *
 */
public class WebService extends Application<WebServiceConfig> {
    private static final Logger LOG = LoggerFactory.getLogger(WebService.class);

    private WebServiceConfig configuration;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            LOG.error("Missing or wrong arguments");
        } else {
            new WebService().run(args[0], args[1]);
        }
    }

    @Override
    public void initialize(Bootstrap<WebServiceConfig> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<WebServiceConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(WebServiceConfig configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
        LOG.info("Lankheet LNB IOT web service", "");
    }

    @Override
    public void run(WebServiceConfig configuration, Environment environment) throws Exception {
        this.setConfiguration(configuration);
        DatabaseManager dbManager = new DatabaseManager(configuration.getDatabaseConfig());
        WebServiceInfoResource webServiceInfoResource = new WebServiceInfoResource(new WebServiceInfo());
        MeasurementsResource measurementsResource = new MeasurementsResource(dbManager);
        environment.getApplicationContext().setContextPath("/api");
        environment.lifecycle().manage(dbManager);
        environment.jersey().register(webServiceInfoResource);
        environment.jersey().register(measurementsResource);
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<DomoticsUser>()
                    .setAuthenticator(new DomoticsUserAuthenticator())
                    .setAuthorizer(new DomoticsUserAuthorizer())
                    .setRealm("SUPER SECRET STUFF")
                    .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //If you want to use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(DomoticsUser.class));        
        environment.healthChecks().register("database", new DatabaseHealthCheck(dbManager));
    }

    public WebServiceConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(WebServiceConfig configuration) {
        this.configuration = configuration;
    }
}
