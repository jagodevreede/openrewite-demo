package com.wreckage.rewrite;

import org.openrewrite.Recipe;

public class RenameServiceVariable extends Recipe {
    private static final String SERVICE_TYPE = "com.wreckage.NotificationService";
    private static final String OLD_NAME = "notificationCervice";
    private static final String NEW_NAME = "notificationService";

    @Override
    public String getDisplayName() {
        return "Rename `notificationCervice` NotificationService variables";
    }

    @Override
    public String getDescription() {
        return "Renames variables named `notificationCervice` when their type is NotificationService.";
    }


}
