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

import org.eclipse.core.runtime.IProgressMonitor;

final class SystemOutProgressMonitor implements IProgressMonitor {
    private boolean canceled;

    @Override
    public void beginTask(String name, int totalWork) {}

    @Override
    public void done() {}

    @Override
    public void internalWorked(double work) {}

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean value) {
        canceled = value;
    }

    @Override
    public void setTaskName(String name) {}

    @Override
    public void subTask(String name) {
        if (name.length() > 0) {
            System.out.println("  " + name);
        }
    }

    @Override
    public void worked(int work) {}
}
