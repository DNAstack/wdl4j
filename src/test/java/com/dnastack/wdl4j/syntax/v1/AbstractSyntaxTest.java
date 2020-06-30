package com.dnastack.wdl4j.syntax.v1;

import com.dnastack.wdl4j.WdlDocumentFactory;
import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AbstractSyntaxTest {

    @Getter
    WdlDocumentFactory documentFactory = new WdlDocumentFactory();


    public Document parseResourceAndImport(String resourceLocation) throws URISyntaxException, IOException, NamespaceException {
        URI resource = this.getClass().getClassLoader().getResource(resourceLocation).toURI();
        return documentFactory.createAndImport(resource);
    }

}
