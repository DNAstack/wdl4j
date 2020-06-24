package com.dnastack.wdl4j.api;

import java.io.IOException;
import java.net.URI;

public interface WdlResolver {

    boolean canResolve(URI resource);

    String resolveWdlToString(URI resource) throws IOException;

    String resolveWdlToString(URI resource, URI context) throws IOException;

}
