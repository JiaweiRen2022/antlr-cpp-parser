package utils;

import antlr.cpp.CPP14Lexer;
import antlr.cpp.CPP14Parser;
import antlr.cpp.CPP14ParserBaseListener;
import metadata.TClass;
import metadata.TElement;
import metadata.TElementLocation;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public abstract class ListenerImp extends CPP14ParserBaseListener {

    private static final Logger logger = LoggerFactory.getLogger(ListenerImp.class);

    protected TClass currentClass;

    protected boolean loggerNode = false;

    protected Set<TClass> classes = new HashSet<TClass>();

    protected String path;

    public ListenerImp(){}
    public ListenerImp(String filePath) {
        this.path = filePath;
    }

    public ListenerImp(String filePath, boolean loggerNode) {
        this.loggerNode = loggerNode;
        this.path = filePath;
    }


    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        //自孩子节点为一个时， 认为匹配子规则， 不输出；
        if (ctx.getChildCount() == 1)
            return;
        if (loggerNode)
            loggerNode(ctx);
    }


    @Override
    public void enterClassSpecifier(CPP14Parser.ClassSpecifierContext ctx) {
        try {
            if (ctx.classHead().classHeadName() == null)
                return;
            String name = ctx.classHead().classHeadName().getText();
            // 匿名结构体不处理
            if (name == null || name.trim().length() == 0) {
                logger.info("WARN: ClassName Empty ! CurrentLevel (" + ctx.depth() + ")");
                return;
            }
            currentClass = new TClass(name);

            setSourceLocation(currentClass, ctx);
            Token start = ctx.classHead().classKey().getStart();
            if (start.getType() == CPP14Lexer.Struct) {
                currentClass.setType(TClass.TClassType.STRUCT);
            }
            classes.add(currentClass);
        } catch (Exception e) {
            logger.error("<========= !! ERROR Parse Class !!==========>", e);
            loggerNode(ctx);
        }
    }

    @Override
    public void exitClassSpecifier(CPP14Parser.ClassSpecifierContext ctx) {
        //clean up the global variable
        currentClass = null;
    }


    protected void setSourceLocation(TElement element, ParserRuleContext node) {
        TElementLocation location = new TElementLocation();
        int startline = node.getStart().getLine();
        int endline = node.getStop().getLine();
        location.setStartline(startline);
        location.setEndline(endline);
        location.setPath(path);
        element.setLocation(location);
    }

    protected void loggerNode(ParserRuleContext ctx) {
        loggerNode("",ctx);
    }

    protected void loggerNode(String remark,ParserRuleContext ctx) {
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();
        String lineinfo = "(start:" + startLine + "," + endLine + ")";
        logger.info(remark + String.format("%1$" + ctx.depth() + "s", "-") + ctx.getClass().getSimpleName() + lineinfo + " -> [[ " + getConstrainedContent(ctx) + " ]]");
    }


    protected String getConstrainedContent(ParserRuleContext ctx) {
        String content = ctx.getText();
        return content.length() > 300 ? content.substring(0, 300) : content;
    }


    public Set<TClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<TClass> classes) {
        this.classes = classes;
    }


}
