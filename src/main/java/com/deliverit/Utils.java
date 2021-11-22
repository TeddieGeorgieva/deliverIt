package com.deliverit;

import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;

public abstract class Utils {

    public static void throwIfNotEmployee(User user, String errorMessage) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(errorMessage);
        }
    }
}
