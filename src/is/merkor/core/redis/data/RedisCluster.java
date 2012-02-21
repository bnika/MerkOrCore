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
import is.merkor.core.Item;

/**
 * An implementation of the {@link Cluster} interface using a Redis
 * representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisCluster implements Cluster {
	
	Long id;
	String name;
	Item center;
	
	public RedisCluster (Long id, String name) {
		if (null == id || null == name)
			throw new IllegalArgumentException("params must not be null!");
		
		this.id = id;
		this.name = name;
	}
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Item getCenter() {
		// TODO: implement real center of RedisCluster
		return new RedisItem(0L, "", "noun");
	}
	
	@Override
	public String toString() {
		return "cluster: " + getName() + " [id=" + getId() + "]";
	}
}
