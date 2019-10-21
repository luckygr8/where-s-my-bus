package com.jmit.wmbsender;

public interface Values {
    /**
     * @author
     * lakshay dutta
     *
     * minTime	long: minimum time interval between location updates, in milliseconds
     * minDistance	float: minimum distance between location updates, in meters
     *
     *  A new location is updated when both the conditions have been met. I.e. MIN_TIME seconds have
     *  elapsed and the distance covered was >=MIN_DIST meters.
     *
     *  Choose the values wisely as there is a connection delay between requests and requests can
     *  not be made instantaneously one after the other. We recommend not changing the values for
     *  the MIN_TIME. You can change the MIN_DIST according to necessity.
     */
    int MIN_TIME=3000; // in milliseconds -- for more speed do 2500
    float MIN_DIST=0.1f; // in meters

    /**
     * testing only
     */
    String TEST_URL = "https://us-central1-where-s-my-bus-ca926.cloudfunctions.net/fun";

}
