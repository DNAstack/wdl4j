package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.StandardLib;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.typing.CoercionOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class Namespace {

    private final Map<String, Namespace> children = new HashMap<>();
    @Getter
    private final Map<String, WdlElement> elements = new HashMap<>();
    @Setter
    @Getter
    private LanguageLevel languageLevel;
    @Setter
    private Namespace parent;
    @Setter
    private StandardLib lib;

    @Getter
    @Setter
    private CoercionOptions coercionOptions;

    public Namespace(Namespace namespace) {
        this.languageLevel = namespace.languageLevel;
        this.parent = namespace.parent;
        this.children.putAll(namespace.children);
        this.coercionOptions = namespace.coercionOptions;
    }

    private void addChildElement(String name, WdlElement element) throws NamespaceException {
        String[] parts = name.split("\\.");
        String namespace = parts[0];
        String childName = String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
        getChildNamespace(namespace).addElement(childName, element);
    }

    private boolean isDefinedInLocalNamespace(String name) {
        return elements.containsKey(name);
    }

    private boolean isDefinedInParentNamespace(String name) {
        return parent != null && parent.isDefinedInNamespace(name);
    }

    private boolean isNamespaceDefined(String[] name) {
        return isNamespaceDefinedInChild(name) || isNamespaceDefinedInParent(name);
    }

    private boolean isNamespaceDefinedInParent(String[] name) {
        return parent != null && parent.isNamespaceDefined(name);
    }

    private boolean isNamespaceDefinedInChild(String[] name) {
        if (name.length == 1) {
            return children.containsKey(name[0]);
        } else {
            return children.containsKey(name[0]) && children.get(name[0])
                                                            .isNamespaceDefined(Arrays.copyOfRange(name,
                                                                                                   1,
                                                                                                   name.length));
        }
    }

    public WdlElement getElement(String name) throws NamespaceException {
        if (isDefinedInParentNamespace(name)) {
            return parent.getElement(name);
        } else if (isDefinedInLocalNamespace(name)) {
            return elements.get(name);
        } else if (isDefinedInChildNamespace(name)) {
            return getChildElement(name);
        } else {
            throw new NamespaceException("WdlElement with name \"" + name + "\" is not defined in the current namespace");
        }
    }

    public WdlElement getChildElement(String name) throws NamespaceException {
        String[] parts = name.split("\\.");
        String namespace = parts[0];
        return getChildNamespace(namespace).getElement(String.join(".", Arrays.copyOfRange(parts, 1, parts.length)));
    }

    public void addElement(String name, WdlElement element) throws NamespaceException {
        assertNotDefinedInNamespace(name,
                                    "Namespace collision, An element with the name  '" + name + "' already exists");
        if (name.contains(".")) {
            addChildElement(name, element);
        } else {
            elements.put(name, element);
        }
    }

    public boolean isDefinedInNamespace(String name) {
        return isDefinedInChildNamespace(name) || isDefinedInParentNamespace(name) || isDefinedInLocalNamespace(name);
    }

    public boolean isDefinedInChildNamespace(String name) {
        if (name.contains(".")) {
            String[] parts = name.split("\\.");
            String namespace = parts[0];
            String childName = String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
            Namespace childNamespace = children.get(namespace);
            return childNamespace != null && childNamespace.isDefinedInNamespace(childName);
        } else {
            return false;
        }
    }

    public void assertNotDefinedInNamespace(String name, String errorMessage) throws NamespaceException {
        if (isDefinedInNamespace(name)) {
            throw new NamespaceException(errorMessage);
        }
    }

    public void addDeclaration(String name, Declaration declaration) throws NamespaceException {
        addElement(name, declaration);
    }

    public void addStruct(Struct struct) throws NamespaceException {
        addElement(struct.getName(), struct);
    }

    public <T extends WdlElement> Map<String, T> filterElementsForThisNamespace(Class<T> clazz) {
        Map<String, T> filtered = new HashMap<>();
        elements.forEach((key, value) -> {
            if (clazz.isAssignableFrom(value.getClass())) {
                filtered.put(key, (T) value);
            }
        });
        return filtered;
    }

    public Declaration getDeclaration(String name) throws NamespaceException {
        WdlElement element = getElement(name);
        if (element instanceof Declaration) {
            return (Declaration) element;
        } else {
            throw new NamespaceException("Element with name \"" + name + "\" exists in the current namespace, however it is not a Declaration");
        }
    }

    public Struct getStruct(String name) throws NamespaceException {
        WdlElement element = getElement(name);
        if (element instanceof Struct) {
            return (Struct) element;
        } else {
            throw new NamespaceException("Element with name \"" + name + "\" exists in the current namespace, however it is not a Struct");
        }
    }

    public Task getTask(String name) throws NamespaceException {
        WdlElement element = getElement(name);
        if (element instanceof Task) {
            return (Task) element;
        } else {
            throw new NamespaceException("Element with name \"" + name + "\" exists in the current namespace, however it is not a Task");
        }
    }

    public Call getCall(String name) throws NamespaceException {
        WdlElement element = getElement(name);
        if (element instanceof Call) {
            return (Call) element;
        } else {
            throw new NamespaceException("Element with name \"" + name + "\" exists in the current namespace, however it is not a Call");
        }
    }

    public Workflow getWorkflow(String name) throws NamespaceException {
        WdlElement element = getElement(name);
        if (element instanceof Workflow) {
            return (Workflow) element;
        } else {
            throw new NamespaceException("Element with name \"" + name + "\" exists in the current namespace, however it is not a Workflow");
        }
    }

    public Namespace getChildNamespace(String name) throws NamespaceException {
        String[] parts = name.split("\\.");
        if (isNamespaceDefinedInParent(parts)) {
            return parent.getChildNamespace(name);
        } else if (isNamespaceDefinedInChild(parts) && parts.length == 1) {
            return children.get(name);
        } else if (isNamespaceDefinedInChild(parts)) {
            return children.get(parts[0])
                           .getChildNamespace(String.join(".", Arrays.copyOfRange(parts, 1, parts.length)));
        } else {
            throw new NamespaceException("Child Namespace with name \"" + name + "\" is not defined in the current namespace");
        }
    }

    public void addChildNamespace(String name, Namespace child) throws NamespaceException {
        assertNotDefinedInNamespace(name,
                                    "Namespace collision, an element in the Namespace with the name \"" + name + "\" already exists");
        children.put(name, child);
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

    public CoercionOptions getCoercionOptions() {
        if (parent != null) {
            return parent.getCoercionOptions();
        }
        return coercionOptions;
    }

    public LanguageLevel getLanguageLevel() {
        if (parent != null) {
            return parent.getLanguageLevel();
        }
        return languageLevel;
    }
}
