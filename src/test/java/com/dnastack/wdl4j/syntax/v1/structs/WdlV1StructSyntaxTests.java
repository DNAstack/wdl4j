package com.dnastack.wdl4j.syntax.v1.structs;

import com.dnastack.wdl4j.lib.Declaration;
import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.Struct;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.IntType;
import com.dnastack.wdl4j.lib.typing.StringType;
import com.dnastack.wdl4j.lib.typing.StructType;
import com.dnastack.wdl4j.syntax.v1.AbstractSyntaxTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class WdlV1StructSyntaxTests extends AbstractSyntaxTest {

    @Test
    public void testSimpleStruct() throws URISyntaxException, IOException, WdlValidationError {
        Document document = parseResourceAndImport("syntax/v1/structs/simple.wdl");

        assertThat(document.getStructs()).isNotNull().hasSize(2);

        List<Struct> structList = document.getStructs();
        Struct person = structList.get(0);
        Struct family = structList.get(1);

        assertThat(person).extracting("name").isEqualTo("Person");
        List<Declaration> members = person.getMembers();
        assertThat(members).isNotNull().hasSize(3);
        assertThat(members).extracting("name").containsExactly("firstName", "lastName", "age");
        assertThat(members).extracting("declType")
                           .containsExactly(StringType.getType(), StringType.getType(), IntType.getType());

        assertThat(family).extracting("name").isEqualTo("Family");
        assertThat(family.getMembers()).extracting("name").containsExactly("members");
        assertThat(family.getMembers()).extracting("declType")
                                       .containsExactly(ArrayType.getType(StructType.getType("Person")));
        document.validate();
    }

    @Test
    public void testStructImportedIntoGlobalNamespace() throws URISyntaxException, IOException, WdlValidationError {
        Document document = parseResourceAndImport("syntax/v1/structs/import_without_alias.wdl");
        assertThat(document.getStructs()).isNotNull().hasSize(3);
        assertThat(document.getStructs()).extracting("name").containsExactly("Team", "Person", "Family");
        document.validate();

    }

    @Test
    public void testStructImportedIntoGlobalNamespace_WithAlias() throws URISyntaxException, IOException, WdlValidationError {
        Document document = parseResourceAndImport("syntax/v1/structs/import_with_alias.wdl");
        assertThat(document.getStructs()).isNotNull().hasSize(3);
        assertThat(document.getStructs()).extracting("name").containsExactly("Team", "Person", "ExtendedFamily");
        document.validate();

    }

    @Test
    public void testStructImportedIntoGlobalNamespace_WithExistingStruct_resultsInNamespaceCollision() throws URISyntaxException, IOException, WdlValidationError {
        Document document = parseResourceAndImport("syntax/v1/structs/namespace_collision.wdl");

        assertThat(document.getStructs()).isNotNull().hasSize(4);
        assertThat(document.getStructs()).extracting("name").containsExactly("Family", "Team", "Person", "Family");
        assertThatExceptionOfType(NamespaceException.class).isThrownBy(document::validate)
                                                           .withMessage(
                                                                   "Namespace collision, An element with the name  'Family' already exists");
    }

    @Test
    public void testStructImportedIntoGlobalNamespace_AliasingNonExistantStruct_resultsInNamespaceValidationError() throws URISyntaxException, IOException, WdlValidationError {
        Document document = parseResourceAndImport("syntax/v1/structs/import_with_alias_and_unknown_struct_name.wdl");
        assertThatExceptionOfType(WdlValidationError.class).isThrownBy(document::validate)
                                                           .withMessageStartingWith("Attempting to alias non-existant");
    }
}
