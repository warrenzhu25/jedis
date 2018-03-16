package redis.clients.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.options.ClientOptions;

public class JedisPool extends JedisPoolAbstract {

  public JedisPool(final GenericObjectPoolConfig poolConfig, final ClientOptions options) {
    super(poolConfig, new JedisFactory(options));
  }

  public JedisPool(ClientOptions build) {
    super(new GenericObjectPoolConfig(), new JedisFactory(build));
  }

  @Override
  public Jedis getResource() {
    Jedis jedis = super.getResource();
    jedis.setDataSource(this);
    return jedis;
  }

  @Override
  protected void returnBrokenResource(final Jedis resource) {
    if (resource != null) {
      returnBrokenResourceObject(resource);
    }
  }

  @Override
  protected void returnResource(final Jedis resource) {
    if (resource != null) {
      try {
        resource.resetState();
        returnResourceObject(resource);
      } catch (Exception e) {
        returnBrokenResource(resource);
        throw new JedisException("Could not return the resource to the pool", e);
      }
    }
  }
}
