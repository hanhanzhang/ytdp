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

import com.ytdp.data.entity.org.User;
import com.ytdp.data.platform.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RequestAccessDecisionManager implements AccessDecisionManager {

    private static final String ATTRIBUTE_PERMIT_ALL = "permitAll";
    private static final String ATTRIBUTE_AUTHENTICATED = "authenticated";

    @Autowired
    private RoleResourceInformation roleResourceInformation;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // URL访问权限有两种情况:
        // 1. permitAll
        // 2. authenticated
        Preconditions.checkArgument(configAttributes.size() == 1);
        ConfigAttribute configAttribute = configAttributes.iterator().next();
        String attribute = configAttribute.getAttribute();
        if (attribute.equals(ATTRIBUTE_PERMIT_ALL)) {
            return;
        }

        if (!attribute.equals(ATTRIBUTE_AUTHENTICATED)) {
            throw new AccessDeniedException("unsupported authenticate attribute: " + attribute);
        }

        User user = (User) authentication.getDetails();
        // 查询用户具有权限角色是否可访问该资源
        FilterInvocation invocation = (FilterInvocation) object;
        String url = invocation.getRequestUrl();
        if (roleResourceInformation.hasPermission(user.getRoles(), url)) {
            return;
        }
        throw new AccessDeniedException("failed authentication");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
