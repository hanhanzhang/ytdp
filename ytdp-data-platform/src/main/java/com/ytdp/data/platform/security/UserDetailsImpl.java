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

import com.ytdp.data.entity.org.Department;
import com.ytdp.data.entity.org.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetailsWithOrg {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    // 用户账号是否过期
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // 用户账号是否被锁
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 用户凭证是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 用户是否可用
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Department userDepartmentDetails() {
        return user.getDepartment();
    }
}
