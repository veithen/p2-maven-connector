# p2-maven-connector

[![Continuous Integration](https://github.com/veithen/p2-maven-connector/actions/workflows/ci.yml/badge.svg)](https://github.com/veithen/p2-maven-connector/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.veithen.maven/p2-maven-connector)](https://central.sonatype.com/artifact/com.github.veithen.maven/p2-maven-connector)
[![codecov](https://codecov.io/gh/veithen/p2-maven-connector/branch/master/graph/badge.svg)](https://codecov.io/gh/veithen/p2-maven-connector)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

**Use Eclipse p2 repositories (update sites) as ordinary Maven repositories.**

p2-maven-connector is a Maven build extension that lets a plain Maven build
resolve OSGi bundles directly from an [Eclipse p2](https://www.eclipse.org/equinox/p2/)
repository. You declare the p2 repository in your POM with `<layout>p2</layout>`,
add bundles as regular `<dependency>` entries, and Maven downloads them like any
other artifact.

You don't need Tycho, a target platform, or a step that mirrors bundles into a
Maven repository first.

## Why?

Many libraries in the Eclipse ecosystem are published only to p2 update sites,
not to Maven Central. If you want to use them from a normal Maven project (for
example to compile against an Eclipse API, run tests, or package a bundle in
something that isn't an Eclipse RCP application), you usually have to:

- switch the whole build to [Tycho](https://github.com/eclipse-tycho/tycho), or
- republish the bundles to a Maven repository yourself.

p2-maven-connector removes that step: Maven's resolver talks to the p2
repository directly.

## Quick start

Add the extension and a p2 repository to your `pom.xml`, then depend on bundles
using the `osgi.bundle` group ID:

```xml
<project>
    ...
    <dependencies>
        <dependency>
            <groupId>osgi.bundle</groupId>
            <artifactId>org.eclipse.core.runtime</artifactId>
            <version>[3.0,4.0)</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>eclipse-release</id>
            <layout>p2</layout>
            <url>https://download.eclipse.org/releases/2024-03/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

    <build>
        <extensions>
            <extension>
                <groupId>com.github.veithen.maven</groupId>
                <artifactId>p2-maven-connector</artifactId>
                <version>0.6.0</version>
            </extension>
        </extensions>
    </build>
</project>
```

The repository URL, bundle name and version range above are only an example.
Point it at the p2 repository you need. For reproducible builds, use an exact
bundle version such as `3.31.0.v20240215-1631` instead of a range.

## How artifacts are mapped

Maven and p2 identify artifacts differently. The connector maps between them as
follows:

| Maven coordinate                          | p2 artifact                                  |
|-------------------------------------------|----------------------------------------------|
| `groupId`                                 | p2 artifact classifier (only `osgi.bundle` is supported) |
| `artifactId`                              | bundle symbolic name                         |
| `version`                                 | OSGi bundle version (e.g. `1.2.3.v20240101`) |
| `classifier` = *(none)*                   | the bundle itself                            |
| `classifier` = `sources`                  | the source bundle (`<symbolic-name>.source`) |

Notes:

- **POMs are generated on the fly.** p2 repositories don't contain POM files,
  so the connector creates a minimal POM for each bundle. That POM declares
  **no dependencies**: you list every bundle you need yourself.
- **Version ranges work.** The connector builds `maven-metadata.xml` from the
  versions in the p2 repository, so ranges such as `[3.0,4.0)` or `[0,)` resolve.
- **Versions must be valid OSGi versions.** A Maven version that p2 can't parse
  is treated as not found.
- **Read-only.** Deploying to a p2 repository is not supported.
- **Pack200 isn't used.** Only plain `.jar` artifacts are downloaded;
  `.jar.pack.gz` variants are ignored.

## Proxies

The connector uses the proxy configured for the repository in Maven's
`settings.xml`, so builds behind a corporate HTTP proxy work without extra
setup. p2 mirror selection is turned off on purpose: the connector only
downloads from the URL you configured, so the build is predictable and doesn't
switch to mirrors that use protocols (such as FTP) your proxy settings don't
cover.

## Plain-HTTP repositories

Recent versions of p2 refuse to load repositories over unencrypted `http://` by
default. Use `https://` URLs where you can. If you must use a plain-HTTP
repository, allow it by setting the `p2.httpRule` system property, for example:

```bash
MAVEN_OPTS="-Dp2.httpRule=allow" mvn verify
```

## How it works

The connector registers a Maven Resolver `RepositoryConnectorFactory` named
`p2`. It handles every remote repository whose layout is `p2`.

Behind the scenes it runs the real Eclipse p2 and Equinox code inside Maven
through [Cosmos](https://github.com/veithen/cosmos), a lightweight OSGi
runtime. It doesn't reimplement the p2 repository format. Composite
repositories, compressed metadata (`.jar`/`.xz`) and other p2 features work
the same way they do in Eclipse. p2 metadata is cached under
`.p2-metadata` in your local Maven repository.

## Requirements

- Java 17 or later (CI builds run on Java 21 and 25)
- A recent Maven version (the current development version is built against
  Maven Resolver 2.x)

## Building from source

```bash
./mvnw verify
```

This runs the unit tests and the integration tests in `src/it`. The
integration tests start a local p2 repository and an HTTP proxy, then run
real Maven builds against them.

## Related projects

- [p2-publisher-maven-plugin](https://github.com/veithen/p2-publisher-maven-plugin):
  the reverse direction, publishing Maven artifacts as a p2 repository.
- [Cosmos](https://github.com/veithen/cosmos): the embedded OSGi runtime this
  connector is built on.

## Contributing

Bug reports and pull requests are welcome on
[GitHub](https://github.com/veithen/p2-maven-connector/issues).

## License

Licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
