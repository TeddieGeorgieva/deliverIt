package com.deliverit;

import com.deliverit.exceptions.UnauthorizedOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.deliverit.Helpers.createMockUser;
import static com.deliverit.Utils.throwIfNotEmployee;

public class UtilsTests {

    @Test
    public void throwIfNotEmployee_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> throwIfNotEmployee(user, "errorMessage"));
    }
}
