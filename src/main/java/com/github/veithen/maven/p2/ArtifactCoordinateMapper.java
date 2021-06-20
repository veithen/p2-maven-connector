/*-
 * #%L
 * p2-maven-connector
 * %%
 * Copyright (C) 2012 - 2021 Andreas Veithen
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.repository.artifact.ArtifactKeyQuery;

public final class ArtifactCoordinateMapper {
    private static final Set<String> supportedP2Classifiers =
            new HashSet<>(Arrays.asList("osgi.bundle"));

    private ArtifactCoordinateMapper() {}

    public static P2Coordinate createP2Coordinate(Artifact artifact) {
        if (!supportedP2Classifiers.contains(artifact.getGroupId())) {
            return null;
        }
        String id;
        String classifier = artifact.getClassifier();
        if (classifier.isEmpty()) {
            id = artifact.getArtifactId();
        } else if (classifier.equals("sources")) {
            id = artifact.getArtifactId() + ".source";
        } else {
            return null;
        }
        Version version;
        try {
            version = Version.create(artifact.getVersion());
        } catch (IllegalArgumentException ex) {
            return null;
        }
        return new P2Coordinate(artifact.getGroupId(), id, version);
    }

    public static ArtifactKeyQuery createArtifactKeyQuery(String groupId, String artifactId) {
        if (!supportedP2Classifiers.contains(groupId)) {
            return null;
        }
        return new ArtifactKeyQuery(groupId, artifactId, null);
    }
}
