package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.StandardLib;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Namespace {

    @Setter
    @Getter
    private LanguageLevel languageLevel;
    @Setter
    private Namespace parent;
    private final Map<String, Namespace> children = new HashMap<>();
    private final Map<String, Struct> structs = new HashMap<>();
    private final Map<String,Task> tasks = new HashMap<>();
    @Getter
    private final Map<String, Declaration> declarations = new HashMap<>();
    @Setter
    private StandardLib lib;

    public Namespace() {

    }

    public Namespace(Namespace namespace) {
        this.languageLevel = namespace.languageLevel;
        this.parent = namespace.parent;
        this.children.putAll(namespace.children);
        this.structs.putAll(namespace.structs);
        this.tasks.putAll(namespace.tasks);
        this.declarations.putAll(namespace.declarations);
    }

    public void addChildNamespace(String name, Namespace child) throws NamespaceException {
        assertNotDefinedInNamespace(name,
                                    "Namespace collision, an element in the Namespace with the name \"" + name + "\" already exists");
        children.put(name, child);
    }

    public void addDeclaration(String name, Declaration declaration) throws NamespaceException {
        assertNotDefinedInNamespace(name,
                                    "Namespace collision, an element in the Namespace with the name \"" + name + "\" already exists");

        declarations.put(name,
                         new Declaration(declaration.getDeclType(),
                                     declaration.getName(),
                                     declaration.getExpression(),
                                     declaration.getId()));
    }

    public void addStruct(Struct struct) throws NamespaceException {
        assertNotDefinedInNamespace(struct.getName(),
                                    "Namespace collision, an element in the Namespace with the name \"" + struct.getName() + "\" already exists");
        structs.put(struct.getName(), struct);
    }

    public void addTask(Task task) throws NamespaceException {
        assertNotDefinedInNamespace(task.getName(),
                                    "Namespace collision, an element in the Namespace with the name \"" + task.getName() + "\" already exists");
        tasks.put(task.getName(),task);
    }

    public boolean isDefinedInNamespace(String name) {
        return isDefinedInParentNamesapce(name) || isDefininedInLocalNamespace(name);
    }

    private boolean isDefininedInLocalNamespace(String name) {
        return children.containsKey(name) || declarations.containsKey(name) || structs.containsKey(name) || tasks.containsKey(name);
    }

    private boolean isDefinedInParentNamesapce(String name) {
        return parent != null && parent.isDefinedInNamespace(name);
    }

    public void assertNotDefinedInNamespace(String name, String errorMessage) throws NamespaceException {
        if (!isDefinedInNamespace(name)) {
            throw new NamespaceException(errorMessage);
        }
    }

    public Declaration getDeclaration(String name) throws NamespaceException {
        if (isDefinedInParentNamesapce(name)) {
            return parent.getDeclaration(name);
        }

        if (isDefininedInLocalNamespace(name)) {
            return declarations.get(name);
        } else {
            throw new NamespaceException("Declaration with name \"" + name + "\" is not defined in the current namespace");
        }
    }

    public Struct getStruct(String name) throws NamespaceException {
        if (isDefinedInParentNamesapce(name)) {
            return parent.getStruct(name);
        }

        if (isDefininedInLocalNamespace(name)) {
            return structs.get(name);
        } else {
            throw new NamespaceException("Struct with name \"" + name + "\" is not defined in the current namespace");
        }
    }

    public Task getTask(String name) throws NamespaceException {
        if (isDefinedInParentNamesapce(name)) {
            return parent.getTask(name);
        }

        if (isDefininedInLocalNamespace(name)) {
            return tasks.get(name);
        } else {
            throw new NamespaceException("Task with name \"" + name + "\" is not defined in the current namespace");
        }
    }

    public Namespace getChildNamespace(String name) throws NamespaceException {
        if (isDefinedInParentNamesapce(name)){
            return parent.getChildNamespace(name);
        }

        if (isDefininedInLocalNamespace(name)){
            return children.get(name);
        } else {
            throw new NamespaceException("Child Namespace with name \"" + name + "\" is not defined in the current namespace");
        }
    }

    public StandardLib getLib() throws NamespaceException {
        if (lib != null) {
            return lib;
        } else if (parent != null) {
            return parent.getLib();
        } else {
            throw new NamespaceException("No standard lib is currently defined in this namespace");
        }
    }

    public LanguageLevel getLanguageLevel() {
        if (parent != null) {
            return parent.getLanguageLevel();
        }
        return languageLevel;
    }
}
