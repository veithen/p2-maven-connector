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
package com.github.veithen.maven.p2;

import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;

public final class P2Coordinate {
    private final String classifier;
    private final String id;
    private final Version version;

    public P2Coordinate(String classifier, String id, Version version) {
        this.classifier = classifier;
        this.id = id;
        this.version = version;
    }

    public P2Coordinate(String id, Version version) {
        // TODO: use constant for osgi.bundle
        this("osgi.bundle", id, version);
    }

    public IArtifactKey createIArtifactKey(IArtifactRepository repository) {
        return repository.createArtifactKey(classifier, id, version);
    }

    public String getClassifier() {
        return classifier;
    }

    public String getId() {
        return id;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format("[classifier=%s,id=%s,version=%s]", classifier, id, version);
    }
}
