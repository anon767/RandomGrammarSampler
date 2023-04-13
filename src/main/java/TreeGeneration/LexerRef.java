package TreeGeneration;

public class LexerRef extends Rule {
    public LexerRef(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "TreeGeneration.LexerRef{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return (T) visitor.visitLexerRef(this);
    }
}
