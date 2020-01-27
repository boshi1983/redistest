package redistest;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ZParams;

public class RedisTest {

	public static void main(String[] args) {
		java.util.List<Object> list = new ArrayList<Object>();
		
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.1.250");
        //jedis.auth("123");
        System.out.println(jedis.info("Server"));
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
        
        jedis.flushDB();
        
        list.add(jedis.set("test1", "1"));
        list.add(jedis.get("test1"));
        list.add(jedis.incr("test1"));
        list.add(jedis.get("test1"));
        list.add(jedis.set("test1", "10"));
        list.add(jedis.get("test1"));
        
        Map<String, Double> scoreMembers = new HashMap<String, Double>();
        scoreMembers.put("a", 1.0);
        scoreMembers.put("b", 2.0);
        scoreMembers.put("c", 3.0);
		list.add(jedis.zadd("test2", scoreMembers));
		list.add(jedis.zrange("test2", 0, -1));
		
		Map<String, Double> scoreMembers1 = new HashMap<String, Double>();
        scoreMembers1.put("c", 3.0);
        scoreMembers1.put("d", 5.0);
        scoreMembers1.put("e", 6.0);
		list.add(jedis.zadd("test3", scoreMembers1));
		list.add(jedis.zrange("test3", 0, -1));

		ZParams zParams = new ZParams();
        zParams.aggregate(ZParams.Aggregate.MAX);
		list.add(jedis.zunionstore("test4", zParams, "test2", "test3"));
		list.add(jedis.zrange("test4", 0, -1));

        System.out.println(list);
        
        jedis.select(1);
        jedis.flushDB();
        
        System.out.print(jedis.set("test1", "1")+", ");
        
        Pipeline pipeline = jedis.pipelined();
        
        pipeline.get("test1");
        pipeline.incr("test1");
        pipeline.get("test1");
        pipeline.set("test1", "10");
        pipeline.get("test1");
        
        Map<String, Double> scoreMembers2 = new HashMap<String, Double>();
        scoreMembers2.put("a", 1.0);
        scoreMembers2.put("b", 2.0);
        scoreMembers2.put("c", 3.0);
        pipeline.zadd("test2", scoreMembers2);
        pipeline.zrange("test2", 0, -1);
		
		Map<String, Double> scoreMembers3 = new HashMap<String, Double>();
		scoreMembers3.put("c", 3.0);
		scoreMembers3.put("d", 5.0);
		scoreMembers3.put("e", 6.0);
		pipeline.zadd("test3", scoreMembers3);
		pipeline.zrange("test3", 0, -1);

		ZParams zParams1 = new ZParams();
		zParams1.aggregate(ZParams.Aggregate.MAX);
		pipeline.zunionstore("test4", zParams1, "test2", "test3");
		pipeline.zrange("test4", 0, -1);
		
        List<Object> list1 = pipeline.syncAndReturnAll();
        System.out.println(list1);
        
        jedis.close();
        
    }
}