package de.skaliant.wax.util.beans.exp;

import java.lang.reflect.Type;
import java.util.List;

import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
class ChainedExpression implements BeanExpression {
	private List<BeanExpression> chain;


	ChainedExpression(List<BeanExpression> chain) {
		this.chain = chain;
	}


	List<BeanExpression> getChain() {
		return chain;
	}


	@Override
	public boolean isIndexed() {
		return MiscUtils.getLastElementOf(chain).isIndexed();
	}


	@Override
	public Type getTypeIn(Object bean) {
		Object current = bean;

		for (int i = 0, max = chain.size() - 1; (i < max)
				&& (current != null); i++) {
			current = chain.get(i).readFrom(current);
		}
		if (current != null) {
			return MiscUtils.getLastElementOf(chain).getTypeIn(current);
		}

		return null;
	}


	@Override
	public Object readFrom(Object bean) {
		Object current = bean;

		for (int i = 0, max = chain.size(); (i < max) && (current != null); i++) {
			current = chain.get(i).readFrom(current);
		}

		return current;
	}


	@Override
	public void writeTo(Object bean, Object value) {
		Object current = bean;

		for (int i = 0, max = chain.size() - 1; (i < max)
				&& (current != null); i++) {
			current = chain.get(i).readFrom(current);
		}
		if (current != null) {
			MiscUtils.getLastElementOf(chain).writeTo(current, value);
		}
	}
}
