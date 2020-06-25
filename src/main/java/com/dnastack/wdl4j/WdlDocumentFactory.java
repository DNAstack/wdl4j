package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.Import;
import com.dnastack.wdl4j.lib.api.WdlResolver;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.WdlResolutionException;
import com.dnastack.wdl4j.lib.exception.WdlSyntaxError;
import com.dnastack.wdl4j.util.FileWdlResolver;
import com.dnastack.wdl4j.util.UrlWdlResolver;
import com.dnastack.wdl4j.util.UriUtils;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.openwdl.wdl.parser.WdlV1Lexer;
import org.openwdl.wdl.parser.WdlV1Parser;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class WdlDocumentFactory {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WdlDocumentFactory.class);
    private List<WdlResolver> wdlResolvers;

    public WdlDocumentFactory() {
        wdlResolvers = Arrays.asList(new UrlWdlResolver(), new FileWdlResolver());
    }

    public WdlDocumentFactory(List<WdlResolver> wdlResolvers) {
        Objects.requireNonNull(wdlResolvers, "WdlResolvers cannot be null");
        this.wdlResolvers = wdlResolvers;
    }

    private String resolveWdlToString(URI uri, URI context) throws WdlResolutionException {
        List<WdlResolver> resolvers = wdlResolvers.stream()
                                                  .filter(resolver -> resolver.canResolve(uri))
                                                  .collect(Collectors.toList());

        if (resolvers.isEmpty()) {
            throw new WdlResolutionException("There are no configured WdlResolvers capable of resolving: " + uri.toString());
        }

        IOException lastException = null;
        for (WdlResolver resolver : resolvers) {
            try {
                return resolver.resolveWdlToString(uri, context);
            } catch (IOException e) {
                lastException = e;
                log.warn("Encountered an error while resolving WDL: " + e.getMessage());
            }
        }

        throw new WdlResolutionException("Could not resolve WDL", lastException);
    }

    private Document parseWdlIntoDocument(WdlV1Parser parser, URI context) {
        WdlAggregatingErrorListener errorListener = new WdlAggregatingErrorListener(context);
        try {
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            WdlV1DocumentVisitor visitor = new WdlV1DocumentVisitor();
            Document document = visitor.visitDocument(parser.document());
            if (errorListener.hasErrors()) {
                throw new WdlSyntaxError(errorListener.getErrors());
            }
            return document;
        } catch (Exception e) {
            if (e instanceof WdlSyntaxError) {
                throw e;
            } else if (errorListener.hasErrors()) {
                throw new WdlSyntaxError(errorListener.getErrors());
            } else {
                throw new WdlSyntaxError("Encountered a non recoverable syntax error", e);
            }
        }
    }

    private WdlV1Parser getParser(String inp) {
        CodePointBuffer codePointBuffer = CodePointBuffer.withBytes(ByteBuffer.wrap(inp.getBytes()));
        WdlV1Lexer lexer = new WdlV1Lexer(CodePointCharStream.fromBuffer(codePointBuffer));
        return new WdlV1Parser(new CommonTokenStream(lexer));
    }

    public void setWdlResolvers(List<WdlResolver> wdlResolvers) {
        Objects.requireNonNull(wdlResolvers, "WdlResolvers cannot be null");
        this.wdlResolvers = wdlResolvers;
    }

    public void addImportResolver(WdlResolver wdlResolver) {
        wdlResolvers.add(wdlResolver);
    }

    public Document create(String wdl) throws IOException {
        WdlV1Parser parser = getParser(wdl);
        return parseWdlIntoDocument(parser, null);
    }

    public Document create(URI wdlPath) throws IOException {
        String resolvedWdl = resolveWdlToString(wdlPath, null);
        WdlV1Parser parser = getParser(resolvedWdl);
        return parseWdlIntoDocument(parser, wdlPath);

    }

    public Document createAndImport(String wdl) throws IOException, NamespaceException {
        Document document = create(wdl);
        return resolveImports(document, null);
    }

    public Document createAndImport(URI wdlPath) throws IOException, NamespaceException {
        Document document = create(wdlPath);
        return resolveImports(document, wdlPath);
    }

    public Document resolveImports(Document wdl, URI uriContext) throws IOException, NamespaceException {
        List<Import> imports = wdl.getImports();
        if (imports != null && imports.size() > 0) {
            Map<String, Document> importedDocuments = new HashMap<>();
            for (Import wdlImport : imports) {
                URI resolvedImportUri = wdlImport.resolveImportUri();
                if (uriContext != null) {
                    resolvedImportUri = uriContext.resolve(resolvedImportUri);
                }
                Document imported = createAndImport(resolvedImportUri);
                if (wdlImport.getName() != null) {
                    importedDocuments.put(wdlImport.getName(), imported);
                } else {
                    importedDocuments.put(UriUtils.getImportNamepsaceFromUri(resolvedImportUri), imported);
                }
            }
            wdl.setImportedDocuments(importedDocuments);
        }
        return wdl;
    }
}
