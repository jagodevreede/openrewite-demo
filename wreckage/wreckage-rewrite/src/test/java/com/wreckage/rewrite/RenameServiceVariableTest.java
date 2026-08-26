package com.wreckage.rewrite;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

public class RenameServiceVariableTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new RenameServiceVariable());
    }

    @Test
    void renamesNotificationServiceField() {
        rewriteRun(java("""
                package com.example.survivor;
                
                import com.wreckage.NotificationService;

                class IncidentService {
                    private final NotificationService notificationCervice = new NotificationService();

                    void notifySurvivors() {
                        notificationCervice.toString();
                    }
                }
                """, """
                package com.example.survivor;
                
                import com.wreckage.NotificationService;

                class IncidentService {
                    private final NotificationService notificationService = new NotificationService();

                    void notifySurvivors() {
                        notificationService.toString();
                    }
                }
                """));
    }

    @Test
    void renamesNotificationServiceInOtherServiceField() {
        rewriteRun(java("""
                package com.example.survivor;
                
                import com.wreckage.NotificationService;

                class OtherService {
                    private final NotificationService notificationCervice = new NotificationService();

                    void notifySurvivors() {
                        notificationCervice.toString();
                    }
                }
                """, """
                package com.example.survivor;
                
                import com.wreckage.NotificationService;

                class OtherService {
                    private final NotificationService notificationService = new NotificationService();

                    void notifySurvivors() {
                        notificationService.toString();
                    }
                }
                """));
    }

    @Test
    void doesNotRenameFieldWithDifferentClass() {
        rewriteRun(java("""
                package com.example.survivor;

                class OtherService {
                    private final String notificationCervice = "service";
                }
                """));
    }
}
