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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.text.CaseSensitivity;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class Files2Test implements PublicStaticHelperTesting<Files2> {

    @Test
    public void testGlobPatternsWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> Files2.globPatterns(null)
        );
    }

    @Test
    public void testGlobPatternsWithPatterns() {
        final String content = "# comment 1a\n" +
                "\n" +
                "path-to/file/*.txt\n";

        this.checkEquals(
                Lists.of(
                        CaseSensitivity.INSENSITIVE.globPattern("path-to/file/*.txt", '\\')
                ),
                Files2.globPatterns(content)
        );
    }

    @Override
    public Class<Files2> type() {
        return Files2.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return true;
    }
}
