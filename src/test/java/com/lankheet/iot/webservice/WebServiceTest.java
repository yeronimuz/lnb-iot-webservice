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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.BeforeClass;
import org.junit.Test;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.datatypes.entities.MeasurementType;
import com.lankheet.iot.datatypes.entities.Sensor;
import com.lankheet.iot.datatypes.entities.SensorType;
import com.lankheet.iot.webservice.config.DatabaseConfig;

/**
 * Test for @link {@link WebService} A database is required An mqtt server is required
 */
public class WebServiceTest {
    private static final String PERSISTENCE_UNIT = "meas-pu";

    private static WebService webService = new WebService();
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void doSetup() throws Exception {
        // Start service
        webService.run("server", "src/test/resources/application.yml");

        DatabaseConfig dbConfig = webService.getConfiguration().getDatabaseConfig();
        // Overwrite production persistence unit settings with test values
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", dbConfig.getDriver());
        properties.put("javax.persistence.jdbc.url", dbConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", dbConfig.getUserName());
        properties.put("javax.persistence.jdbc.password", dbConfig.getPassword());
        properties.put("hibernate.hbm2ddl.auto", "create");

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        em = emf.createEntityManager();
        // Prepare database
        Sensor sensor1 = new Sensor(SensorType.POWER_METER, "AA:BB:CC:DD", "power-meter", "meterkast");
        Measurement measurement = new Measurement(sensor1, new Date(), MeasurementType.CONSUMED_POWER_T1, 0.1);
        em.getTransaction().begin();
        em.persist(sensor1);
        em.persist(measurement);
        em.getTransaction().commit();
    }

    // @Test
    public void testEndToEndTest() throws Exception {
        Measurement measurement =
                new Measurement(new Sensor(SensorType.POWER_METER, "AA:BB:CC:DD", "power-meter", "meterkast"),
                        new Date(), MeasurementType.ACTUAL_CONSUMED_POWER, 1.1);
        Thread.sleep(2000);
        Query query = em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + measurement.getSensor()
                + "AND e.type = " + measurement.getMeasurementType() + " AND e.value = " + measurement.getValue());
        List<Measurement> resultList = query.getResultList();
        int numberOfDuplicates = resultList.size();
        assertThat(numberOfDuplicates, is(1));
    }
}
