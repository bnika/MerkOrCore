/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.dictionaries;

/**
 * All constants used in the Redis representation of the MerkOr data.
 * These are the key prefixes, followed by an id, a lemma, or a cluster name.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class MerkorRedisConstants {
	/**
	 * The prefix for lemma keys in MerkOr_IS_general.
	 * The value for a lemma key is a set
	 */
	public static final String LEMMA_KEY_PREFIX_IS = "merkor_is_lemma_";
	/**
	 * The prefix for lemma id keys in MerkOr_IS_general.
	 * The value for a lexical item key is a hash with the fields 
	 * 'lemma' and 'wordclass'.
	 */
	public static final String ID_KEY_PREFIX_IS = "merkor_is_id_";
	/**
	 * The prefix for relation id keys in MerkOr_IS_general.
	 * The value for a relation key is a hash with the fields
	 * 'from_item', 'to_item', 'relation', and 'score'. 
	 */
	public static final String REL_ID_PREFIX_IS = "merkor_is_rel_";
	/**
	 * The prefix for sorted relation sets in MerkOr_IS_general.
	 * The value for the lexical item key is a sorted set of relation ids
	 * with their confidence scores.
	 */
	public static final String SORTED_REL_SET_ID = "sorted_rel_set_";
	/**
	 * The prefix for relation type values in MerkOr_IS_general.
	 * The value of the relation type key is a string (= id of the relation type).
	 */
	public static final String REL_TYPE_PREFIX = "merkor_is_reltype_";
	/**
	 * The prefix for top relations of each type.
	 * The top relations value for relation type is a sorted set of relation ids,
	 * having confidence score as score.
	 */
	public static final String REL_TYPE_TOP_RELATIONS = "merkor_is_top_by_reltype_";
	
	/**
	 * The prefix for clusters in MerkOr_IS_general.
	 * The value for the cluster key is a set of lexical items ids.
	 */
	public static final String CLUSTER_ID_IS = "merkor_is_cluster_";
	
	/**
	 * The prefix for cluster element in MerkOr_IS_general.
	 * The value for the lexical item in_cluster is a set of cluster ids.
	 */
	public static final String IN_CLUSTER_IS = "in_cluster_";
}
