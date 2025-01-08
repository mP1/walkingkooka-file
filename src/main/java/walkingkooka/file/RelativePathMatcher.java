/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An adapter that only matches paths under the provided parent that also have their relative path matched by the given {@link Predicate}.
 */
final class RelativePathMatcher implements PathMatcher {

    static RelativePathMatcher with(final Predicate<String> patterns,
                                    final Path parent) {
        return new RelativePathMatcher(
            Objects.requireNonNull(patterns, "patterns"),
            Objects.requireNonNull(parent, "parent")
        );
    }

    private RelativePathMatcher(final Predicate<String> patterns,
                                final Path parent) {
        this.patterns = patterns;
        this.parent = parent;
    }

    @Override
    public boolean matches(final Path test) {
        final Path parent = this.parent;

        return test.startsWith(parent) &&
            this.patterns.test(
                parent.relativize(test)
                    .toString()
                    .replace(File.separatorChar, '/')
            );
    }

    private final Predicate<String> patterns;
    private final Path parent;

    @Override
    public String toString() {
        return this.parent + " " + this.patterns;
    }
}
