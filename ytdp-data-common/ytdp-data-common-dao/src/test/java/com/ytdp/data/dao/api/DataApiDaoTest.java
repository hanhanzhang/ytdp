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

package com.ytdp.data.dao.api;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.ytdp.data.entity.api.DataApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@Slf4j
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataApiDaoTest {

    @Autowired
    private DataAppMapper dataAppMapper;

    @Test
    @Rollback(value = false) // 在单测中自动回滚事务, 导致插入成功后再回滚插入动作
    public void testAddDataApp() {
        DataApp app = new DataApp();
        app.setAppName("hotel");
        app.setAppKey("hotel_data");
        app.setAppSecretKey("hotel_secret_89");
        app.setAppOwner("郑明");
        app.setAppStatus(0);
        app.setCreateTime(new Date());
        app.setUpdateTime(new Date());

        int ret = dataAppMapper.insert(app);

        log.info("insert result: {}", ret == 1 ? "success" : "failed");
    }

}
