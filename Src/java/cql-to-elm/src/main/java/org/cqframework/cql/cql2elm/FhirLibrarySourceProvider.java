package org.cqframework.cql.cql2elm;

import org.hl7.elm.r1.VersionedIdentifier;

import java.io.InputStream;
import java.util.Collections;

/**
 * Created by Bryn on 3/28/2017.
 */
public class FhirLibrarySourceProvider implements LibrarySourceProvider, NamespaceAware {

    private final String namespaceName = "FHIR";
    private final String namespaceUri = "http://hl7.org/fhir";

    @Override
    public InputStream getLibrarySource(VersionedIdentifier libraryIdentifier) {
        // If the FHIRHelpers library is referenced in a namespace-enabled context,
        // set the namespace to the FHIR namespace URI
        if (namespaceManager != null && namespaceManager.hasNamespaces()) {
            // If the context already has a namespace registered for FHIR, use that.
            NamespaceInfo namespaceInfo = namespaceManager.getNamespaceInfoFromUri(namespaceUri);
            if (namespaceInfo == null) {
                namespaceInfo = new NamespaceInfo(namespaceName, namespaceUri);
                namespaceManager.ensureNamespaceRegistered(namespaceInfo);
            }
            libraryIdentifier.setSystem(namespaceUri);
        }

        return FhirLibrarySourceProvider.class.getResourceAsStream(String.format("/org/hl7/fhir/%s-%s.cql", libraryIdentifier.getId(),
                libraryIdentifier.getVersion()));
    }

    private NamespaceManager namespaceManager;

    @Override
    public void setNamespaceManager(NamespaceManager namespaceManager) {
        this.namespaceManager = namespaceManager;
    }
}
