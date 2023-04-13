package TreeGeneration;

public class Rule implements IRule {
    String name;

    public Rule(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TreeGeneration.Rule{" +
                "name='" + name + '\'' +
                '}';
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitRule(this);
    }
}
