package utils;

import antlr.cpp.CPP14Lexer;
import antlr.cpp.CPP14Parser;
import antlr.cpp.CPP14ParserBaseListener;
import entry.MainEntry;
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


public class AntlrUtils {

    private static final Logger logger = LoggerFactory.getLogger(AntlrUtils.class);


    public static TFile defaultTypeParseFile(File file) throws IOException {
        CodePointCharStream charStream = CharStreams.fromString(FileUtils.readFileToString(file, "utf-8"));
        CPP14Lexer cpp14Lexer = new CPP14Lexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(cpp14Lexer);
        CPP14Parser cpp14Parser = new CPP14Parser(commonTokenStream);
        //error listener
        ErrorListenerImp errorListenerImp = new ErrorListenerImp();
        cpp14Parser.addErrorListener(errorListenerImp);
        //listener visitor
        String relativePath = MainEntry.getRelativePath(file.getAbsolutePath());
        LogicalNodeListenerImp listenerImp = new LogicalNodeListenerImp(relativePath);

        //root node
        CPP14Parser.TranslationUnitContext root = cpp14Parser.translationUnit();
        ParseTreeWalker.DEFAULT.walk(listenerImp, root);

        //result
        TFile tFile = new TFile(file);
        tFile.setType(TFile.AnalyzeType.LOGICAL_NODE);
        tFile.setFunctions(listenerImp.getAllFunctions());
        tFile.setErrorList(errorListenerImp.getErrorDescs());
        return tFile;
    }




    public static void anilysisFile(File file, CPP14ParserBaseListener listener) throws IOException {
        anilysisFile(file, listener, null);
    }


    public static void anilysisFile(File file, CPP14ParserBaseListener listener, BaseErrorListener errorListener) throws IOException {
        String content = FileUtils.readFileToString(file, "utf-8");
        anilysisFile(content, listener, errorListener);
    }

    public static void anilysisFile(String content, CPP14ParserBaseListener listener, BaseErrorListener errorListener) throws IOException {
        CodePointCharStream charStream = CharStreams.fromString(content);
        CPP14Lexer cpp14Lexer = new CPP14Lexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(cpp14Lexer);
        CPP14Parser cpp14Parser = new CPP14Parser(commonTokenStream);
        if (errorListener != null)
            cpp14Parser.addErrorListener(errorListener);
        CPP14Parser.TranslationUnitContext root = cpp14Parser.translationUnit();
        ParseTreeWalker.DEFAULT.walk(listener, root);
    }



}
