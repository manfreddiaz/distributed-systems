package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.exception.ConfigurationException;

/**
 * Created by linux on 8/9/2016.
 */
public interface Server extends Manageable {
    Integer id();
    void activate() throws ConfigurationException;
    void passivate() throws ConfigurationException;
}
