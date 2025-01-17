/*
 * Copyright 2015 - 2019 TU Dortmund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.learnlib.alex.auth.dao;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.auth.entities.UserRole;
import de.learnlib.alex.auth.repositories.UserRepository;
import de.learnlib.alex.common.exceptions.NotFoundException;
import de.learnlib.alex.common.utils.IdsList;
import de.learnlib.alex.data.dao.FileDAOImpl;
import de.learnlib.alex.data.dao.ProjectDAO;
import de.learnlib.alex.data.repositories.ProjectRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserDAOImplTest {

    private static final int TEST_USER_COUNT = 3;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileDAOImpl fileDAO;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private ProjectRepository projectRepository;

    private UserDAO userDAO;

    @Before
    public void setUp() throws NotFoundException {
        userDAO = new UserDAOImpl(userRepository, fileDAO, projectDAO, projectRepository);
    }

    @Test
    public void shouldCreateAValidUser() throws NotFoundException {
        User user = createUser();

        userDAO.create(user);

        Mockito.verify(userRepository).save(user);
    }

    @Test(expected = ValidationException.class)
    public void shouldHandleTransactionSystemExceptionsOnUserCreationGracefully() {
        User user = createUser();
        ConstraintViolationException constraintViolationException;
        constraintViolationException = new ConstraintViolationException("User is not valid!", new HashSet<>());
        RollbackException rollbackException = new RollbackException("RollbackException", constraintViolationException);
        TransactionSystemException transactionSystemException;
        transactionSystemException = new TransactionSystemException("Spring TransactionSystemException",
                                                                    rollbackException);
        BDDMockito.given(userRepository.save(user)).willThrow(transactionSystemException);

        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void shouldHandleConstraintViolationExceptionOnUserCreationGracefully() {
        User user = createUser();
        BDDMockito.given(userRepository.save(user)).willThrow(ConstraintViolationException.class);

        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void shouldHandleDataIntegrityViolationExceptionOnUserCreationGracefully() {
        User user = createUser();
        BDDMockito.given(userRepository.save(user)).willThrow(DataIntegrityViolationException.class);

        userDAO.create(user);
    }

    @Test
    public void shouldGetAllUsers() {
        List<User> users = createUsersList();
        BDDMockito.given(userRepository.findAll()).willReturn(users);

        List<User> allUsers = userDAO.getAll();

        assertThat(allUsers.size(), is(equalTo(users.size())));
        for (User u : allUsers) {
            Assert.assertTrue(users.contains(u));
        }
    }

    @Test
    public void shouldOnlyGetAllAdmins() {
        List<User> users = createUsersList();
        BDDMockito.given(userRepository.findByRole(UserRole.ADMIN)).willReturn(users);

        List<User> allAdmins = userDAO.getAllByRole(UserRole.ADMIN);

        assertThat(allAdmins.size(), is(equalTo(users.size())));
        for (User u : allAdmins) {
            Assert.assertTrue(users.contains(u));
        }
    }

    @Test
    public void shouldGetAllRegisteredUsers() {
        List<User> users = createUsersList();
        BDDMockito.given(userRepository.findByRole(UserRole.REGISTERED)).willReturn(users);

        List<User> allRegistered = userDAO.getAllByRole(UserRole.REGISTERED);

        assertThat(allRegistered.size(), is(equalTo(users.size())));
        for (User u : allRegistered) {
            Assert.assertTrue(users.contains(u));
        }
    }

    @Test
    public void shouldGetByID() throws NotFoundException {
        User user = createUser();
        BDDMockito.given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        User userFromDB = userDAO.getById(user.getId());

        Assert.assertEquals(user, userFromDB);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowAnExceptionIfTheUserCanNotBeFoundByID() throws NotFoundException {
        userDAO.getById((long) TEST_USER_COUNT);
    }

    @Test
    public void shouldGetByEmail() throws NotFoundException {
        User user = createUser();
        BDDMockito.given(userRepository.findOneByEmail(user.getEmail())).willReturn(user);

        User userFromDB = userDAO.getByEmail(user.getEmail());

        Assert.assertEquals(user, userFromDB);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowAnExceptionIfTheUserCanNotBeFoundByEmail() throws NotFoundException {
        User user = createUser();
        BDDMockito.given(userRepository.findOneByEmail(user.getEmail())).willReturn(null);

        userDAO.getByEmail(user.getEmail());
    }

    @Test
    public void shouldUpdateAUser() throws NotFoundException {
        User user = createUser();

        userDAO.update(user);

        Mockito.verify(userRepository).save(user);
    }

    @Test(expected = ValidationException.class)
    public void shouldHandleTransactionSystemExceptionsOnUserUpdateGracefully() {
        User user = createUser();
        ConstraintViolationException constraintViolationException;
        constraintViolationException = new ConstraintViolationException("User is not valid!", new HashSet<>());
        RollbackException rollbackException = new RollbackException("RollbackException", constraintViolationException);
        TransactionSystemException transactionSystemException;
        transactionSystemException = new TransactionSystemException("Spring TransactionSystemException",
                                                                    rollbackException);
        BDDMockito.given(userRepository.save(user)).willThrow(transactionSystemException);

        userDAO.update(user);
    }

    @Test(expected = ValidationException.class)
    public void shouldHandleConstraintViolationExceptionOnUserUpdateGracefully() {
        User user = createUser();
        BDDMockito.given(userRepository.save(user)).willThrow(ConstraintViolationException.class);

        userDAO.update(user);
    }

    @Test(expected = ValidationException.class)
    public void shouldHandleDataIntegrityViolationExceptionOnUserUpdateGracefully() {
        User user = createUser();
        BDDMockito.given(userRepository.save(user)).willThrow(DataIntegrityViolationException.class);

        userDAO.update(user);
    }

    @Test
    public void shouldDeleteARegisteredUser() throws NotFoundException {
        User user = createUser();
        BDDMockito.given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        userDAO.delete(user.getId());

        Mockito.verify(userRepository).delete(user);
    }

    @Test
    public void shouldDeleteAnAdminIfThereAreMoreThanOne() throws NotFoundException {
        List<User> admins = createUsersList();
        admins.forEach(u -> u.setRole(UserRole.ADMIN));
        User adminToDelete = admins.get(0);
        BDDMockito.given(userRepository.findById(adminToDelete.getId())).willReturn(Optional.of(adminToDelete));
        BDDMockito.given(userRepository.findByRole(UserRole.ADMIN)).willReturn(admins);

        userDAO.delete(adminToDelete.getId());

        Mockito.verify(userRepository).delete(adminToDelete);
    }

    @Test(expected = NotFoundException.class)
    public void shouldNotDeleteTheLastAdmin() throws NotFoundException {
        User admin = createAdmin();
        BDDMockito.given(userRepository.findById(admin.getId())).willReturn(Optional.of(admin));
        BDDMockito.given(userRepository.findByRole(UserRole.ADMIN)).willReturn(Collections.singletonList(admin));

        userDAO.delete(admin.getId());
    }

    @Test
    public void shouldDeleteMultipleUsers() throws NotFoundException {
        User user1 = new User();
        user1.setId(42L);
        user1.setEmail("user1@mail.de");
        user1.setEncryptedPassword("test");

        User user2 = new User();
        user2.setId(21L);
        user2.setEmail("user2@mail.de");
        user2.setEncryptedPassword("test");

        BDDMockito.given(userRepository.findAllByIdIn(Arrays.asList(user1.getId(), user2.getId())))
                .willReturn(Arrays.asList(user1, user2));

        IdsList ids = new IdsList(String.valueOf(user1.getId()) + "," + String.valueOf(user2.getId()));
        userDAO.delete(ids);

        Assert.assertEquals(userDAO.getAllByRole(UserRole.REGISTERED).size(), 0);
    }

    @Test(expected = NotFoundException.class)
    public void shouldNotDeleteMultipleUsersOnNotFound() throws NotFoundException {
        User user1 = new User();
        user1.setId(42L);
        user1.setEmail("user1@mail.de");
        user1.setEncryptedPassword("test");

        userDAO.create(user1);

        IdsList ids = new IdsList(String.valueOf(user1.getId()) + ",123");
        userDAO.delete(ids);
    }

    @Test(expected = NotFoundException.class)
    public void shouldFailToDeleteAUserOnInvalidId() throws NotFoundException {
        userDAO.delete(-1L);
    }

    @Test(expected = NotFoundException.class)
    public void shouldNotDeleteOnlyExistingAdmin() throws NotFoundException {
        User admin = createAdmin();
        userDAO.delete(admin.getId());
    }

    private User createUser() {
        User user = new User();
        user.setEmail("user@text.example");
        user.setEncryptedPassword("alex");
        return user;
    }

    private User createAdmin() {
        User admin = createUser();
        admin.setRole(UserRole.ADMIN);
        return admin;
    }

    private List<User> createUsersList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i  < TEST_USER_COUNT; i++) {
            User u = new User();
            u.setEmail("user-" + String.valueOf(i) + "@mail.de");
            u.setEncryptedPassword("test");
            users.add(u);
        }
        return users;
    }

}
