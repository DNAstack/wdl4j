package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.LanguageLevel;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.UnsupportedLanguageLevel;
import com.dnastack.wdl4j.lib.exception.WdlSyntaxError;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class WdlDocumentFactoryTest {

    @Test
    public void testParseWdlV1LanguageLevel() {
        String wdl = "version 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        LanguageLevel languageLevel = documentFactory.detectVersion(wdl);
        assertThat(languageLevel).isEqualTo(LanguageLevel.WDL_V1);
        wdl = "version      1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        assertThat(languageLevel).isEqualTo(LanguageLevel.WDL_V1);

        wdl = "#Comment\nversion 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        assertThat(languageLevel).isEqualTo(LanguageLevel.WDL_V1);
    }

    @Test
    public void testWdlDraft2LanguageLevel() {
        String wdl = "workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        LanguageLevel languageLevel = documentFactory.detectVersion(wdl);
        assertThat(languageLevel).isEqualTo(LanguageLevel.WDL_DRAFT_2);

        wdl = "#Comment\n workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        assertThat(languageLevel).isEqualTo(LanguageLevel.WDL_DRAFT_2);
    }

    @Test
    public void testUnknownLanguageLevel_ThrowsError() {
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        assertThatExceptionOfType(UnsupportedLanguageLevel.class).isThrownBy(() -> documentFactory.detectVersion(
                "version workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        assertThatExceptionOfType(UnsupportedLanguageLevel.class).isThrownBy(() -> documentFactory.detectVersion(
                "version 1 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        assertThatExceptionOfType(UnsupportedLanguageLevel.class).isThrownBy(() -> documentFactory.detectVersion(
                "version 1.1 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        assertThatExceptionOfType(UnsupportedLanguageLevel.class).isThrownBy(() -> documentFactory.detectVersion(
                "version 2 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        assertThatExceptionOfType(UnsupportedLanguageLevel.class).isThrownBy(() -> documentFactory.detectVersion(
                "version draft-2 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
    }

    @Test
    public void testParsingDraft2String_ResultsInDraft2ParserRules() throws IOException {
        String wdl = "workflow a { String p = 'happy' }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdl);
        assertThat(document.getLanguageLevel()).isEqualTo(LanguageLevel.WDL_DRAFT_2);
    }

    @Test
    public void testParsingV1String_ResultsInV1ParserRules() throws IOException {
        String wdl = "version 1.0 workflow a { String p = 'happy' }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdl);
        assertThat(document.getLanguageLevel()).isEqualTo(LanguageLevel.WDL_V1);
    }

    // Assert Features From V1 Are not valid in draft 2
    @Test
    public void testV1Features_throwsErrorInDraft2Parser() throws IOException {
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        // Input block in workflow
        assertThatExceptionOfType(WdlSyntaxError.class).isThrownBy(()-> documentFactory.create(
                                        "workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        // input block in task
        assertThatExceptionOfType(WdlSyntaxError.class).isThrownBy(()-> documentFactory.create("task a { input { String p = 'Happy are those ~{1 + 2}' }}"));
        // Version statement
        assertThatExceptionOfType(WdlSyntaxError.class).isThrownBy(() -> documentFactory.parseWdlDraft2IntoDocument(documentFactory.getDraft2Parser(
                                        "version 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"),
                                                                                 null));
        // Structs not supported
        assertThatExceptionOfType(WdlSyntaxError.class).isThrownBy(() -> documentFactory.create(
                                        "struct b { File x } workflow a { String p = 'Happy are those ~{1 + 2}' }"));
    }

    @Test
    public void testParsingWdlString() throws IOException {
        String wdl = "version 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";

        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdl);
        assertThat(document.getVersion()).extracting("release").isEqualTo("1.0");
        assertThat(document.getWorkflow()).extracting("name").isEqualTo("a");
        assertThat(document.getWorkflow().getInputs().getDeclarations()).hasSize(1);
    }

    @Test
    public void testParsingWorkflowFromFile() throws IOException {
        String wdl = "version 1.0 workflow a { input {String p = 'Happy are those ~{1 + 2}'} }";
        File wdlFile = Files.createTempFile("tempfile", ".wdl").toFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(wdlFile));
        bw.write(wdl);
        bw.close();
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdlFile.toURI());
        assertThat(document.getVersion()).extracting("release").isEqualTo("1.0");
        assertThat(document.getWorkflow()).extracting("name").isEqualTo("a");
        assertThat(document.getWorkflow().getInputs().getDeclarations()).hasSize(1);
    }


}