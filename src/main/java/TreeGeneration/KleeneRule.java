package TreeGeneration;

public class KleeneRule implements IRule{
    public IRule rule;
    public KleeneRule(IRule rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "TreeGeneration.KleeneRule{" +
                "rule=" + rule +
                '}';
    }

    @Override
    public String getName() {
        return "Kleene";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return (T) visitor.visitKleeneRule(this);
    }
}
