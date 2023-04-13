package TreeGeneration;

public interface Visitor<T> {
    T visitAlternation(Alternation alternation);

    T visitKleeneRule(KleeneRule kleene);

    T visitLexerRef(LexerRef lexerRef);

    T visitLexerRule(LexerRule lexerRule);

    T visitOptionalRule(OptionalRule optionalRule);

    T visitRule(Rule rule);
}
