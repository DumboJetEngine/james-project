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

package org.apache.james.mailbox.store.mail.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;

import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.exception.MailboxNotFoundException;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.model.MailboxPath;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.model.impl.SimpleMailbox;
import org.junit.After;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.xenei.junit.contract.Contract;
import org.xenei.junit.contract.ContractTest;
import org.xenei.junit.contract.IProducer;

/**
 * Generic purpose tests for your implementation MailboxMapper.
 * 
 * You then just need to instantiate your mailbox mapper and an IdGenerator.
 */
@Contract(MapperProvider.class)
public class MailboxMapperTest<T extends MapperProvider> {
    
    private final static char DELIMITER = ':';
    private final static char WILDCARD = '%';
    private final static long UID_VALIDITY = 42;

    private IProducer<T> producer;
    private MailboxMapper mailboxMapper;

    private MailboxPath benwaInboxPath;
    private SimpleMailbox benwaInboxMailbox;
    private MailboxPath benwaWorkPath;
    private SimpleMailbox benwaWorkMailbox;
    private MailboxPath benwaWorkTodoPath;
    private SimpleMailbox benwaWorkTodoMailbox;
    private MailboxPath benwaPersoPath;
    private SimpleMailbox benwaPersoMailbox;
    private MailboxPath benwaWorkDonePath;
    private SimpleMailbox benwaWorkDoneMailbox;
    private MailboxPath bobInboxPath;
    private SimpleMailbox bobyMailbox;
    private MailboxPath bobyMailboxPath;
    private SimpleMailbox bobInboxMailbox;
    private MailboxPath esnDevGroupInboxPath;
    private SimpleMailbox esnDevGroupInboxMailbox;
    private MailboxPath esnDevGroupHublinPath;
    private SimpleMailbox esnDevGroupHublinMailbox;
    private MailboxPath esnDevGroupJamesPath;
    private SimpleMailbox esnDevGroupJamesMailbox;
    private MailboxPath obmTeamGroupInboxPath;
    private SimpleMailbox obmTeamGroupInboxMailbox;
    private MailboxPath obmTeamGroupOPushPath;
    private SimpleMailbox obmTeamGroupOPushMailbox;
    private MailboxPath obmTeamGroupRoundCubePath;
    private SimpleMailbox obmTeamGroupRoundCubeMailbox;
    private MailboxPath bobDifferentNamespacePath;
    private SimpleMailbox bobDifferentNamespaceMailbox;

    @Rule
    public ExpectedException expected = ExpectedException.none();
    private T mapperProvider;

    @Contract.Inject
    public final void setProducer(IProducer<T> producer) throws MailboxException {
        this.producer = producer;
        this.mapperProvider = producer.newInstance();
        Assume.assumeFalse(mapperProvider.getNotImplemented().contains(MapperProvider.Capabilities.MAILBOX));

        this.mapperProvider.ensureMapperPrepared();
        this.mailboxMapper = mapperProvider.createMailboxMapper();

        benwaInboxPath = new MailboxPath("#private", "benwa", "INBOX");
        benwaWorkPath = new MailboxPath("#private", "benwa", "INBOX"+DELIMITER+"work");
        benwaWorkTodoPath = new MailboxPath("#private", "benwa", "INBOX"+DELIMITER+"work"+DELIMITER+"todo");
        benwaPersoPath = new MailboxPath("#private", "benwa", "INBOX"+DELIMITER+"perso");
        benwaWorkDonePath = new MailboxPath("#private", "benwa", "INBOX"+DELIMITER+"work"+DELIMITER+"done");
        bobInboxPath = new MailboxPath("#private", "bob", "INBOX");
        bobyMailboxPath = new MailboxPath("#private", "boby", "INBOX.that.is.a.trick");
        bobDifferentNamespacePath = new MailboxPath("#private_bob", "bob", "INBOX.bob");
        esnDevGroupInboxPath = new MailboxPath("#community_ESN_DEV", null, "INBOX");
        esnDevGroupHublinPath = new MailboxPath("#community_ESN_DEV", null, "INBOX"+DELIMITER+"hublin");
        esnDevGroupJamesPath = new MailboxPath("#community_ESN_DEV", null, "INBOX"+DELIMITER+"james");
        obmTeamGroupInboxPath = new MailboxPath("#community_OBM_Core_Team", null, "INBOX");
        obmTeamGroupOPushPath = new MailboxPath("#community_OBM_Core_Team", null, "INBOX"+DELIMITER+"OPush");
        obmTeamGroupRoundCubePath = new MailboxPath("#community_OBM_Core_Team", null, "INBOX"+DELIMITER+"roundCube");

        benwaInboxMailbox = createMailbox(benwaInboxPath);
        benwaWorkMailbox = createMailbox(benwaWorkPath);
        benwaWorkTodoMailbox = createMailbox(benwaWorkTodoPath);
        benwaPersoMailbox = createMailbox(benwaPersoPath);
        benwaWorkDoneMailbox = createMailbox(benwaWorkDonePath);
        bobInboxMailbox = createMailbox(bobInboxPath);
        esnDevGroupInboxMailbox = createMailbox(esnDevGroupInboxPath);
        esnDevGroupHublinMailbox = createMailbox(esnDevGroupHublinPath);
        esnDevGroupJamesMailbox = createMailbox(esnDevGroupJamesPath);
        obmTeamGroupInboxMailbox = createMailbox(obmTeamGroupInboxPath);
        obmTeamGroupOPushMailbox = createMailbox(obmTeamGroupOPushPath);
        obmTeamGroupRoundCubeMailbox = createMailbox(obmTeamGroupRoundCubePath);
        bobyMailbox = createMailbox(bobyMailboxPath);
        bobDifferentNamespaceMailbox = createMailbox(bobDifferentNamespacePath);
    }

    @After
    public void tearDown() throws MailboxException {
        producer.cleanUp();
    }

    @ContractTest
    public void findMailboxByPathWhenAbsentShouldFail() throws MailboxException {
        expected.expect(MailboxNotFoundException.class);
        mailboxMapper.findMailboxByPath(new MailboxPath("#private", "benwa", "INBOX"));
    }

    @ContractTest
    public void saveShouldPersistTheMailbox() throws MailboxException{
        mailboxMapper.save(benwaInboxMailbox);
        MailboxAssert.assertThat(mailboxMapper.findMailboxByPath(benwaInboxPath)).isEqualTo(benwaInboxMailbox);
    }

    @ContractTest
    public void saveWithNullUserShouldPersistTheMailbox() throws MailboxException{
        mailboxMapper.save(esnDevGroupInboxMailbox);
        MailboxAssert.assertThat(mailboxMapper.findMailboxByPath(esnDevGroupInboxPath)).isEqualTo(esnDevGroupInboxMailbox);
    }

    @ContractTest
    public void listShouldRetrieveAllMailbox() throws MailboxException {
        saveAll();
        List<Mailbox> mailboxes = mailboxMapper.list();
        assertThat(mailboxes).contains(benwaInboxMailbox, benwaWorkMailbox, benwaWorkTodoMailbox, benwaPersoMailbox, benwaWorkDoneMailbox, bobInboxMailbox, esnDevGroupInboxMailbox, esnDevGroupHublinMailbox,
            esnDevGroupJamesMailbox, obmTeamGroupInboxMailbox, obmTeamGroupOPushMailbox, obmTeamGroupRoundCubeMailbox);
    }

    @ContractTest
    public void hasChildrenShouldReturnFalseWhenNoChildrenExists() throws MailboxException {
        saveAll();
        assertThat(mailboxMapper.hasChildren(benwaWorkTodoMailbox, DELIMITER)).isFalse();
    }

    @ContractTest
    public void hasChildrenShouldReturnTrueWhenChildrenExists() throws MailboxException {
        saveAll();
        assertThat(mailboxMapper.hasChildren(benwaInboxMailbox, DELIMITER)).isTrue();
    }

    @ContractTest
    public void hasChildrenWithNullUserShouldReturnFalseWhenNoChildrenExists() throws MailboxException {
        saveAll();
        assertThat(mailboxMapper.hasChildren(esnDevGroupHublinMailbox, DELIMITER)).isFalse();
    }

    @ContractTest
    public void hasChildrenWithNullUserShouldReturnTrueWhenChildrenExists() throws MailboxException {
        saveAll();
        assertThat(mailboxMapper.hasChildren(esnDevGroupInboxMailbox, DELIMITER)).isTrue();
    }

    @ContractTest
    public void hasChildrenShouldNotBeAcrossUsersAndNamespace() throws MailboxException {
        saveAll();
        assertThat(mailboxMapper.hasChildren(bobInboxMailbox, '.')).isFalse();
    }

    @ContractTest
    public void findMailboxWithPathLikeShouldBeLimitedToUserAndNamespace() throws MailboxException {
        saveAll();
        MailboxPath mailboxPathQuery = new MailboxPath(bobInboxMailbox.getNamespace(), bobInboxMailbox.getUser(), "IN" + WILDCARD);
        assertThat(mailboxMapper.findMailboxWithPathLike(mailboxPathQuery)).containsOnly(bobInboxMailbox);
    }

    @ContractTest
    public void deleteShouldEraseTheGivenMailbox() throws MailboxException {
        expected.expect(MailboxNotFoundException.class);
        try {
            saveAll();
            mailboxMapper.delete(benwaInboxMailbox);
        } catch(MailboxException exception) {
            fail("Error was not thrown by the appropriate method", exception);
        }
        mailboxMapper.findMailboxByPath(benwaInboxPath);
    }

    @ContractTest
    public void deleteWithNullUserShouldEraseTheGivenMailbox() throws MailboxException {
        expected.expect(MailboxNotFoundException.class);
        try {
            saveAll();
            mailboxMapper.delete(esnDevGroupJamesMailbox);
        } catch(MailboxException exception) {
            fail("Error was not thrown by the appropriate method", exception);
        }
        mailboxMapper.findMailboxByPath(esnDevGroupJamesPath);
    }

    @ContractTest
    public void findMailboxWithPathLikeWithChildRegexShouldRetrieveChildren() throws MailboxException {
        saveAll();
        MailboxPath regexPath = new MailboxPath(benwaWorkPath.getNamespace(), benwaWorkPath.getUser(), benwaWorkPath.getName() + WILDCARD);
        assertThat(mailboxMapper.findMailboxWithPathLike(regexPath)).containsOnly(benwaWorkMailbox, benwaWorkTodoMailbox, benwaWorkDoneMailbox);
    }

    @ContractTest
    public void findMailboxWithPathLikeWithNullUserWithChildRegexShouldRetrieveChildren() throws MailboxException {
        saveAll();
        MailboxPath regexPath = new MailboxPath(obmTeamGroupInboxPath.getNamespace(), obmTeamGroupInboxPath.getUser(), obmTeamGroupInboxPath.getName() + WILDCARD);
        assertThat(mailboxMapper.findMailboxWithPathLike(regexPath)).contains(obmTeamGroupInboxMailbox, obmTeamGroupOPushMailbox, obmTeamGroupRoundCubeMailbox);
    }

    @ContractTest
    public void findMailboxWithPathLikeWithRegexShouldRetrieveCorrespondingMailbox() throws MailboxException {
        saveAll();
        MailboxPath regexPath = new MailboxPath(benwaInboxPath.getNamespace(), benwaInboxPath.getUser(), WILDCARD + "X");
        assertThat(mailboxMapper.findMailboxWithPathLike(regexPath)).containsOnly(benwaInboxMailbox);
    }

    @ContractTest
    public void findMailboxWithPathLikeWithNullUserWithRegexShouldRetrieveCorrespondingMailbox() throws MailboxException {
        saveAll();
        MailboxPath regexPath = new MailboxPath(esnDevGroupInboxPath.getNamespace(), esnDevGroupInboxPath.getUser(), WILDCARD + "X");
        assertThat(mailboxMapper.findMailboxWithPathLike(regexPath)).contains(esnDevGroupInboxMailbox);
    }

    @ContractTest
    public void findMailboxWithPathLikeShouldEscapeMailboxName() throws MailboxException {
        saveAll();
        MailboxPath regexPath = new MailboxPath(benwaInboxPath.getNamespace(), benwaInboxPath.getUser(), "INB?X");
        assertThat(mailboxMapper.findMailboxWithPathLike(regexPath)).isEmpty();
    }

    @ContractTest
    public void findMailboxByIdShouldReturnExistingMailbox() throws MailboxException {
        saveAll();
        Mailbox actual = mailboxMapper.findMailboxById(benwaInboxMailbox.getMailboxId());
        assertThat(actual).isEqualTo(benwaInboxMailbox);
    }
    
    @ContractTest
    public void findMailboxByIdShouldFailWhenAbsent() throws MailboxException {
        expected.expect(MailboxNotFoundException.class);
        saveAll();
        MailboxId removed = benwaInboxMailbox.getMailboxId();
        mailboxMapper.delete(benwaInboxMailbox);
        mailboxMapper.findMailboxById(removed);
    }

    private void saveAll() throws MailboxException{
        mailboxMapper.save(benwaInboxMailbox);
        mailboxMapper.save(benwaWorkMailbox);
        mailboxMapper.save(benwaWorkTodoMailbox);
        mailboxMapper.save(benwaPersoMailbox);
        mailboxMapper.save(benwaWorkDoneMailbox);
        mailboxMapper.save(esnDevGroupInboxMailbox);
        mailboxMapper.save(esnDevGroupHublinMailbox);
        mailboxMapper.save(esnDevGroupJamesMailbox);
        mailboxMapper.save(obmTeamGroupInboxMailbox);
        mailboxMapper.save(obmTeamGroupOPushMailbox);
        mailboxMapper.save(obmTeamGroupRoundCubeMailbox);
        mailboxMapper.save(bobyMailbox);
        mailboxMapper.save(bobDifferentNamespaceMailbox);
        mailboxMapper.save(bobInboxMailbox);
    }

    private SimpleMailbox createMailbox(MailboxPath mailboxPath) {
        SimpleMailbox mailbox = new SimpleMailbox(mailboxPath, UID_VALIDITY);
        mailbox.setMailboxId(mapperProvider.generateId());
        return mailbox;
    }

}
