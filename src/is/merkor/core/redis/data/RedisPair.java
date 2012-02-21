/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.data;

import org.apache.commons.lang.builder.HashCodeBuilder;

import is.merkor.core.Item;
import is.merkor.core.Pair;

/**
 * An implementation of the {@link Pair} interface using a Redis
 * representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisPair implements Pair {
	
	Long id;
	RedisItem from;
	RedisItem to;
	
	public RedisPair (Long id, RedisItem from, RedisItem to) {
		if (null == id || null == from || null == to)
			throw new IllegalArgumentException("params must not be null!");
		
		this.id = id;
		this.from = from;
		this.to = to;
	}

	public RedisPair(RedisItem from, RedisItem to) {
		this.id = 0L;
		this.from = from;
		this.to = to;
	}

	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Pair#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Pair#getFrom()
	 */
	@Override
	public RedisItem getFrom() {
		return from;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Pair#getTo()
	 */
	@Override
	public RedisItem getTo() {
		return to;
	}

	/**
	 * Compares two pairs. The comparison is based on the from attributes, with these being equal, 
	 * the to attributes are compared, and at last, with these being equal, the id attributes are compared. 
	 * 
	 * @return the value 0 if the argument pair is equal to this wordPair; a value less than 0
	 * if this wordPair's from is less than the argument's from, or, these being equal, if this
	 * wordPair's to is less than the argument's to, or these being equal, if this wordPair's id is 
	 * less than the argument's id; and a value greater than 0 if this
	 * wordPair's from is greater than the argument's from, or, these being equal, if this
	 * wordPair's to is greater than the argument's to, or these being equal, if this wordPair's id is 
	 * greater than the argument's id;
	 */
	@Override 
	public int compareTo (Pair pair) {
		int result = getFrom().compareTo((RedisItem)pair.getFrom());
		if (result == 0)
			result = getTo().compareTo((RedisItem)pair.getTo());
		if (result == 0)
			result = getId().compareTo(pair.getId());
		
		return result;
	}
	
	/**
	 * Compares the specified object with this object for equality.
	 * Returns {@code true} if and only if the specified object is also
	 * a WordPair and {@code this.compareTo(wordPair2) == 0}.
	 * 
	 * @param obj the object to be compared for equality with this wordPair
	 * @return {@code true} if the specified object is equal to this wordPair, otherwise returns {@code false}
	 */
	@Override
	public boolean equals (Object obj) {
		if (this == obj)
			return true;
		if (null == obj)
			return false;
		if (!this.getClass().equals(obj.getClass()))
			return false;
		
		Pair pair = (Pair) obj;
		return compareTo(pair) == 0;
	}
	
	/**
	 * Returns the hash code value for this wordPair. 
	 * The hash code of a wordPair uses the hash code generator of {@link org.apache.commons.lang.builder.HashCodeBuilder}:
	 * <p>
	 * {@code hashCode = new HashCodeBuilder().append(getFrom()).append(getTo()).append(getId()).toHashCode())}
	 * 
	 * @return the hash code value for this wordPair
	 * 
	 */
	@Override
	public int hashCode () {
		return new HashCodeBuilder()
			.append(getFrom())
			.append(getTo())
			.append(getId())
			.toHashCode();
	}
	
}

