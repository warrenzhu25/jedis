package redis.clients.jedis;

import redis.clients.jedis.options.ClientOptions;
import redis.clients.jedis.util.ShardInfo;
import redis.clients.jedis.util.Sharded;

public class JedisShardInfo extends ShardInfo<Jedis> {
  private ClientOptions clientOptions;

  public JedisShardInfo(ClientOptions clientOptions) {
    super(Sharded.DEFAULT_WEIGHT);
    this.clientOptions = clientOptions;
  }

  public ClientOptions getClientOptions() {
    return clientOptions;
  }

  @Override
  public Jedis createResource() {
    return new Jedis(clientOptions);
  }

  @Override
  public String getName() {
    return clientOptions.getClientName();
  }

}
