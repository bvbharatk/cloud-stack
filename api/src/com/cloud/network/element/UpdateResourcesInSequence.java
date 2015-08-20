package com.cloud.network.element;

import com.cloud.network.Network;

/**
 * Created by bharat on 11/08/15.
 */
public interface UpdateResourcesInSequence {
     public void configureResourceUpdateSequence(Network network);
     public boolean isUpdateComplete(Network network);
}
