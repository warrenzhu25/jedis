package redis.clients.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.options.ClientOptions;

import java.io.Closeable;
import java.util.Map;
import java.util.Set;

public abstract class JedisClusterConnectionHandler implements Closeable {
  protected final JedisClusterInfoCache cache;

  public JedisClusterConnectionHandler(Set<HostAndPort> nodes,
          final GenericObjectPoolConfig poolConfig, ClientOptions clientOptions) {
    this.cache = new JedisClusterInfoCache(poolConfig, clientOptions);
    initializeSlotsCache(nodes);
}

  abstract Jedis getConnection();

  abstract Jedis getConnectionFromSlot(int slot);

  public Jedis getConnectionFromNode(HostAndPort node) {
    return cache.setupNodeIfNotExist(node).getResource();
  }
  
  public Map<String, JedisPool> getNodes() {
    return cache.getNodes();
  }

  private void initializeSlotsCache(Set<HostAndPort> startNodes) {
    for (HostAndPort hostAndPort : startNodes) {
      Jedis jedis = null;
      try {
        jedis = new Jedis(ClientOptions.builder().withHostAndPort(hostAndPort).build());
        if (cache.getClientOptions().getPassword() != null) {
          jedis.auth(cache.getClientOptions().getPassword());
        }
        if (cache.getClientOptions().getClientName()!= null) {
          jedis.clientSetname(cache.getClientOptions().getClientName());
        }
        cache.discoverClusterNodesAndSlots(jedis);
        break;
      } catch (JedisConnectionException e) {
        // try next nodes
      } finally {
        if (jedis != null) {
          jedis.close();
        }
      }
    }
  }

  public void renewSlotCache() {
    cache.renewClusterSlots(null);
  }

  public void renewSlotCache(Jedis jedis) {
    cache.renewClusterSlots(jedis);
  }

  @Override
  public void close() {
    cache.reset();
  }
}
