package io.github.artemy.osipov.thrift.bridge.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenRemoteRepositories;
import org.jboss.shrinkwrap.resolver.api.maven.repository.MavenUpdatePolicy;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ThriftModelArtifactRepository {

    private final ThriftModelArtifact artifact;
    private String version;
    private ClassLoader classLoader;

    public boolean hasArtifact() {
        return artifact != null;
    }

    @SneakyThrows
    public void fetchLatest() {
        var resolver = Maven.configureResolver()
                .withMavenCentralRepo(false);
        for (String repository : artifact.repositories) {
            resolver = resolver.withRemoteRepo(
                    MavenRemoteRepositories
                            .createRemoteRepository(repository, repository, "default")
                            .setUpdatePolicy(MavenUpdatePolicy.UPDATE_POLICY_ALWAYS)
            );
        }
        var resolvedArtifacts = resolver
                .resolve(artifact.groupId + ":" + artifact.artifactId + ":RELEASE")
                .withTransitivity()
                .asResolvedArtifact();
        version = Arrays.stream(resolvedArtifacts)
                .filter(resolved ->
                        artifact.groupId.equals(resolved.getCoordinate().getGroupId())
                                && artifact.artifactId.equals(resolved.getCoordinate().getArtifactId())
                )
                .findFirst()
                .orElseThrow()
                .getResolvedVersion();

        var urls = new URL[resolvedArtifacts.length];
        for (int i = 0; i < resolvedArtifacts.length; i++) {
            urls[i] = resolvedArtifacts[i].asFile().toURI().toURL();
        }

        classLoader = new URLClassLoader(
                urls,
                Thread.currentThread().getContextClassLoader()
        );
    }

    public record ThriftModelArtifact(
            List<String> repositories,
            String groupId,
            String artifactId
    ) {
    }
}
