package com.google.refine.expr.functions.strings;

import java.text.Normalizer;
import java.util.Properties;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import com.google.refine.clustering.binning.FingerprintKeyer;
import com.google.refine.clustering.binning.Keyer;
import com.google.refine.grel.Function;
//import com.google.refine.grel.FunctionDescription;
//public class Normalize implements Function {
//    static Keyer fingerprint = new FingerprintKeyer();
//    @Override
//    public Object call(Properties bindings, Object[] args) {
//        if (args.length == 1 && args[0] != null) {
//            Object o = args[0];
//            String s = (o instanceof String) ? (String) o : o.toString();
//            String new_string= fingerprint.key(s);
//        }
//        return null;
//    }
//
//    @Override
//    public String getDescription() {
//        return null;
//    }
//
//    @Override
//    public String getReturns() {
//        return null;
//    }
//}
public class Normalize implements Function {
    static Keyer fingerprint = new FingerprintKeyer();

    static {
        // Initialize Jython
        PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);
    }

    private enum NormalizationForm {
        NFC, NFD, NFKC, NFKD, UNICODE
    }


    @Override
    public Object call(Properties bindings, Object[] args) {
        if (args.length == 1 && args[0] != null) {
            Object o = args[0];
            String s = (o instanceof String) ? (String) o : o.toString();
            // Apply fingerprint normalization
            String newString = fingerprint.key(s);
            // Apply additional normalization forms
            return normalize(newString, NormalizationForm.UNICODE);
        } else if (args.length == 2 && args[0] != null && args[1] != null) {
            String s = args[0].toString();
            String form = args[1].toString().toUpperCase();
            NormalizationForm normalizationForm = NormalizationForm.valueOf(form);
            return normalize(s, normalizationForm);
        }
        return null;
    }

    public static String normalize(String s, NormalizationForm form) {
        if (s == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }

        // Apply Unicode normalization
        switch (form) {
            case NFC:
                s = Normalizer.normalize(s, Normalizer.Form.NFC);
                break;
            case NFD:
                s = Normalizer.normalize(s, Normalizer.Form.NFD);
                break;
            case NFKC:
                s = Normalizer.normalize(s, Normalizer.Form.NFKC);
                break;
            case NFKD:
                s = Normalizer.normalize(s, Normalizer.Form.NFKD);
                break;
            case UNICODE:
                s = applyUnidecode(s);
                break;
        }

        return s;
    }

    private static String applyUnidecode(String s) {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            pyInterp.exec("import sys");
            pyInterp.exec("sys.path.append(r'E:\\jython2.7.1rc1\\Lib\\site-packages')");
            pyInterp.exec("from unidecode import unidecode");
            PyObject unidecode = pyInterp.get("unidecode");
            PyObject result = unidecode.__call__(new PyString(s));
            return result.toString();
        }
    }

    @Override
    public String getDescription() {
        return "Normalizes the string by removing diacritics, converting extended Western characters to their ASCII equivalents, and applying Unicode normalization forms.";
    }

    @Override
    public String getReturns() {
        return "string";
    }

    @Override
    public String getParams() {
        return "string s, [optional] string normalizationForm (NFC, NFD, NFKC, NFKD, UNICODE)";
    }
}
