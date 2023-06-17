package utils;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ErrorListenerImp extends BaseErrorListener {

    private static final Logger logger = LoggerFactory.getLogger(ErrorListenerImp.class);

    private ArrayList<String> errorDescs = new ArrayList<>();

    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String errorLog = "line " + line + ":" + charPositionInLine + " " + msg;
        errorDescs.add(errorLog);
        logger.error(errorLog);
    }

    public ArrayList<String> getErrorDescs() {
        return errorDescs;
    }

    public void setErrorDescs(ArrayList<String> errorDescs) {
        this.errorDescs = errorDescs;
    }
}
