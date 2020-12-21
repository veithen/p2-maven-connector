/*-
 * #%L
 * Cosmos
 * %%
 * Copyright (C) 2012 - 2018 Andreas Veithen
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
package com.github.veithen.cosmos.maven.p2;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.repository.artifact.ArtifactKeyQuery;

public final class ArtifactCoordinateMapper {
    private ArtifactCoordinateMapper() {}

    public static Artifact createArtifact(P2Coordinate p2Coordinate) {
        String id = p2Coordinate.getId();
        String artifactId;
        String classifier;
        if (id.endsWith(".source")) {
            artifactId = id.substring(0, id.length()-7);
            classifier = "sources";
        } else {
            artifactId = id;
            classifier = null;
        }
        Version version = p2Coordinate.getVersion();
        return new DefaultArtifact(p2Coordinate.getClassifier(), artifactId, classifier, "jar",
                version == null ? "" : version.toString());
    }

    public static P2Coordinate createP2Coordinate(Artifact artifact) {
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
        return new ArtifactKeyQuery(groupId, artifactId, null);
    }
}
