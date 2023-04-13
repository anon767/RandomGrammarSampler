import TreeGeneration.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParserRuleGenVisitor extends ANTLRv4ParserBaseVisitor<List<IRule>> {
    public HashMap<String, List<IRule>> productions = new HashMap<>();


    @Override
    public List<IRule> visitParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext ctx) {
        List<IRule> rules = addChildren(ctx);
        productions.put(ctx.start.getText(), rules);
        return rules;
    }


    @Override
    public List<IRule> visitRuleBlock(ANTLRv4Parser.RuleBlockContext ctx) {
        return addChildren(ctx);
    }

    @Override
    public List<IRule> visitExceptionGroup(ANTLRv4Parser.ExceptionGroupContext ctx) {
        return addChildren(ctx);
    }

    @Override
    public List<IRule> visitRuleAltList(ANTLRv4Parser.RuleAltListContext ctx) {
        return addChildren(ctx);
    }


    @Override
    public List<IRule> visitLabeledAlt(ANTLRv4Parser.LabeledAltContext ctx) {
        ArrayList<IRule> rules = new ArrayList<>();
        ArrayList<IRule> altRules = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            altRules.addAll(visit(ctx.getChild(i)));
        }
        if (altRules.size() > 1) {
            rules.add(new Alternation(altRules));
        } else {
            rules.addAll(altRules);
        }

        return rules;
    }


    @Override
    public List<IRule> visitAlternative(ANTLRv4Parser.AlternativeContext ctx) {
        ArrayList<IRule> rules = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String label = ctx.getChild(i).getText();

            Rule rule;
            if (Character.isUpperCase(label.charAt(0)))
                rule = new Rule(stripOffMOdifier(label));
            else
                rule = new LexerRef(stripOffMOdifier(label));

            if (label.endsWith("?"))
                rules.add(new OptionalRule(rule));
            else if (label.endsWith("*"))
                rules.add(new KleeneRule(rule));
            else
                rules.add(rule);
        }
        return rules;
    }

    private String stripOffMOdifier(String replace) {
        return replace.replace("*", "").replace("?", "");
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
