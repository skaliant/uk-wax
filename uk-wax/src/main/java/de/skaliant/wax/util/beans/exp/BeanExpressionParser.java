package de.skaliant.wax.util.beans.exp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class BeanExpressionParser {
	private Pattern indexedPattern;


	public static BeanExpression parse(String str) {
		if (MiscUtils.isBlank(str)) {
			throw new IllegalArgumentException("Expression is empty");
		}
		return new BeanExpressionParser().parseInternal(str);
	}


	BeanExpression parseInternal(String str) {
		List<String> parts = MiscUtils.splitAtChar(str, '.');
		List<BeanExpression> chain = new ArrayList<>(parts.size());
		BeanExpression result;

		for (String part : parts) {
			chain.add(parseSingle(part));
		}
		if (chain.size() == 1) {
			result = chain.get(0);
		} else {
			result = new ChainedExpression(chain);
		}

		return result;
	}


	BeanExpression parseSingle(String str) {
		BeanExpression result;

		if (str.endsWith("]")) {
			result = parseIndexed(str);
		} else {
			result = new SimpleExpression(str);
		}

		return result;
	}


	IndexedExpression parseIndexed(String str) {
		IndexedExpression result;
		Pattern p = getIndexedPattern();
		Matcher m = p.matcher(str);

		if (m.matches()) {
			result = new IndexedExpression(m.group(1), Integer.parseInt(m.group(2)));
		} else {
			throw new RuntimeException("Invalid indexed property: \"" + str + '"');
		}

		return result;
	}


	private Pattern getIndexedPattern() {
		if (indexedPattern == null) {
			indexedPattern = Pattern.compile("^(.+)\\[(\\d+)\\]$");
		}

		return indexedPattern;
	}
}
