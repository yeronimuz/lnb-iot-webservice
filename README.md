# lnb-iot-webservice
This project is the REST service for the LNB domotics eco system.

# end-points
http://<hostname>:<port>/api/swagger-static/index.html
http://<hostname>:<port>/api/swagger.json

# known features
* Get measurements by sensor
* Get system info

# Features to be expected:
* Adding homes, rooms, sensors, users and permissions
* Adding an Angular front-end
* Intelligence about switching statistics. It would be nice if we could determine what device is consuming how much power and when. It is yet to be determined how the switching knowledge is to be stored.
* Cleanup algorithm. We don't want to store all measurements indefinitely. It would be nice if we could generate statistics, store them and remove the old measurements.
