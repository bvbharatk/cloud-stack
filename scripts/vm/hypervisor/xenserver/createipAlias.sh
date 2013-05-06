#!/usr/bin/env bash
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.


# $Id: call_firewall.sh 9132 2010-06-04 20:17:43Z manuel $ $HeadURL: svn://svn.lab.vmops.com/repos/branches/2.0.0/java/scripts/vm/hypervisor/xenserver/patch/call_firewall.sh $
# firewall.sh -- allow some ports / protocols to vm instances
usage() {
  printf " %s  routerip  <alias_count:ip:netmask;alias_count2:ip2:netmask2;....> \n" $(basename $0) >&2
}

set -x 
cert="/root/.ssh/id_rsa.cloud"
ssh -p 3922 -q -o StrictHostKeyChecking=no -i $cert root@$1 "/root/createIpAlias.sh $2"  


