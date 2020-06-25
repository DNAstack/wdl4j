package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
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