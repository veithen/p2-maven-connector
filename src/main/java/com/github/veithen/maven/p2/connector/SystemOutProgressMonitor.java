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

import org.eclipse.core.runtime.IProgressMonitor;

final class SystemOutProgressMonitor implements IProgressMonitor {
    private boolean canceled;

    public void beginTask(String name, int totalWork) {}

    public void done() {}

    public void internalWorked(double work) {}

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean value) {
        canceled = value;
    }

    public void setTaskName(String name) {}

    public void subTask(String name) {
        if (name.length() > 0) {
            System.out.println("  " + name);
        }
    }

    public void worked(int work) {}
}
