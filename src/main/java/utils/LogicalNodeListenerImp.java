package utils;

import antlr.cpp.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LogicalNodeListenerImp extends FunctionListenerImp{

    private static final Logger logger = LoggerFactory.getLogger(LogicalNodeListenerImp.class);

    public LogicalNodeListenerImp(){

    }

    public LogicalNodeListenerImp(String filePath) {
        super(filePath);
    }

    public LogicalNodeListenerImp(String filePath, boolean loggerNode) {
        super(filePath, loggerNode);
    }

    @Override
    public void enterSelectionStatement(CPP14Parser.SelectionStatementContext ctx) {
        if (ctx.Switch()!=null){
            List<CPP14Parser.StatementContext> statement =
                    ctx.statement().get(0).compoundStatement().statementSeq().statement();
            statement.forEach(s->{
                if (s.labeledStatement() != null)
                    selfIncrease(ctx);
            });
        }else if (ctx.If()!=null){
            selfIncrease(ctx);
            if (ctx.Else()!=null && ctx.statement(1).selectionStatement()==null){
                //存在else, 并且无后续的if
                selfIncrease(ctx);
            }
        }else{
            loggerNode("unknow SelectionStatement Node : {}",ctx);
        }
    }

    @Override
    public void enterIterationStatement(CPP14Parser.IterationStatementContext ctx) {
        selfIncrease(ctx);
    }

    @Override
    public void enterLogicalOrExpression(CPP14Parser.LogicalOrExpressionContext ctx) {
        if (ctx.OrOr().size()>0)
            selfIncrease(ctx);
    }

    @Override
    public void enterLogicalAndExpression(CPP14Parser.LogicalAndExpressionContext ctx) {
        if (ctx.AndAnd().size()>0)
            selfIncrease(ctx);
    }

    private void selfIncrease(ParserRuleContext ctx){
        if (currentFunc !=null){
            currentFunc.setComplexity(currentFunc.getComplexity() +1);
        }else{
            loggerNode("logical node has no current function! ",ctx);
        }
    }

}
