package utils;

import antlr.cpp.CPP14Parser;
import metadata.TClass;
import metadata.TFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionListenerImp extends ListenerImp{

    private static final Logger logger = LoggerFactory.getLogger(FunctionListenerImp.class);

    protected Set<TFunction> globalFunctions = new HashSet<TFunction>();

    protected TFunction currentFunc;


    //兼容历史commit不存在实际源文件情况
    public FunctionListenerImp(){}

    //TODO : 直接解析文件源码; 最好全部按commit处理, 最新版本取head ;
    // 不想改了QAQ 就这样吧   =.=!!
    public FunctionListenerImp(String filePath) {
        super(filePath);
    }

    public FunctionListenerImp(String filePath, boolean loggerNode) {
        super(filePath, loggerNode);
    }

    @Override
    public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        try {
            CPP14Parser.NoPointerDeclaratorContext noPointerDeclaratorContext = null;
            CPP14Parser.ParametersAndQualifiersContext params = null;
            //two rules
            if (ctx.declarator().pointerDeclarator()!=null){
                noPointerDeclaratorContext = ctx.declarator().pointerDeclarator()
                        .noPointerDeclarator().noPointerDeclarator();
                params = ctx.declarator().pointerDeclarator().noPointerDeclarator().parametersAndQualifiers();
            }else{
                noPointerDeclaratorContext = ctx.declarator().noPointerDeclarator();
                params = ctx.declarator().parametersAndQualifiers();
            }
            String functionName = noPointerDeclaratorContext.declaratorid().getText();
            TFunction function = new TFunction(functionName, currentClass);
            currentFunc = function;
            function.setContent(ctx.getText());
            if (currentClass != null && currentClass.getType() == TClass.TClassType.STRUCT)
                function.setPublic(true);
            setSourceLocation(function,ctx);
            //process function param
            CPP14Parser.ParameterDeclarationClauseContext parameterDeclarationClause = params.parameterDeclarationClause();
            if (parameterDeclarationClause != null){
                //have params
                List<CPP14Parser.ParameterDeclarationContext> parames = parameterDeclarationClause.parameterDeclarationList().parameterDeclaration();
                for (CPP14Parser.ParameterDeclarationContext paramDeclare : parames){
                    String type = paramDeclare.declSpecifierSeq().getText();
                    //python接口绑定属于c++ 扩展, 无name
                    if (paramDeclare.declarator()!= null)
                        function.addParam(type, paramDeclare.declarator().getText());
                }
            }
            if (currentClass != null) {
                currentClass.add(function);
            } else {
                globalFunctions.add(function);
            }
        }catch (Exception e){
            logger.error("<========= !! ERROR Parse function !!==========>",e);
            loggerNode(ctx);
        }

    }

    @Override
    public void exitFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        currentFunc = null;
    }

    public void resultPrint(){
        logger.info("=========== Global =============");
        Set<TFunction> functions = globalFunctions;
        for (TFunction tFunction : functions) {
            logger.info("#" + tFunction);
        }

        logger.info("=========== Classes =============");

        for (TClass klass : classes) {
            logger.info(klass.toString());
            logger.info("-----------------------");

            functions = klass.getFunctions();
            for (TFunction tFunction : functions) {
                logger.info(tFunction.toString());
            }

            logger.info("FINISHED");
        }
    }


    public List<TFunction> getAllFunctions(){
        List<TFunction> functions = new ArrayList<>();
        functions.addAll(globalFunctions);
        for (TClass klass : classes) {
            functions.addAll(klass.getFunctions());
        }
        return functions;
    }

    public Set<TFunction> getGlobalFunctions() {
        return globalFunctions;
    }

    public void setGlobalFunctions(Set<TFunction> globalFunctions) {
        this.globalFunctions = globalFunctions;
    }
}
