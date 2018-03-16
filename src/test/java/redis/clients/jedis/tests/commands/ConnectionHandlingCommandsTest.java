package redis.clients.jedis.tests.commands;

import org.junit.Test;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.options.ClientOptions;
import redis.clients.jedis.tests.HostAndPortUtil;

import static org.junit.Assert.assertEquals;

public class ConnectionHandlingCommandsTest {
  private static HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);

  @Test
  public void quit() {
    Jedis jedis = new Jedis(ClientOptions.builder().withHostAndPort(hnp).build());
    assertEquals("OK", jedis.quit());
  }

  @Test
  public void binary_quit() {
    BinaryJedis bj = new BinaryJedis(ClientOptions.builder().withHostAndPort(hnp).build());
    assertEquals("OK", bj.quit());
  }
}