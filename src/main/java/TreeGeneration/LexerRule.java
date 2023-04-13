package TreeGeneration;

public class LexerRule implements IRule {
    String name;

    public LexerRule(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TreeGeneration.LexerRule{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return (T) visitor.visitLexerRule(this);
    }
}
