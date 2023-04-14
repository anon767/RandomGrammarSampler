import TreeGeneration.*;

import java.util.*;

public class NaiveSampleStrategy implements Visitor<String> {

    HashMap<String, List<String>> cache = new HashMap<>();
    HashMap<String, List<IRule>> ctx = new HashMap<>();
    HashMap<String, List<IRule>> lexerCtx;

    int cachSize = 50;
    Random rand = new Random();

    public NaiveSampleStrategy(HashMap<String, List<IRule>> parserCtx, HashMap<String, List<IRule>> lexerCtx) {
        ctx.putAll(parserCtx);
        ctx.putAll(lexerCtx);
        this.lexerCtx = lexerCtx;
    }

    private void addToCache(String rule, String sample) {
        if (cache.containsKey(rule)) {
            List<String> l = cache.get(rule);
            l.add(sample.hashCode() % cachSize, sample);
        } else {
            ArrayList<String> l = new ArrayList<>();
            l.add(sample);
            cache.put(rule, l);
        }
    }

    private String getFromCache(String rule) {
        return cache.getOrDefault(rule, null).get(rand.nextInt(cachSize));
    }

    @Override
    public String visitAlternation(Alternation alternation) {
        int randomDecision = rand.nextInt(alternation.rules.size());
        return alternation.rules.get(randomDecision).accept(this);
    }

    @Override
    public String visitKleeneRule(KleeneRule kleene) {
        int randomDecision = rand.nextInt(2);
        String r = kleene.rule.accept(this);
        if (randomDecision == 1) {
            r += kleene.accept(this);
        }
        return r;
    }


    @Override
    public String visitLexerRule(LexerRule lexerRule) {
        StringBuilder r = new StringBuilder();
        boolean escaped = false;
        String label = lexerRule.getName();
        for (int i = 0; i < label.length(); i++) {
            if (label.charAt(i) == '\'') {
                escaped = !escaped;
            } else if (escaped || label.charAt(i) == ' ') {
                r.append(label.charAt(i));
            } else {
                String tmp = label.substring(i);
                int next = tmp.indexOf('\'');

                if (next > -1) {
                    r.append(parseAntlrRegex(tmp.substring(0, next)));
                    i += next - 1;
                } else {
                    r.append(parseAntlrRegex(tmp));
                    break;
                }
            }
        }
        return r.toString();
    }

    private String parseAntlrRegex(String substring) {
        if (lexerCtx.containsKey(substring)) {
            return new LexerRef(substring).accept(this);
        }
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < substring.length(); i++) {
            if (substring.startsWith(".?")) {
                r.append(new OptionalRule(getRandomLexerRule()).accept(this));
                i += 1;
            } else if (substring.startsWith(".*")) {
                r.append(new KleeneRule(getRandomLexerRule()).accept(this));
                i += 1;
            } else if (substring.startsWith(".")) {
                r.append(getRandomLexerRule().accept(this));
            }

        }
        return r.toString();
    }

    private IRule getRandomLexerRule() {
        String random = (String) this.lexerCtx.keySet().toArray()[rand.nextInt(this.lexerCtx.keySet().size())];
        return new LexerRef(random);
    }

    @Override
    public String visitOptionalRule(OptionalRule optionalRule) {
        int randomDecision = rand.nextInt(2);
        if (randomDecision == 1) {
            return optionalRule.rule.accept(this);
        } else {
            return "";
        }
    }

    @Override
    public String visitLexerRef(LexerRef lexerRef) {
        StringBuilder r = new StringBuilder();
        for (IRule rule : ctx.getOrDefault(lexerRef.getName(), Collections.emptyList())) {
            r.append(rule.accept(this));
        }
        return r.toString();
    }

    @Override
    public String visitRule(Rule rule) {
        StringBuilder r = new StringBuilder();
        for (IRule tmp : ctx.get(rule.getName())) {
            r.append(tmp.accept(this));
        }
        return r.toString();
    }
}
