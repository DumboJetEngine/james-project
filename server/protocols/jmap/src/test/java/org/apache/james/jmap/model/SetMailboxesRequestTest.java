/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.james.jmap.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang.NotImplementedException;
import org.apache.james.jmap.model.mailbox.MailboxRequest;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SetMailboxesRequestTest {

    @Test(expected=NotImplementedException.class)
    public void builderShouldThrowWhenAccountId() {
        SetMailboxesRequest.builder().accountId("1");
    }

    @Test(expected=NotImplementedException.class)
    public void builderShouldThrowWhenIfInState() {
        SetMailboxesRequest.builder().ifInState("1");
    }

    @Test(expected=NotImplementedException.class)
    public void builderShouldThrowWhenUpdate() {
        SetMailboxesRequest.builder().update(ImmutableMap.of());
    }
    
    @Test(expected=NotImplementedException.class)
    public void builderShouldThrowWhenDestroy() {
        SetMailboxesRequest.builder().destroy(ImmutableList.of());
    }
    
    @Test
    public void builderShouldWork() {
        MailboxCreationId creationId = MailboxCreationId.of("creationId");
        MailboxRequest mailboxRequest = MailboxRequest.builder()
            .name("mailboxRequest")
            .build();
        SetMailboxesRequest expected = new SetMailboxesRequest(ImmutableMap.of(creationId, mailboxRequest));
        
        SetMailboxesRequest actual = SetMailboxesRequest.builder()
            .create(creationId, mailboxRequest)
            .build();
        
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}