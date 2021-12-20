package com.appsdeveloperblog.app.ws.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

// make it an integration test
@ExtendWith(SpringExtension.class) // it brings up spring context that is why it is an integration test
@SpringBootTest
class UtilsTest {

    Utils utils;

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testGenerateUserId() {
        // we don't really need an integration test for this case
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId2));
    }
}