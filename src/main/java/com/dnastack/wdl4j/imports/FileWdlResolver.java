package com.dnastack.wdl4j.imports;

import com.dnastack.wdl4j.api.WdlResolver;
import com.dnastack.wdl4j.exception.WdlResolutionException;
import com.dnastack.wdl4j.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWdlResolver implements WdlResolver {

    @Override
    public boolean canResolve(URI resource) {
        return UriUtils.isFile(resource);
    }

    @Override
    public String resolveWdlToString(URI resource) throws IOException {
        return resolveWdlToString(resource, null);
    }

    @Override
    public String resolveWdlToString(URI resource, URI context) throws IOException {
        if (resource == null) {
            throw new WdlResolutionException("Could not resolve null import");
        }

        URI uriToResolve = resource;
        if (context != null) {
            uriToResolve = context.resolve(resource);
        }

        byte[] bytes = Files.readAllBytes(Paths.get(uriToResolve));
        return new String(bytes);
    }
}
