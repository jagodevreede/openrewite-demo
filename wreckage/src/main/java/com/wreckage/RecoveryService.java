package com.wreckage;

public final class RecoveryService {
    public void recover(Wreck wreck) {
        if (!wreck.isRecoverable()) {
            throw new IllegalArgumentException("Wreck " + wreck.getId() + " (" + wreck.getName() + ") is not recoverable");
        }
    }
}
