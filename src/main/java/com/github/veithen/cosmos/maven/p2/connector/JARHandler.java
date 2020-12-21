/*-
 * #%L
 * p2-maven-connector
 * %%
 * Copyright (C) 2012 - 2020 Andreas Veithen
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.veithen.cosmos.maven.p2.connector;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.repository.artifact.IArtifactDescriptor;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JARHandler extends ArtifactHandler {
    private static final Logger logger = LoggerFactory.getLogger(JARHandler.class);

    @Override
    void download(Artifact artifact, IArtifactRepository artifactRepository, IArtifactDescriptor descriptor, OutputStream out) throws IOException, DownloadException {
        IStatus status;
        status = artifactRepository.getArtifact(descriptor, out, new SystemOutProgressMonitor());
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Status: %s", status));
        }
        if (!status.isOK()) {
            throw new DownloadException(status.getMessage(), status.getException());
        }
    }
}
