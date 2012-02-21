/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.data;

import is.merkor.core.Cluster;
import is.merkor.core.ClusterMember;
import is.merkor.core.Item;

/**
 * A cluster member is an {@link Item} connected with a {@link Cluster} it belongs
 * to. RedisClusterMember maps to the Redis representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 *
 */
public class RedisClusterMember implements ClusterMember {
	
	Item item;
	Cluster cluster;
	Double value;
	
	public RedisClusterMember (Item item, Cluster cluster, Double value) {
		if (null == item || null == cluster || null == value) 
			throw new IllegalArgumentException("params must not be null!");
		
		this.item = item;
		this.cluster = cluster;
		this.value = value;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMember#getItem()
	 */
	@Override
	public Item getItem() {
		return item;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMember#getCluster()
	 */
	@Override
	public Cluster getCluster() {
		return cluster;
	}
	/*
	 * (non-Javadoc)
	 * @see is.merkor.core.ClusterMember#getValue()
	 */
	@Override
	public Double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return item.toString() + ", " + cluster.toString() + ", value: " + value;
	}

}
