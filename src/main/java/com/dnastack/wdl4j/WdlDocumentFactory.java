package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Document;
import com.dnastack.wdl4j.lib.Import;
import com.dnastack.wdl4j.lib.LanguageLevel;
import com.dnastack.wdl4j.lib.Struct;
import com.dnastack.wdl4j.lib.api.WdlResolver;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.UnsupportedLanguageLevel;
import com.dnastack.wdl4j.lib.exception.WdlResolutionException;
import com.dnastack.wdl4j.lib.exception.WdlSyntaxError;
import com.dnastack.wdl4j.util.FileWdlResolver;
import com.dnastack.wdl4j.util.UriUtils;
import com.dnastack.wdl4j.util.UrlWdlResolver;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.openwdl.wdl.parser.WdlDraft2Lexer;
import org.openwdl.wdl.parser.WdlDraft2Parser;
import org.openwdl.wdl.parser.WdlV1Lexer;
import org.openwdl.wdl.parser.WdlV1Parser;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class WdlDocumentFactory {

    private List<WdlResolver> wdlResolvers;

    public WdlDocumentFactory() {
        wdlResolvers = Arrays.asList(new UrlWdlResolver(), new FileWdlResolver());
    }

    public WdlDocumentFactory(List<WdlResolver> wdlResolvers) {
        Objects.requireNonNull(wdlResolvers, "WdlResolvers cannot be null");
        this.wdlResolvers = wdlResolvers;
    }

    public void setWdlResolvers(List<WdlResolver> wdlResolvers) {
        Objects.requireNonNull(wdlResolvers, "WdlResolvers cannot be null");
        this.wdlResolvers = wdlResolvers;
    }

    public void addImportResolver(WdlResolver wdlResolver) {
        wdlResolvers.add(wdlResolver);
    }

    public Document create(String wdl) throws IOException {
        LanguageLevel languageLevel = detectVersion(wdl);
        if (languageLevel.equals(LanguageLevel.WDL_DRAFT_2)) {
            WdlDraft2Parser parser = getDraft2Parser(wdl);
            return parseWdlDraft2IntoDocument(parser, null);
        } else {
            WdlV1Parser parser = getV1Parser(wdl);
            return parseWdlV1IntoDocument(parser, null);
        }
    }

    public Document create(URI wdlPath) throws IOException {
        String resolvedWdl = resolveWdlToString(wdlPath, wdlPath);
        LanguageLevel languageLevel = detectVersion(resolvedWdl);
        if (languageLevel.equals(LanguageLevel.WDL_DRAFT_2)) {
            WdlDraft2Parser parser = getDraft2Parser(resolvedWdl);
            return parseWdlDraft2IntoDocument(parser, wdlPath);
        } else {
            WdlV1Parser parser = getV1Parser(resolvedWdl);
            return parseWdlV1IntoDocument(parser, wdlPath);
        }
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
            for (Import wdlImport : imports) {
                URI resolvedImportUri = wdlImport.resolveImportUri();
                if (uriContext != null) {
                    resolvedImportUri = uriContext.resolve(resolvedImportUri);
                }
                Document imported = createAndImport(resolvedImportUri);
                if (!imported.getLanguageLevel().equals(wdl.getLanguageLevel())) {
                    throw new UnsupportedLanguageLevel("Parent WDL has a language level of " + wdl.getLanguageLevel() + " however imported document does not");
                }

                wdlImport.setDocument(imported);
                if (wdlImport.getName() == null) {
                    wdlImport.setName(UriUtils.getImportNamepsaceFromUri(resolvedImportUri));
                }

                //Add struct to global parent namespace
                for (Struct struct : imported.getStructs()) {
                    String alias = getAliasForStruct(wdlImport.getAliases(), struct);
                    if (alias != null) {
                        struct = new Struct(alias, struct.getMembers(), struct.getId());
                    }
                    wdl.getStructs().add(struct);
                }

            }

        }
        return wdl;
    }

    private String getAliasForStruct(List<Import.ImportAlias> aliases, Struct struct) {
        if (aliases == null) {
            return null;
        }
        return aliases.stream()
                      .filter(alias -> alias.getName().equals(struct.getName()))
                      .map(alias -> alias.getAlias())
                      .findFirst()
                      .orElse(null);
    }

    public String resolveWdlToString(URI uri, URI context) throws WdlResolutionException {
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

    public Document parseWdlV1IntoDocument(WdlV1Parser parser, URI context) {
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

    public Document parseWdlDraft2IntoDocument(WdlDraft2Parser parser, URI context) {
        WdlAggregatingErrorListener errorListener = new WdlAggregatingErrorListener(context);
        try {
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            WdlDraft2DocumentVisitor visitor = new WdlDraft2DocumentVisitor();
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

    public WdlV1Parser getV1Parser(String inp) {
        CodePointBuffer codePointBuffer = CodePointBuffer.withBytes(ByteBuffer.wrap(inp.getBytes()));
        WdlV1Lexer lexer = new WdlV1Lexer(CodePointCharStream.fromBuffer(codePointBuffer));
        return new WdlV1Parser(new CommonTokenStream(lexer));
    }

    public WdlDraft2Parser getDraft2Parser(String inp) {
        CodePointBuffer codePointBuffer = CodePointBuffer.withBytes(ByteBuffer.wrap(inp.getBytes()));
        WdlDraft2Lexer lexer = new WdlDraft2Lexer(CodePointCharStream.fromBuffer(codePointBuffer));
        return new WdlDraft2Parser(new CommonTokenStream(lexer));
    }

    LanguageLevel detectVersion(String wdl) {
        String[] lines = wdl.split("[\n\r]");
        String firstLine = null;
        int i = 0;
        while (firstLine == null && i < lines.length) {
            String line = lines[i].trim();
            if (!line.startsWith("#")) {
                firstLine = line;
            }
            i++;
        }

        if (firstLine == null) {
            throw new UnsupportedLanguageLevel("Could not resolve WDL, No readable lines in WDL file");
        }

        if (firstLine.startsWith("version")) {
            Pattern v1Pattern = Pattern.compile("^version\\s+1\\.0");
            Matcher matcher = v1Pattern.matcher(firstLine);
            if (matcher.find()) {
                return LanguageLevel.WDL_V1;
            } else {
                throw new UnsupportedLanguageLevel(
                        "The current WDL document appears to be an unsupported language level: " + firstLine);
            }

        } else {
            return LanguageLevel.WDL_DRAFT_2;
        }

    }
}
