package com.portal.job_portal_service.util;

import com.portal.job_portal_service.model.User;

import java.time.ZonedDateTime;


public final class MockDataUtil {

    /**
     * Create test data for the User entity.
     * @return The user object.
     */
    public static User getUserData() {

        User user = new User();
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setUserEmail("test@dev.com");
        user.setUserPassword("myPassword");
        user.setCreatedTimestamp(ZonedDateTime.now());
        user.setUpdatedTimestamp(ZonedDateTime.now());

        return user;
    }
}
