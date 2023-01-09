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

import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;

import java.util.List;
import java.util.function.Predicate;

public final class Files2 implements PublicStaticHelper {

    /**
     * Accepts the content of a file containing glob patterns.
     */
    public static Predicate<String> globPatterns(final String fileContent) {
        final Predicate<String>[] predicate = new Predicate[]{
                Predicates.never()
        };

        final List<String> patterns = Lists.array();

        new TextFileWithCommentsVisitor() {

            @Override
            public void visitNonEmptyLine(final String pattern) {
                predicate[0] =
                        predicate[0].or(
                                CaseSensitivity.INSENSITIVE.globPattern(pattern, '\\')
                        );
                patterns.add(pattern);
            }

        }.accept(fileContent);

        return Predicates.customToString(
                predicate[0],
                String.join(" | ", patterns)
        );
    }

    private Files2() {
        throw new UnsupportedOperationException();
    }
}
