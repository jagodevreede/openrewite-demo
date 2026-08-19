package com.wreckage;

public final class RecoveryService {
    public void recover(Wreck wreck) {
        if (!wreck.recoverable()) {
            throw new IllegalArgumentException("Wreck " + wreck.id() + " (" + wreck.name() + ") is not recoverable");
        }
    }
}
