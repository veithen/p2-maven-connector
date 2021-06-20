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
package com.github.veithen.maven.p2.connector;

import java.util.Locale;

import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.core.net.proxy.IProxyData;

final class AetherProxyDataAdapter implements IProxyData {
    private final Proxy proxy;

    AetherProxyDataAdapter(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public String getType() {
        return proxy.getType().toUpperCase(Locale.ENGLISH);
    }

    @Override
    public String getHost() {
        return proxy.getHost();
    }

    @Override
    public void setHost(String host) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPort() {
        return proxy.getPort();
    }

    @Override
    public void setPort(int port) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUserId() {
        Authentication auth = proxy.getAuthentication();
        if (auth == null) {
            return null;
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setUserid(String userid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPassword() {
        Authentication auth = proxy.getAuthentication();
        if (auth == null) {
            return null;
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setPassword(String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequiresAuthentication() {
        return proxy.getAuthentication() != null;
    }

    @Override
    public void disable() {
        throw new UnsupportedOperationException();
    }
}
