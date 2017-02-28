package ca.concordia.encs.distributed.web;


import ca.concordia.encs.distributed.service.ClusterContextHolder;
import ca.concordia.encs.distributed.service.communication.multicast.ClusterCommunicationException;

import javax.xml.ws.Endpoint;

public class ClinicServerWebEndpoint {
    public static Integer DefaultWebPort = 5900;
    public static void main(String[] args) {
        try {
            String serverEndpoint = String.format("http://localhost:%d/clinic/", DefaultWebPort + ClusterContextHolder.CurrentLocation.index());
            Endpoint.publish(serverEndpoint, new ClinicServerWeb());
            System.out.println("Clinic server listening...");
        } catch (ClusterCommunicationException e) {
            e.printStackTrace();
        }
    }
}
