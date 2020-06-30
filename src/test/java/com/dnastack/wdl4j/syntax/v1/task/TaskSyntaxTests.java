package com.dnastack.wdl4j.syntax.v1.task;

import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.Task;
import com.dnastack.wdl4j.lib.exception.WdlSyntaxError;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.syntax.v1.AbstractSyntaxTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TaskSyntaxTests extends AbstractSyntaxTest {

    @Test
    public void minimalTaskParsersProperly() throws WdlValidationError, IOException, URISyntaxException {
        Document document = parseResourceAndImport("syntax/v1/tasks/minimal_task.wdl");
        assertThat(document.getTasks()).hasSize(2).extracting("name").containsExactly("foo", "bar");
        for (Task task : document.getTasks()) {
            assertThat(task.getOutputs()).isNull();
            assertThat(task.getInputs()).isNull();
            assertThat(task.getRuntime()).isNull();
            assertThat(task.getMeta()).isNull();
            assertThat(task.getParameterMeta()).isNull();
            assertThat(task.getCommand()).isNotNull();
        }
        document.validate();
    }

    @Test
    public void taskMissingCommandBlock_FailsValidation() throws IOException {
        //totally empty
        assertThatExceptionOfType(WdlSyntaxError.class).isThrownBy(() -> getDocumentFactory().create(
                "version 1.0\n task foo {}").validate()).withMessageContaining("mismatched input '}' expecting");
        //with inputs
        assertThatExceptionOfType(WdlValidationError.class).isThrownBy(() -> getDocumentFactory().create(
                "version 1.0\n task foo { input { String a }}").validate()).withMessageContaining("A task Requires at least one command");
        //with outputs
        assertThatExceptionOfType(WdlValidationError.class).isThrownBy(() -> getDocumentFactory().create(
                "version 1.0\n task foo {output { String a = \"\" }}").validate()).withMessageContaining("A task Requires at least one command");
        assertThatExceptionOfType(WdlValidationError.class).isThrownBy(() -> getDocumentFactory().create(
                "version 1.0\n task foo {runtime { docker: \"test\"}}").validate()).withMessageContaining("A task Requires at least one command");

    }


    @Test
    public void tasksWithSingleSections_ProperlyValidate() throws WdlValidationError, IOException, URISyntaxException {
        Document document = parseResourceAndImport("syntax/v1/tasks/task_sections.wdl");
        document.validate();
    }

    @Test
    public void taskDefiningOutputsPriorToInputs_withoutUsingDeclaration_Validates() throws IOException, WdlValidationError, URISyntaxException {

    }

    @Test
    public void taskUsingDeclarationsBeforeTheyAreDefined_FailsValidation(){

    }
}
