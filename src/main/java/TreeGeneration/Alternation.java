package TreeGeneration;

import java.util.List;

public class Alternation implements IRule {
    public List<IRule> rules;

    public Alternation(List<IRule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "TreeGeneration.Alternation{" +
                "rules=" + rules +
                '}';
    }

    @Override
    public String getName() {
        return "Alternation";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return (T) visitor.visitAlternation(this);
    }
}
