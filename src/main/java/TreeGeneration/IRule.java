package TreeGeneration;

public interface IRule {
    public abstract String getName();
    public abstract <T> T accept(Visitor<T> visitor);
}
