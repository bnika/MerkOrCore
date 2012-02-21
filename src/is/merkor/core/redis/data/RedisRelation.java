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

import is.merkor.core.Relation;
import is.merkor.core.RelationType;

/**
 * An implementation of the {@link Relation} interface using a Redis 
 * representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisRelation implements Relation {
	
	private Long id;
	private RedisPair pair;
	private RelationType type;
	private Double confidence;
	
	public RedisRelation (RedisPair pair, RelationType type, Double confidence) {
		this(0L, pair, type, confidence);
	}
	public RedisRelation (Long id, RedisPair pair, RelationType type, Double confidence) {
		if (null == id || null == pair || null == type || null == confidence)
			throw new IllegalArgumentException("no param may be null!");
		
		this.id = id;
		this.pair = pair;
		this.type = type;
		this.confidence = confidence;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Relation#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Relation#getPair()
	 */
	@Override
	public RedisPair getPair() {
		return pair;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Relation#getType()
	 */
	@Override
	public RelationType getType() {
		return type;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Relation#setType(is.merkor.core.RelationType)
	 */
	@Override
	public void setType(RelationType type) {
		if (null == type)
			throw new IllegalArgumentException("param 'type' must not be null!");
		
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Relation#getConfidence()
	 */
	@Override
	public Double getConfidence() {
		return confidence;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Relation#getCertainty()
	 */
	@Override
	public int getCertainty() {
		// all relations saved in Redis
		// should have certainty 1
		return 1;
	}

	@Override
	public void setCertainty(int cert) {
		throw new UnsupportedOperationException("RedisRelations should have default certainty 1!");
		
	}


	/**
	 * Returns a string representation of this lexicalRelation.
	 * It is composed as follows: 
	 * <p>
	 * {@code "[" + this.getClass().getName() + leftLemma + relationTypeName + rightLemma + (confidence) + "]"}
	 * 
	 * @return a string representation of this cluster
	 */
	public String toString () {
		return "Relation: [" + pair.getFrom().getLemma() + " " + getType().getName() + " " + pair.getTo().getLemma() + " (" + getConfidence() + ")]"; 
	}
	
	/**
	 * Compares two relations. The comparison is based on the confidence attributes in descending order, with these being equal, 
	 * the pair attributes are compared, and at last, with these being equal, the id attributes are compared. 
	 * 
	 * @return the value 0 if the argument relation is equal to this lexicalRelation; a value less than 0
	 * if this lexicalRelation's confidence is GREATER than the argument's confidence, or, these being equal, if this
	 * lexcialRelation's pair is less than the argument's pair, or these being equal, if this lexicalRelation's id is 
	 * less than the argument's id; and a value greater than 0 if this
	 * lexicalRelation's confidence is LESS than the argument's confidence, or, these being equal, if this
	 * lexicalRelation's pair is greater than the argument's pair, or these being equal, if this lexicalRelation's id is 
	 * greater than the argument's id;
	 */
	@Override
	public int compareTo(Relation relation) {
		// comparison of confidence values should result in descending order,
		// so compareTo() is called in reverse order (relation to this)
		int result = relation.getConfidence().compareTo(getConfidence());
		if (result == 0)
			result = getPair().compareTo(relation.getPair());
		if (result == 0)
			result = getId().compareTo(relation.getId());
		return result;
	}
	
	/**
	 * Compares the specified object with this object for equality.
	 * Returns {@code true} if and only if the specified object is also
	 * a LexicalRelation and {@code this.compareTo(lexRelation2) == 0}.
	 * 
	 * @param obj the object to be compared for equality with this lexicalRelation
	 * @return {@code true} if the specified object is equal to this lexicalRelation, otherwise returns {@code false}
	 */
	@Override
	public boolean equals (Object obj) {
		if (this == obj)
			return true;
		if (null == obj)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final RedisRelation rel = (RedisRelation)obj;
		
		return compareTo(rel) == 0;
	}
	
	/**
	 * Returns the hash code value for this lexicalRelation. 
	 * The hash code of a lexicalRelation uses the hash code generator of {@link org.apache.commons.lang.builder.HashCodeBuilder}:
	 * <p>
	 * {@code hashCode = new HashCodeBuilder().append(getId()).append(getPair()).append(getConfidence()).toHashCode())}
	 * 
	 * @return the hash code value for this lexicalRelation
	 * 
	 */
	@Override
	public int hashCode () {
		return new HashCodeBuilder()
        .append(getId())
        .append(getPair())
        .append(getConfidence())
        .toHashCode();
	}	
}