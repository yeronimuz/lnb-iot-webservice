package com.lankheet.iot.webservice.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class WebServiceConfig extends Configuration {

    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;
    
	@Valid
    @NotNull
    @JsonProperty
    private MqttConfig mqttConfig = new MqttConfig();

	@Valid
    @NotNull
    @JsonProperty
    private DatabaseConfig databaseConfig = new DatabaseConfig();

	public MqttConfig getMqttConfig() {
		return mqttConfig;
	}

	public void setMqttConfig(MqttConfig mqttConfig) {
		this.mqttConfig = mqttConfig;
	}

	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

    /**
     * Get swaggerBundleConfiguration.
     * @return the swaggerBundleConfiguration
     */
    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    /**
     * Set swaggerBundleConfiguration.
     * @param swaggerBundleConfiguration the swaggerBundleConfiguration to set
     */
    public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {
        this.swaggerBundleConfiguration = swaggerBundleConfiguration;
    }
}
