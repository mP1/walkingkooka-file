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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RelativePathMatcherTest implements ClassTesting<RelativePathMatcher>, ToStringTesting<RelativePathMatcher> {

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(
            NullPointerException.class,
            () -> RelativePathMatcher.with(
                null,
                Paths.get("/var/temp")
            )
        );
    }

    @Test
    public void testWithNullParentPathFails() {
        assertThrows(
            NullPointerException.class,
            () -> RelativePathMatcher.with(
                Predicates.fake(),
                null
            )
        );
    }

    @Test
    public void testNotUnderParentFalse() {
        this.matchAndCheck(
            "*.txt",
            "/var/home/mP",
            "/different",
            false
        );
    }

    @Test
    public void testParentPathTrue() {
        this.matchAndCheck(
            "*",
            "/var/home/mP",
            "/var/home/mP",
            true
        );
    }

    @Test
    public void testChildPathTrue() {
        this.matchAndCheck(
            "*.txt",
            "/var/home",
            "/var/home/file.txt",
            true
        );
    }

    @Test
    public void testChildPathFalse() {
        this.matchAndCheck(
            "*.txt",
            "/var/home",
            "/var/home/file.wrong.ext",
            false
        );
    }

    @Test
    public void testDescendantPathTrue() {
        this.matchAndCheck(
            "**/*.txt",
            "/var/home",
            "/var/home/sub/file.txt",
            true
        );
    }

    @Test
    public void testDescendantPathFalse() {
        this.matchAndCheck(
            "**/*.txt",
            "/var/home",
            "/var/home/sub/file.wrong.ext",
            false
        );
    }

    private void matchAndCheck(final String glob,
                               final String parentPath,
                               final String test,
                               final boolean expected) {
        this.checkEquals(
            expected,
            RelativePathMatcher.with(
                Files2.globPatterns(
                    glob,
                    CaseSensitivity.SENSITIVE
                ),
                Paths.get(parentPath)
            ).matches(Paths.get(test)),
            () -> parentPath + " " + glob + " matches " + test
        );
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            RelativePathMatcher.with(
                Files2.globPatterns(
                    "*.txt\n*.ini",
                    CaseSensitivity.SENSITIVE
                ),
                Paths.get("/var/home")
            ),
            "/var/home *.txt | *.ini"
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<RelativePathMatcher> type() {
        return RelativePathMatcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
