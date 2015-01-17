package net.morilib.regex.dfa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParserUtils {

	//
	private static final Pattern MT_ATEND = Pattern.compile("\\G\\z");
	private static final Pattern MT_OTHERS = Pattern.compile(
			"\\G[^\\[\\]()\\\\]+");
	private static final Pattern MT_ESCAPE = Pattern.compile(
			"\\G\\\\.?", Pattern.DOTALL);
	private static final Pattern MT_LOOKAHEAD = Pattern.compile(
			"\\G\\(\\?([=!].*)\\)$");
	private static final Pattern MT_LPAREN = Pattern.compile(
			"\\G\\(");
	private static final Pattern MT_RPAREN = Pattern.compile(
			"\\G\\)");
	private static final Pattern MT_LBRACKET = Pattern.compile(
			"\\G\\[");
	private static final Pattern MT_RBRACKET = Pattern.compile(
			"\\G\\]");

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String[] splitLookahead(String s) {
		StringBuilder r = new StringBuilder();
		int pos = 0, dp = 0, db = 0;
		String a = null;
		Matcher m;

		m = MT_ATEND.matcher(s);
		while(pos < s.length()) {
			if(m.usePattern(MT_OTHERS).find(pos)) {
				r.append(m.group());
			} else if(m.usePattern(MT_ESCAPE).find(pos)) {
				r.append(m.group());
			} else if(dp <= 0 && db <= 0 &&
					m.usePattern(MT_LOOKAHEAD).find(pos)) {
				a = m.group(1);
			} else if(m.usePattern(MT_LPAREN).find(pos)) {
				r.append(m.group());
				if(db <= 0)  dp++;
			} else if(m.usePattern(MT_RPAREN).find(pos)) {
				r.append(m.group());
				if(db <= 0 && dp > 0)  dp--;
			} else if(m.usePattern(MT_LBRACKET).find(pos)) {
				r.append(m.group());
				db++;
			} else if(m.usePattern(MT_RBRACKET).find(pos)) {
				r.append(m.group());
				if(db > 0)  db--;
			} else {
				throw new RuntimeException();
			}
			pos = m.end();
		}
		return new String[] { r.toString(), a };
	}

}
