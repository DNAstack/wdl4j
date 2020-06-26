package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.LanguageLevel;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.UnsupportedLanguageLevel;
import com.dnastack.wdl4j.lib.exception.WdlSyntaxError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class WdlDocumentFactoryTest {

    @Test
    public void testParseWdlV1LanguageLevel(){
        String wdl = "version 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        LanguageLevel languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_V1);

        wdl = "version      1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_V1);

        wdl = "version 1 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_V1);

        wdl = "version      1 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_V1);

        wdl = "#Comment\nversion 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_V1);
    }

    @Test
    public void testWdlDraft2LanguageLevel(){
        String wdl = "workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        LanguageLevel languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_DRAFT_2);

        wdl = "#Comment\n workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";
        languageLevel = documentFactory.detectVersion(wdl);
        Assertions.assertEquals(languageLevel,LanguageLevel.WDL_DRAFT_2);
    }

    @Test
    public void testUnknownLanguageLevel_ThrowsError(){
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Assertions.assertThrows(UnsupportedLanguageLevel.class,() -> documentFactory.detectVersion("version workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        Assertions.assertThrows(UnsupportedLanguageLevel.class,() -> documentFactory.detectVersion("version 1.1 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        Assertions.assertThrows(UnsupportedLanguageLevel.class,() -> documentFactory.detectVersion("version 2 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        Assertions.assertThrows(UnsupportedLanguageLevel.class,() -> documentFactory.detectVersion("version draft-2 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
    }


    @Test
    public void testParsingDraft2String_ResultsInDraft2ParserRules() throws IOException {
        String wdl = "workflow a { String p = 'happy' }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdl);
        Assertions.assertEquals(document.getLanguageLevel(),LanguageLevel.WDL_DRAFT_2);
    }

    @Test
    public void testParsingV1String_ResultsInV1ParserRules() throws IOException {
        String wdl = "version 1.0 workflow a { String p = 'happy' }";
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdl);
        Assertions.assertEquals(document.getLanguageLevel(),LanguageLevel.WDL_V1);
    }


    // Assert Features From V1 Are not valid in draft 2
    @Test
    public void testV1Features_throwsErrorInDraft2Parser() throws IOException {
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        // Input block in workflow
        Assertions.assertThrows(WdlSyntaxError.class, () -> documentFactory.create("workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"));
        // input block in task
        Assertions.assertThrows(WdlSyntaxError.class, () -> documentFactory.create("task a { input { String p = 'Happy are those ~{1 + 2}' }}"));
        // Version statement
        Assertions.assertThrows(WdlSyntaxError.class, () -> documentFactory.parseWdlDraft2IntoDocument(documentFactory.getDraft2Parser("version 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }"),null));
        // Structs not supported
        Assertions.assertThrows(WdlSyntaxError.class, () -> documentFactory.create("struct b { File x } workflow a { String p = 'Happy are those ~{1 + 2}' }"));
    }

    @Test
    public void testParsingWdlString() throws IOException {
        String wdl = "version 1.0 workflow a { input { String p = 'Happy are those ~{1 + 2}'} }";

        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(wdl);
        Assertions.assertEquals("1.0", document.getVersion().getRelease());
        Assertions.assertEquals("a", document.getWorkflow().getName());
        Assertions.assertEquals(1, document.getWorkflow().getInputs().getDeclarations().size());
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
        Assertions.assertEquals("1.0", document.getVersion().getRelease());
        Assertions.assertEquals("a", document.getWorkflow().getName());
        Assertions.assertEquals(1, document.getWorkflow().getInputs().getDeclarations().size());
    }

    @Test
    public void test3() throws IOException {
        URI url = URI.create(
                "https://raw.githubusercontent.com/openwdl/Testathon-2020/master/pytest-wdl-tests/tests/task/add2/main.wdl");
        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.create(url);
        Assertions.assertNotNull(document.getVersion());
    }

    @Test
    public void testImportsResolveFromLocalFiles() throws IOException, NamespaceException {
        String wdl = "version 1.0 import \"file2.wdl\" as file2 workflow a { input {String p = 'Happy are those ~{1 + 2}'} }";
        String wdl2 = "version 1.0 task a { input { String p } command <<< echo ~{p} >>> }";
        Path tmpdir = Files.createTempDirectory("wdl-imports");
        Path wdl1Path = Paths.get(tmpdir.toString(), "file1.wdl");
        Path wdl2Path = Paths.get(tmpdir.toString(), "file2.wdl");
        Files.write(wdl1Path, wdl.getBytes());
        Files.write(wdl2Path, wdl2.getBytes());

        WdlDocumentFactory documentFactory = new WdlDocumentFactory();
        Document document = documentFactory.createAndImport(wdl1Path.toUri());
        Assertions.assertEquals("1.0", document.getVersion().getRelease());
        Assertions.assertNotNull(document.getImportedDocuments());
        Assertions.assertTrue(document.getImportedDocuments().keySet().contains("file2"));

    }


}