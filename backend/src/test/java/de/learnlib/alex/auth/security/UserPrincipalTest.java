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

package de.learnlib.alex.auth.security;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.auth.entities.UserRole;
import org.junit.Assert;
import org.junit.Test;

public class UserPrincipalTest {

    @Test
    public void shouldGetTheEmailOfTheUserWhenAskedForTheName() {
        final User user = new User();
        user.setEmail("user@alex.com");
        user.setId(1L);
        user.setEncryptedPassword("test");
        user.setRole(UserRole.REGISTERED);

        final UserPrincipal principal = new UserPrincipal(user);
        Assert.assertEquals("user@alex.com", principal.getName());
    }
}