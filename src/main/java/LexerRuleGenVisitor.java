import TreeGeneration.Alternation;
import TreeGeneration.IRule;
import TreeGeneration.LexerRule;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LexerRuleGenVisitor extends ANTLRv4ParserBaseVisitor<List<IRule>> {
    public HashMap<String, List<IRule>> productions = new HashMap<>();


    @Override
    public List<IRule> visitLexerRuleSpec(ANTLRv4Parser.LexerRuleSpecContext ctx) {
        List<IRule> rules = addChildren(ctx);
        String label = ctx.start.getText();
        if (label.startsWith("fragment")) {
            label = ctx.children.get(1).getText();
        }
        productions.put(label, rules);
        return rules;
    }


    @Override
    public List<IRule> visitLexerRuleBlock(ANTLRv4Parser.LexerRuleBlockContext ctx) {
        return addChildren(ctx);
    }


    @Override
    public List<IRule> visitLexerAltList(ANTLRv4Parser.LexerAltListContext ctx) {
        ArrayList<IRule> rules = new ArrayList<>();
        ArrayList<IRule> altRules = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            List<IRule> childRules = visit(ctx.getChild(i));
            if (childRules != null)
                altRules.addAll(childRules);
        }
        if (altRules.size() > 1) {
            rules.add(new Alternation(altRules));
        } else {
            rules.addAll(altRules);
        }

        return rules;
    }


    @Override
    public List<IRule> visitLexerAlt(ANTLRv4Parser.LexerAltContext ctx) {
        ArrayList<IRule> rules = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String label = ctx.getChild(i).getText();
            if (label.startsWith("->"))
                continue;

            rules.add(new LexerRule(label));
        }
        return rules;
    }


    private List<IRule> addChildren(ParserRuleContext ctx) {
        ArrayList<IRule> rules = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            List<IRule> children = visit(ctx.getChild(i));
            if (children != null)
                rules.addAll(children);
        }
        return rules;
    }



    /*@Override
    public List<TreeGeneration.IRule> visitElement(ANTLRv4Parser.ElementContext ctx) {
        ArrayList<TreeGeneration.IRule> rules = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            rules.addAll(visitChildren(ctx));
        }
        return rules;
    }*/


}
