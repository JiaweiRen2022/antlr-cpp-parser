package utils;

import antlr.cpp.CPP14Lexer;
import antlr.cpp.CPP14Parser;
import antlr.cpp.CPP14ParserBaseListener;
import metadata.TClass;
import metadata.TFile;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;


public class AntlrUtils {

    private static final Logger logger = LoggerFactory.getLogger(AntlrUtils.class);


    public static TFile defaultTypeParseFile(File file, Boolean logNode) throws IOException {
        CodePointCharStream charStream = CharStreams.fromString(FileUtils.readFileToString(file, "utf-8"));
        CPP14Lexer cpp14Lexer = new CPP14Lexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(cpp14Lexer);
        CPP14Parser cpp14Parser = new CPP14Parser(commonTokenStream);
        //error listener
        ErrorListenerImp errorListenerImp = new ErrorListenerImp();
        cpp14Parser.addErrorListener(errorListenerImp);
        //listener visitor
        LogicalNodeListenerImp listenerImp = new LogicalNodeListenerImp();
        listenerImp.setLogNode(logNode);
        //root node
        CPP14Parser.TranslationUnitContext root = cpp14Parser.translationUnit();
        ParseTreeWalker.DEFAULT.walk(listenerImp, root);

        //result
        TFile tFile = new TFile(file);
        tFile.setGlobalFunctions(listenerImp.getGlobalFunctions());
        tFile.setClasses(listenerImp.getClasses());
        tFile.setErrorList(errorListenerImp.getErrorDescs());
        return tFile;
    }





}
