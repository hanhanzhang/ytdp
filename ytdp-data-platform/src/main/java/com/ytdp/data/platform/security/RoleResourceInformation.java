/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ytdp.data.platform.security;

import com.ytdp.data.dao.org.RoleMapper;
import com.ytdp.data.entity.org.Resource;
import com.ytdp.data.entity.org.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
public class RoleResourceInformation {

    @Autowired
    private RoleMapper roleMapper;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    // key: roleId, value: resources
    private volatile Map<Integer, Set<String>> roleResources;

    @PostConstruct
    public void initialize() {
        log.info("load resource information for role");
        try {
            readWriteLock.writeLock().lock();
            this.roleResources = loadRoleResourceInformation();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void updateRoleResourceInformation() {
        this.initialize();
    }

    private Map<Integer, Set<String>> loadRoleResourceInformation() {
        List<Role> roles = roleMapper.selectList(null);
        return roles
                .stream()
                .collect(
                        Collectors.toMap(
                                Role::getRoleId,
                                role -> role
                                        .getResources()
                                        .stream()
                                        .map(Resource::getResourcePath)
                                        .collect(Collectors.toSet())
                        )
                );
    }

    public boolean hasPermission(Collection<Role> roles, String url) {
        try {
            readWriteLock.readLock().lock();
            if (roles == null || roles.isEmpty()) {
                return false;
            }
            for (Role role : roles) {
                Set<String> resources = roleResources.get(role.getRoleId());
                if (resources == null || resources.isEmpty()) {
                    continue;
                }
                if (resources.contains(url)) {
                    return true;
                }
            }
            return false;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

}
