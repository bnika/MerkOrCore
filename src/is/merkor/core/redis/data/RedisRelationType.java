/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core.redis.data;

import is.merkor.core.RelationType;

/**
 * An implementation of the {@link RelationType} interface using a Redis
 * representation of the MerkOr data.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public class RedisRelationType implements RelationType {
	
	Long id;
	String name;
	String description;
	
	public RedisRelationType (Long id, String name, String description) {
		if (null == id || null == name || null == description)
			throw new IllegalArgumentException("no param may be null!");
		
		this.id = id;
		this.name = name;
		this.description = description;
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
	public String getDescription() {
		return description;
	}

}
