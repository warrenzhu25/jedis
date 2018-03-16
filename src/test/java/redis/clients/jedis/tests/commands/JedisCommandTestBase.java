package redis.clients.jedis.tests.commands;

import org.junit.After;
import org.junit.Before;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.options.ClientOptions;
import redis.clients.jedis.tests.HostAndPortUtil;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

public abstract class JedisCommandTestBase {
  protected static HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);

  protected Jedis jedis;

  public JedisCommandTestBase() {
    super();
  }

  @Before
  public void setUp() throws Exception {
    jedis = new Jedis(ClientOptions.builder().withHostAndPort(hnp).withTimeout(500).build());
    jedis.connect();
    jedis.auth("foobared");
    jedis.flushAll();
  }

  @After
  public void tearDown() {
    jedis.disconnect();
  }

  protected Jedis createJedis() {
    Jedis j = new Jedis(ClientOptions.builder().withHostAndPort(hnp).build());
    j.connect();
    j.auth("foobared");
    j.flushAll();
    return j;
  }

  protected boolean arrayContains(List<byte[]> array, byte[] expected) {
    for (byte[] a : array) {
      try {
        assertArrayEquals(a, expected);
        return true;
      } catch (AssertionError e) {

      }
    }
    return false;
  }

  protected boolean setContains(Set<byte[]> set, byte[] expected) {
    for (byte[] a : set) {
      try {
        assertArrayEquals(a, expected);
        return true;
      } catch (AssertionError e) {

      }
    }
    return false;
  }
}
