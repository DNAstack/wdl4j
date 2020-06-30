package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.StandardLib;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.CoercionOptions;
import com.dnastack.wdl4j.lib.typing.StructType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Document implements WdlElement {

    private Version version;
    private List<Import> imports;
    private List<Struct> structs;
    private List<Task> tasks;
    private Workflow workflow;
    @NonNull
    private int id;
    private StandardLib lib;
    private LanguageLevel languageLevel;
    private CoercionOptions coercionOptions;

    public Document(Version version, List<Import> imports, List<Struct> structs, List<Task> tasks, Workflow workflow, int id, LanguageLevel languageLevel) {
        this.version = version;
        this.imports = imports;
        this.structs = structs;
        this.tasks = tasks;
        this.workflow = workflow;
        this.id = id;
        this.coercionOptions = new CoercionOptions(languageLevel);
    }

    private void validateImportedDocs() throws WdlValidationError {
        for (Import wdlImport : imports) {
            if (wdlImport.getDocument() == null) {
                throw new WdlValidationError("Imported WDL does not contain a resolved Document");
            }

            Document importedDocument = wdlImport.getDocument();
            importedDocument.validate();
            if (wdlImport.getAliases() != null && !wdlImport.getAliases().isEmpty()) {
                for (Import.ImportAlias alias : wdlImport.getAliases()) {
                    if (importedDocument.getStructs() == null || importedDocument.getStructs()
                                                                                 .stream()
                                                                                 .noneMatch(s -> s.getName()
                                                                                                  .equals(alias.getName()))) {
                        throw new WdlValidationError("Attempting to alias non-existant struct '" + alias.getName() + "' from import '" + wdlImport
                                .getName() + "' as '" + alias.getAlias() + "'");
                    }
                }
            }
        }
    }

    private Namespace captureNamespace() throws NamespaceException {
        Namespace namespace = new Namespace();
        namespace.setCoercionOptions(coercionOptions);
        namespace.setLib(lib);
        if (imports != null) {
            for (Import wdlImport : imports) {
                captureChildNamespace(namespace, wdlImport.getName(), wdlImport.getDocument());
            }
        }

        if (structs != null && !languageLevel.equals(LanguageLevel.WDL_DRAFT_2)) {
            for (Struct struct : structs) {
                namespace.addStruct(struct);
            }
        }

        if (workflow != null) {
            captureWorkflowNamespace(namespace);
        }

        if (tasks != null) {
            for (Task task : tasks) {
                captureTaskNamespace(namespace, task);
            }
        }
        return namespace;
    }

    private void captureChildNamespace(Namespace currentNamespace, String childNamespace, Document childDocument) throws NamespaceException {
        currentNamespace.addChildNamespace(childNamespace, childDocument.captureNamespace());

    }

    private void captureTaskNamespace(Namespace currentNamespace, Task task) throws NamespaceException {
        Namespace taskNamespace = new Namespace();
        taskNamespace.setCoercionOptions(coercionOptions);
        taskNamespace.setLanguageLevel(languageLevel);
        if (task.getInputs() != null && task.getInputs().getDeclarations() != null) {
            for (Declaration inputDecl : task.getInputs().getDeclarations()) {
                if (inputDecl.getDeclType() instanceof StructType) {
                    setStructMembers(currentNamespace, (StructType) inputDecl.getDeclType());
                }
                inputDecl.setFromInputs(true);
                taskNamespace.addDeclaration(inputDecl.getName(), inputDecl);
            }
        }

        if (task.getOutputs() != null && task.getOutputs().getDeclarations() != null) {
            for (Declaration outputDecl : task.getOutputs().getDeclarations()) {
                if (outputDecl.getDeclType() instanceof StructType) {
                    setStructMembers(currentNamespace, (StructType) outputDecl.getDeclType());
                }
                outputDecl.setFromOutputs(true);
                taskNamespace.addDeclaration(outputDecl.getName(), outputDecl);
            }
        }

        currentNamespace.addChildNamespace(task.getName(), taskNamespace);
    }

    private void captureWorkflowNamespace(Namespace currentNamespace) throws NamespaceException {
        Namespace workflowNamespace = new Namespace();
        workflowNamespace.setCoercionOptions(coercionOptions);
        workflowNamespace.setLanguageLevel(languageLevel);
        if (workflow.getInputs() != null && workflow.getInputs().getDeclarations() != null) {
            for (Declaration inputDecl : workflow.getInputs().getDeclarations()) {
                if (inputDecl.getDeclType() instanceof StructType) {
                    setStructMembers(currentNamespace, (StructType) inputDecl.getDeclType());
                }
                inputDecl.setFromInputs(true);
                workflowNamespace.addDeclaration(inputDecl.getName(), inputDecl);
            }
        }

        if (workflow.getOutputs() != null && workflow.getOutputs().getDeclarations() != null) {
            for (Declaration outputDecl : workflow.getOutputs().getDeclarations()) {
                if (outputDecl.getDeclType() instanceof StructType) {
                    setStructMembers(currentNamespace, (StructType) outputDecl.getDeclType());
                }
                outputDecl.setFromOutputs(true);
                workflowNamespace.addDeclaration(outputDecl.getName(), outputDecl);
            }
        }

        currentNamespace.addChildNamespace(workflow.getName(), workflowNamespace);
    }

    private void setStructMembers(Namespace namespace, StructType structType) throws NamespaceException {
        Struct struct = namespace.getStruct(structType.getName());
        Map<String, Type> members = new HashMap<>();
        for (Declaration declaration : struct.getMembers()) {
            members.put(declaration.getName(), declaration.getDeclType());
        }
        structType.setMembers(members);
    }

    public void validate() throws WdlValidationError {

        if (imports != null) {
            validateImportedDocs();
        }

        Namespace namespace = captureNamespace();
        if (structs != null) {
            for (Struct struct : structs) {
                struct.validate(namespace);
            }
        }
        if (tasks != null) {
            for (Task task : tasks) {
                task.validate(namespace);
            }
        }

        if (workflow != null) {
            workflow.validate(namespace);
        }

    }

}
