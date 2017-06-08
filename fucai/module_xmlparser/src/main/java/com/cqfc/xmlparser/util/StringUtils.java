package com.cqfc.xmlparser.util;


public class StringUtils {
    
    /**
     * <p>Returns either the passed in String,
     * or if the obj is <code>null</code>, an empty String ("").</p>
     *
     * <pre>
     * StringUtils.getString(null)  = ""
     * StringUtils.getString("")    = ""
     * StringUtils.getString("bat") = "bat"
     * </pre>
     *
     * @see String#valueOf(Object)
     * @param obj  the String to check, may be null
     * @return the passed in String, or the empty String if it
     *  was <code>null</code>
     */
    public static String getString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
    
    /**
     * <p>Returns either the passed in String,
     * or if the obj is <code>null</code>, an empty String ("").</p>
     *
     * <pre>
     * StringUtils.getString(null)  = ""
     * StringUtils.getString("")    = ""
     * StringUtils.getString("bat") = "bat\n"
     * </pre>
     *
     * @see String#valueOf(Object)
     * @param obj  the String to check, may be null
     * @return the passed in String, or the empty String if it
     *  was <code>null</code>
     */
    public static String getStringWithNewLineChar(Object obj) {
        return obj == null ? "" : obj.toString() + "\n";
    }
}
