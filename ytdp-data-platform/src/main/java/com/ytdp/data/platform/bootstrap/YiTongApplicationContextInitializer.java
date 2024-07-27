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

package com.ytdp.data.platform.bootstrap;

import com.ytdp.data.platform.annotations.PermissionResource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class YiTongApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
        for (Map.Entry<String, Object> beanEntry : beans.entrySet()) {
            Object bean = beanEntry.getValue();
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                PermissionResource resource = method.getAnnotation(PermissionResource.class);
            }

        }
    }

    private static String classDefinedUrlPath(Object bean) {
        RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);
        GetMapping getMapping = bean.getClass().getAnnotation(GetMapping.class);
        return "";
    }

    @Getter
    @Setter
    @Builder
    private static class Resource {

        private int id;

        private String name;

        private String path;

        private Set<Resource> children;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Resource resource = (Resource) o;
            return Objects.equals(path, resource.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }
}
