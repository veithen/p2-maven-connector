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
package com.github.veithen.maven.p2.connector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnector;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.transfer.NoRepositoryConnectorException;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.veithen.cosmos.osgi.runtime.CosmosRuntime;

@Named("p2")
@Singleton
public final class P2RepositoryConnectorFactory implements RepositoryConnectorFactory {
    private static final Logger logger = LoggerFactory.getLogger(P2RepositoryConnectorFactory.class);
    
    private final Map<File,IProvisioningAgent> provisioningAgents = new HashMap<>();
    
    @Override
    public float getPriority() {
        return 0;
    }

    @Override
    public RepositoryConnector newInstance(RepositorySystemSession session,
            RemoteRepository repository) throws NoRepositoryConnectorException {
        if (repository.getContentType().equals("p2")) {
            File localRepositoryDir = session.getLocalRepository().getBasedir();
            IProvisioningAgent provisioningAgent = provisioningAgents.get(localRepositoryDir);
            if (provisioningAgent == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Creating new provisioning agent for local repository %s", localRepositoryDir));
                }
                try {
                    provisioningAgent = CosmosRuntime.getInstance().getService(IProvisioningAgentProvider.class).createAgent(new File(localRepositoryDir, ".p2-metadata").toURI());
                } catch (BundleException | ProvisionException ex) {
                    logger.error(String.format("Failed to create provisioning agent for local repository %s", localRepositoryDir));
                    throw new NoRepositoryConnectorException(repository, ex);
                }
                provisioningAgents.put(localRepositoryDir, provisioningAgent);
            }
            return new P2RepositoryConnector(repository,
                    (IArtifactRepositoryManager)provisioningAgent.getService(IArtifactRepositoryManager.SERVICE_NAME));
        } else {
            throw new NoRepositoryConnectorException(repository);
        }
    }
}
