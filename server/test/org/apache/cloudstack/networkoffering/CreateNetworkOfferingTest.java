// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.cloudstack.networkoffering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cloud.configuration.ConfigurationManager;
import com.cloud.configuration.ConfigurationVO;
import com.cloud.configuration.dao.ConfigurationDao;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.network.Network;
import com.cloud.network.Network.Provider;
import com.cloud.network.Network.Service;
import com.cloud.network.Networks.TrafficType;
import com.cloud.offering.NetworkOffering.Availability;
import com.cloud.offerings.NetworkOfferingServiceMapVO;
import com.cloud.offerings.NetworkOfferingVO;
import com.cloud.offerings.dao.NetworkOfferingDao;
import com.cloud.offerings.dao.NetworkOfferingServiceMapDao;
import com.cloud.user.AccountManager;
import com.cloud.user.AccountVO;
import com.cloud.user.UserContext;
import com.cloud.user.UserVO;
import com.cloud.utils.component.ComponentContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/createNetworkOffering.xml")

public class CreateNetworkOfferingTest extends TestCase{
    
    @Inject
    ConfigurationManager configMgr;
    
    @Inject
    ConfigurationDao configDao;
    
    @Inject
    NetworkOfferingDao offDao;
    
    @Inject
    NetworkOfferingServiceMapDao mapDao;
    
    @Inject
    AccountManager accountMgr;
    
    @Before
    public void setUp() {
    	ComponentContext.initComponentsLifeCycle();
    	
        ConfigurationVO configVO = new ConfigurationVO("200", "200","200","200","200","200");
        Mockito.when(configDao.findByName(Mockito.anyString())).thenReturn(configVO);
        
        Mockito.when(offDao.persist(Mockito.any(NetworkOfferingVO.class))).thenReturn(new NetworkOfferingVO());
        Mockito.when(mapDao.persist(Mockito.any(NetworkOfferingServiceMapVO.class))).thenReturn(new NetworkOfferingServiceMapVO());
        Mockito.when(accountMgr.getSystemUser()).thenReturn(new UserVO(1));
        Mockito.when(accountMgr.getSystemAccount()).thenReturn(new AccountVO(2));

        UserContext.registerContext(accountMgr.getSystemUser().getId(), accountMgr.getSystemAccount(), null, false);
    }

    //Test Shared network offerings
    @Test
    public void createSharedNtwkOffWithVlan() {
        NetworkOfferingVO off = configMgr.createNetworkOffering("shared", "shared", TrafficType.Guest, null, true,
                Availability.Optional, 200, null, false, Network.GuestType.Shared, false,
                null, false, null, true, false);
        assertNotNull("Shared network offering with specifyVlan=true failed to create ", off);
    }
    
    @Test
    public void createSharedNtwkOffWithNoVlan() {
        try {
            NetworkOfferingVO off = configMgr.createNetworkOffering("shared", "shared", TrafficType.Guest, null, false,
                    Availability.Optional, 200, null, false, Network.GuestType.Shared, false,
                    null, false, null, true, false);
            assertNull("Shared network offering with specifyVlan=false was created", off);
        } catch (InvalidParameterValueException ex) {
        }
    }
    
    @Test
    public void createSharedNtwkOffWithSpecifyIpRanges() {
        NetworkOfferingVO off = configMgr.createNetworkOffering("shared", "shared", TrafficType.Guest, null, true,
                Availability.Optional, 200, null, false, Network.GuestType.Shared, false,
                null, false, null, true, false);
        
        assertNotNull("Shared network offering with specifyIpRanges=true failed to create ", off);
    }
    
    @Test
    public void createSharedNtwkOffWithoutSpecifyIpRanges() {
        try {
            NetworkOfferingVO off = configMgr.createNetworkOffering("shared", "shared", TrafficType.Guest, null, true,
                    Availability.Optional, 200, null, false, Network.GuestType.Shared, false,
                    null, false, null, false, false);
            assertNull("Shared network offering with specifyIpRanges=false was created", off);
        } catch (InvalidParameterValueException ex) {
        }
    }

    //Test Isolated network offerings
    @Test
    public void createIsolatedNtwkOffWithNoVlan() {
        Map<Service, Set<Provider>> serviceProviderMap = new HashMap<Network.Service, Set<Network.Provider>>();
        Set<Network.Provider> vrProvider = new HashSet<Network.Provider>();
        vrProvider.add(Provider.VirtualRouter);
        serviceProviderMap.put(Network.Service.SourceNat, vrProvider);
        NetworkOfferingVO off = configMgr.createNetworkOffering("isolated", "isolated", TrafficType.Guest, null, false,
                Availability.Optional, 200, serviceProviderMap, false, Network.GuestType.Isolated, false,
                null, false, null, false, false);
        
        assertNotNull("Isolated network offering with specifyIpRanges=false failed to create ", off);
    }
    
    @Test
    public void createIsolatedNtwkOffWithVlan() {
        Map<Service, Set<Provider>> serviceProviderMap = new HashMap<Network.Service, Set<Network.Provider>>();
        Set<Network.Provider> vrProvider = new HashSet<Network.Provider>();
        vrProvider.add(Provider.VirtualRouter);
        serviceProviderMap.put(Network.Service.SourceNat, vrProvider);
        NetworkOfferingVO off = configMgr.createNetworkOffering("isolated", "isolated", TrafficType.Guest, null, true,
                Availability.Optional, 200, serviceProviderMap, false, Network.GuestType.Isolated, false,
                null, false, null, false, false);
        assertNotNull("Isolated network offering with specifyVlan=true wasn't created", off);
       
    }
    
    @Test
    public void createIsolatedNtwkOffWithSpecifyIpRangesAndSourceNat() {
        try {
            Map<Service, Set<Provider>> serviceProviderMap = new HashMap<Network.Service, Set<Network.Provider>>();
            Set<Network.Provider> vrProvider = new HashSet<Network.Provider>();
            vrProvider.add(Provider.VirtualRouter);
            serviceProviderMap.put(Network.Service.SourceNat, vrProvider);
            NetworkOfferingVO off = configMgr.createNetworkOffering("isolated", "isolated", TrafficType.Guest, null, false,
                    Availability.Optional, 200, serviceProviderMap, false, Network.GuestType.Isolated, false,
                    null, false, null, true, false);
            assertNull("Isolated network offering with specifyIpRanges=true and source nat service enabled, was created", off);
        } catch (InvalidParameterValueException ex) {
        }
    }
    
    @Test
    public void createIsolatedNtwkOffWithSpecifyIpRangesAndNoSourceNat() {
        
        Map<Service, Set<Provider>> serviceProviderMap = new HashMap<Network.Service, Set<Network.Provider>>();
        Set<Network.Provider> vrProvider = new HashSet<Network.Provider>();
        NetworkOfferingVO off = configMgr.createNetworkOffering("isolated", "isolated", TrafficType.Guest, null, false,
                Availability.Optional, 200, serviceProviderMap, false, Network.GuestType.Isolated, false,
                null, false, null, true, false);
        assertNotNull("Isolated network offering with specifyIpRanges=true and with no sourceNatService, failed to create", off);
        
    }
}
