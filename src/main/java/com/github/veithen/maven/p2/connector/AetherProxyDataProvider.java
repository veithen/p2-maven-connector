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

import org.eclipse.aether.repository.Proxy;
import org.eclipse.core.net.proxy.IProxyData;

import com.github.veithen.maven.p2.ProxyDataProvider;

final class AetherProxyDataProvider implements ProxyDataProvider {
    private final Proxy proxy;

    AetherProxyDataProvider(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public IProxyData getProxyData(String protocol) {
        return new AetherProxyDataAdapter(proxy);
    }

    @Override
    public int hashCode() {
        return proxy.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AetherProxyDataProvider
                && ((AetherProxyDataProvider) obj).proxy.equals(proxy);
    }
}
