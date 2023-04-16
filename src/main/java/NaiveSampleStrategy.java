import TreeGeneration.*;
import com.mifmif.common.regex.Generex;

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
        boolean escaped = false;
        String label = lexerRule.getName();
        for (int i = 0; i < label.length(); i++) {
            if (!escaped && label.charAt(i) == '\'') {
                label = label.substring(0, i) + "(" + label.substring(i + 1);
                escaped = true;
            } else if (escaped && label.charAt(i) == '\'') {
                label = label.substring(0, i) + ")" + label.substring(i + 1);
                escaped = false;
            } else if (escaped && label.charAt(i) == '?') {
                label = label.substring(0, i) + "\\?" + label.substring(i + 1);
            } else if (escaped && label.charAt(i) == '[') {
                label = label.substring(0, i) + "\\[" + label.substring(i + 1);
            }  else if (escaped && label.charAt(i) == ']') {
                label = label.substring(0, i) + "\\]" + label.substring(i + 1);
            }  else if (!escaped && label.charAt(i) == '.') {
                String extended = "(" + getRandomLexerRule() + ")";
                if (extended.equals("( )"))
                    extended = "' '";
                label = label.substring(0, i) + extended + label.substring(i + 1);
            } else if (!escaped && label.startsWith("??", i)) {
                label = label.substring(0, i) + "?" + label.substring(i + 2);
            } else if (!escaped && label.startsWith("+?", i)) {
                label = label.substring(0, i) + "+" + label.substring(i + 2);
            } else if (!escaped && label.startsWith("*?", i)) {
                label = label.substring(0, i) + "*" + label.substring(i + 2);
            }

        }
        for (String k : this.lexerCtx.keySet()) {
            if (label.contains(k)) {
                StringBuilder tmp = new StringBuilder();
                for (IRule rule : this.lexerCtx.get(k))
                    tmp.append(rule.accept(this));
                label = label.replace(k, tmp);
            }
        }
        label = label.replace("\"", "\\\"").replace("<", "\\<").replace(">", "\\>").replace("#", "\\#");
        Generex generex = new Generex(label);
        String r = generex.random();
        return r;
    }


    private String getRandomLexerRule() {
        String random = (String) this.lexerCtx.keySet().toArray()[rand.nextInt(this.lexerCtx.keySet().size())];
        return new LexerRule(random).accept(this);
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
            try {
                r.append(rule.accept(this));
            } catch (StackOverflowError ignored) {
            }
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
