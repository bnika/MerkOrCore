package is.merkor.core.redis.dictionaries;

import is.merkor.core.util.MerkorLogger;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisConnectionCheck {
	
	private static Logger logger;
	
	public static boolean checkConnection (Jedis jedis)  {
		MerkorLogger.configureLogger();
		logger = Logger.getLogger(RedisConnectionCheck.class);
		try {
			jedis.ping();
			return true;
		} catch (JedisConnectionException e) {
			logger.error("Connection to Redis failed.\nPossible reasons:\n\ta) Redis server is not running" +
					"\n\tb) host and/or port are not correct.\n" + e.getMessage());
			return false;
		}
	}
}
