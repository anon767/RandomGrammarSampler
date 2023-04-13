public class ParserPrintVisitor extends ANTLRv4ParserBaseVisitor<Void> {


    @Override
    public Void visitParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext ctx) {
        System.out.println(ctx.start.getText());
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println("    -> " + ctx.getChild(i).getClass() + " " + ctx.getChild(i).getText());
            visitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitRuleBlock(ANTLRv4Parser.RuleBlockContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println("        -> " + ctx.getChild(i).getClass() + " " + ctx.getChild(i).getText());
            visitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitRuleAltList(ANTLRv4Parser.RuleAltListContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println("            -> " + ctx.getChild(i).getClass() + " " + ctx.getChild(i).getText());
            visitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitLabeledAlt(ANTLRv4Parser.LabeledAltContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println("              -> " + ctx.getChild(i).getClass() + " " + ctx.getChild(i).getText());
            visitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitAlternative(ANTLRv4Parser.AlternativeContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println("                -> " + ctx.getChild(i).getClass() + " " + ctx.getChild(i).getText());
            visitChildren(ctx);
        }
        return null;
    }

    /*@Override
    public Void visitElement(ANTLRv4Parser.ElementContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            System.out.println("                    -> " + ctx.getChild(i).getClass() + " " + ctx.getChild(i).getText());
            visitChildren(ctx);
        }
        return null;
    }*/


}
