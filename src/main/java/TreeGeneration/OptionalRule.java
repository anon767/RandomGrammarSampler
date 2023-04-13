package TreeGeneration;

public class OptionalRule implements IRule {
    public IRule rule;

    public OptionalRule(IRule rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "TreeGeneration.OptionalRule{" +
                "rule=" + rule +
                '}';
    }

    @Override
    public String getName() {
        return "Optional";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return (T) visitor.visitOptionalRule(this);
    }
}
