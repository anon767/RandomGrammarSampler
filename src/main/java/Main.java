import TreeGeneration.IRule;
import TreeGeneration.Rule;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            HashMap<String, List<IRule>> parserctx = parseParseFile();
            HashMap<String, List<IRule>> lexerctx = parseLexerFile();
            NaiveSampleStrategy naiveSampleStrategy = new NaiveSampleStrategy(parserctx, lexerctx);
            String r = new Rule("htmlDocument").accept(naiveSampleStrategy);
            //System.out.println(parserctx);
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, List<IRule>> parseParseFile() throws IOException {
        ANTLRv4Lexer lexer = new ANTLRv4Lexer(new ANTLRFileStream("HTMLParser.g4"));
        ANTLRv4Parser parser = new ANTLRv4Parser(new CommonTokenStream(lexer));
        ParseTree tree = parser.grammarSpec();
        //tree.accept(new ParserPrintVisitor());
        ParserRuleGenVisitor ruleGenerator = new ParserRuleGenVisitor();
        tree.accept(ruleGenerator);
        return ruleGenerator.productions;
    }

    private static HashMap<String, List<IRule>> parseLexerFile() throws IOException {
        ANTLRv4Lexer lexer = new ANTLRv4Lexer(new ANTLRFileStream("HTMLLexer.g4"));
        ANTLRv4Parser parser = new ANTLRv4Parser(new CommonTokenStream(lexer));
        ParseTree tree = parser.grammarSpec();

        LexerRuleGenVisitor ruleGenerator = new LexerRuleGenVisitor();
        tree.accept(ruleGenerator);
        return ruleGenerator.productions;
    }
}
