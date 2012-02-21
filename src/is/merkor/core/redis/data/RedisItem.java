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
import is.merkor.core.util.Wordclass;

/**
 * An implementation of the {@link Item} interface using a Redis
 * representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisItem implements Item, Comparable<Item> {
	
	Long id;
	String lemma;
	String wordclass;
	int sense;
	int wordpairCount; // number of relations on the redis server including this item 
	
	public RedisItem (Long id, String lemma, String wordclass) {
		this(id, lemma, wordclass, 1, 0);
	}
	public RedisItem (Long id, String lemma, String wordclass, int sense, int wordpairCount) {
		if (null == id || null == lemma)
			throw new IllegalArgumentException("params must not be null!");
		
		setWordclass(wordclass);
		this.id = id;
		this.lemma = lemma;
		this.sense = sense;
		this.wordpairCount = wordpairCount;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Item#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Item#getLemma()
	 */
	@Override
	public String getLemma() {
		return lemma;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Item#getSense()
	 */
	@Override
	public int getSense() {
		
		return 1;
	}
	private void setWordclass (String wc) {
		if (!Wordclass.VALUES.contains(wc)) 
			throw new IllegalArgumentException("non valid wordclass: " + wc);
		
		this.wordclass = wc;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Item#getWordclass()
	 */
	@Override
	public String getWordclass () {
		return wordclass;
	}

	@Override
	public boolean getHasMoreSenses() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.Item#getWordpairCount()
	 */
	@Override
	public int getWordpairCount() {
		return wordpairCount;
	}

	@Override
	public String getComment() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setComment(String comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getHumanCorrected() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Returns a String 'lemma_senseNr'
	 * 
	 * @return a String containing lemma and senseNr
	 */
	public String printLemmaAndSense () {
		return getLemma() + "_" + getSense();
	}
	/**
	 * Returns a String 'lemma (x)', where 'x' ist the
	 * first letter of wordclass: (n), (v), (a)
	 * 
	 * @return a String containing lemma and first letter of wordclass value
	 */
	public String printLemmaAndWordclass () {
		return getLemma() + " (" + getWordclass().charAt(0) + ")";
	}
	
	/**
	 * Returns a string representation of this lexicalItem.
	 * It is composed as follows:
	 * <p>
	 * {@code this.getClass().getName() + "[id=" + getId() + ", lemma=" + getLemma() + "_" + getSense() + ", wordclass=" + getWordclass() + "]"}
	 * 
	 * @return a string representation of this lexicalItem
	 */
	@Override
	public String toString () {
		return "lexical item: " + "[id=" + getId() + ", lemma=" + getLemma() + "_" + getSense() + ", wordclass=" + getWordclass() + "]";
		
	}
	
	/**
	 * Compares two lexicalItems.
	 * The comparison is primarily based on the lemma attributes, with these being equal, the sense
	 * attributes are compared and at last the id.
	 * In other words, the default sorting order of lexicalItems is lemma, then sense number
	 * and last id.
	 * 
	 * @return the value 0 if the argument lexicalItem is equal to this lexicalItem; a value less than 0
	 * if this lexicalItem's lemma is less than the argument's lemma, or, these being equal, if this
	 * lexicalItem's sense is less than the argument's sense, or, these also being equal, if this lexicalItem's
	 * id is less than the argument's id; and a value greater than 0 if this
	 * lexicalItem's lemma is greater than the argument's lemma, or, these being equal, if this
	 * lexicalItem's sense is greater than the argument's sense, or, these also being equal, if this lexicalItem's
	 * id is greater than the argument's id
	 */
	@Override
	public int compareTo (Item item) {
		int result = getLemma().compareTo(item.getLemma());
		if (result == 0) {
			if (getSense() < item.getSense())
				result = -1;
			else if (getSense() > item.getSense())
				result = 1;
			else
				result = 0;
		}
		if (result == 0)
			result = getId().compareTo(item.getId());
		
		return result;
	}
	
	/**
	 * Compares the specified object with this object for equality.
	 * Returns {@code true} if and only if the specified object is also
	 * a lexicalItem and 
	 * {@code this.compareTo(lexicalItem2) == 0}.
	 * 
	 * @param obj the object to be compared for equality with this lexicalItem
	 * @return {@code true} if the specified object is equal to this lexicalItem, {@code false} otherwise
	 */
	@Override
	public boolean equals (Object obj) {
		if (this == obj)
			return true;
		if (null == obj)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final RedisItem item = (RedisItem)obj;
		
		return compareTo(item) == 0;
	}
	
	/**
	 * Returns the hash code value for this lexicalItem. 
	 * The hash code of a lexicalItem uses the hash code generator of {@link org.apache.commons.lang.builder.HashCodeBuilder}:
	 * <p>
	 * {@code hashCode = new HashCodeBuilder().append(getId()).append(getLemma()).append(sense).toHashCode())}
	 * 
	 * @return the hash code value for this lexicalItem
	 * 
	 */
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .append(getLemma())
            .append(sense)
            .toHashCode();
    }
}
