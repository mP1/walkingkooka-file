package walkingkooka.file;

import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

/**
 * Visitor that accepts file content and supports various visit methods for each of the types of line that may be encountered.
 */
public abstract class TextFileWithCommentsVisitor extends Visitor<String> {

    protected TextFileWithCommentsVisitor() {
        super();
    }

    @Override
    public final void accept(final String text) {
        Objects.requireNonNull(text, "text");

        try (final BufferedReader reader = new BufferedReader(new StringReader(text))) {
            for (; ; ) {
                final String line = reader.readLine();
                if (null == line) {
                    break; // EOF
                }
                this.traverse(line);
            }

        } catch (final IOException never) {
            throw new Error(never);
        }
    }

    private void traverse(final String line) {
        if (Visiting.CONTINUE == this.startVisitLine(line)) {
            final String trimmed = line.trim();
            if (trimmed.length() == 0) {
                this.visitEmptyLine();
            } else {
                if (trimmed.startsWith("#")) {
                    this.visitComment(trimmed.substring(1).trim());
                } else {
                    this.visitNonEmptyLine(trimmed); // TODO add support for un-escaping...
                }
            }
        }
        this.endVisitLine(line);
    }

    public Visiting startVisitLine(final String line) {
        return Visiting.CONTINUE;
    }

    public void endVisitLine(final String line) {
    }

    public void visitEmptyLine() {
        // nop
    }

    public void visitComment(final String comment) {
        // nop
    }

    public void visitNonEmptyLine(final String text) {
        // nop
    }
}
