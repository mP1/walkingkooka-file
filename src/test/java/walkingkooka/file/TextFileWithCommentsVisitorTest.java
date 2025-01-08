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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;
import walkingkooka.visit.VisitorTesting;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class TextFileWithCommentsVisitorTest implements VisitorTesting<TextFileWithCommentsVisitor, String> {

    @Test
    public void testSkipped() {
        final List<String> lines = Lists.array();

        new FakeTextFileWithCommentsVisitor() {
            @Override
            public Visiting startVisitLine(final String line) {
                lines.add(line);
                return Visiting.SKIP;
            }

            @Override
            public void endVisitLine(final String line) {
                // nop
            }

        }.accept(
            "# comment\n\n content 123"
        );

        this.checkEquals(
            Lists.of(
                "# comment", "", " content 123"
            ),
            lines
        );
    }

    @Test
    public void testCommentsTrimmed() {
        final String comment1 = "Comment-1";
        final String comment2 = "Comment-2";

        final List<String> comments = Lists.array();

        new FakeTextFileWithCommentsVisitor() {
            @Override
            public Visiting startVisitLine(final String line) {
                return Visiting.CONTINUE;
            }

            @Override
            public void endVisitLine(final String line) {
                // nop
            }

            @Override
            public void visitComment(final String comment) {
                comments.add(comment);
            }


        }.accept(
            "# " + comment1 + " \n" +
                "#  " + comment2 + "   \n"
        );

        this.checkEquals(
            Lists.of(
                comment1,
                comment2
            ),
            comments,
            "comments"
        );
    }

    @Test
    public void testNonEmptyTextTrimmed() {
        final String nonEmptyLine1 = "Comment-1";
        final String nonEmptyLine2 = "Comment-2";

        final List<String> nonEmptyLines = Lists.array();

        new FakeTextFileWithCommentsVisitor() {
            @Override
            public Visiting startVisitLine(final String line) {
                return Visiting.CONTINUE;
            }

            @Override
            public void endVisitLine(final String line) {
                // nop
            }

            @Override
            public void visitNonEmptyLine(final String content) {
                nonEmptyLines.add(content);
            }


        }.accept(
            " " + nonEmptyLine1 + " \n" +
                "  " + nonEmptyLine2 + "   \n"
        );

        this.checkEquals(
            Lists.of(
                nonEmptyLine1,
                nonEmptyLine2
            ),
            nonEmptyLines,
            "nonEmptyLines"
        );
    }

    @Test
    public void testFileWithCommentsEmptyLinesAndText() {
        final String comment1 = "Comment-1";
        final String comment2 = "Comment-2";

        final String nonEmptyLine1 = "Non Empty Text-1";
        final String nonEmptyLine2 = "Non Empty Text-2";

        final AtomicInteger emptyLines = new AtomicInteger();
        final List<String> comments = Lists.array();
        final List<String> nonEmptyLines = Lists.array();

        new TextFileWithCommentsVisitor() {
            @Override
            public void visitEmptyLine() {
                emptyLines.incrementAndGet();
            }

            @Override
            public void visitComment(final String comment) {
                comments.add(comment);
            }

            @Override
            public void visitNonEmptyLine(final String text) {
                nonEmptyLines.add(text);
            }
        }.accept(
            "#" + comment1 + "\n" +
                "\n" +
                '#' + comment2 + "\n" +
                nonEmptyLine1 + "\n" +
                "\n" +
                nonEmptyLine2
        );

        this.checkEquals(
            2,
            emptyLines.get(),
            "emptyLines"
        );
        this.checkEquals(
            Lists.of(
                comment1,
                comment2
            ),
            comments,
            "comments"
        );
        this.checkEquals(
            Lists.of(
                nonEmptyLine1,
                nonEmptyLine2
            ),
            nonEmptyLines,
            "nonEmptyLines"
        );
    }

    @Override
    public void testCheckToStringOverridden() {
        throw new UnsupportedOperationException();
    }

    // VisitingTesting..................................................................................................

    @Override
    public TextFileWithCommentsVisitor createVisitor() {
        return new FakeTextFileWithCommentsVisitor();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return Visitor.class.getSimpleName();
    }

    @Override
    public Class<TextFileWithCommentsVisitor> type() {
        return TextFileWithCommentsVisitor.class;
    }
}
