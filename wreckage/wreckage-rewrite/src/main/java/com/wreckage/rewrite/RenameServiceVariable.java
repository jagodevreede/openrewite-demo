package com.wreckage.rewrite;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;

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

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new RenameServiceVariableVisitor();
    }

    private class RenameServiceVariableVisitor extends JavaIsoVisitor<ExecutionContext> {
        @Override
        public J.VariableDeclarations.NamedVariable visitVariable(J.VariableDeclarations.NamedVariable variable, ExecutionContext ctx) {
            J.Identifier id = variable.getName();
            String name = id.getSimpleName();
            if (OLD_NAME.equals(name)) {
                JavaType.Variable varType = variable.getVariableType();
                if (TypeUtils.isOfClassType(varType, SERVICE_TYPE)) {
                    JavaType.Variable updatedType = varType.withName(NEW_NAME);
                    J.Identifier newId = id.withSimpleName(NEW_NAME)
                            .withFieldType(updatedType);
                    return variable.withName(newId)
                            .withVariableType(updatedType);
                }
            }
            return super.visitVariable(variable, ctx);
        }

        @Override
        public J.Identifier visitIdentifier(J.Identifier identifier, ExecutionContext ctx) {
            String name = identifier.getSimpleName();
            if (OLD_NAME.equals(name)) {
                JavaType.Variable varType = identifier.getFieldType();
                if (TypeUtils.isOfClassType(varType, SERVICE_TYPE)) {
                    JavaType.Variable updatedType = varType.withName(NEW_NAME);
                    return identifier.withSimpleName(NEW_NAME)
                            .withFieldType(updatedType);
                }
            }
            return super.visitIdentifier(identifier, ctx);
        }
    }
}
