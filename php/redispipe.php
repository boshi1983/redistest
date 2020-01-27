<?php
$redis = new REDIS();
$redis->pconnect('127.0.0.1');
$redis->auth('123');

$redis->flushDB();

$rt = [];

$rt[] = $redis->set('test1', '1');
$rt[] = $redis->get('test1');
$rt[] = $redis->incr('test1');
$rt[] = $redis->get('test1');
$rt[] = $redis->set('test1', '10');
$rt[] = $redis->get('test1');

$rt[] = $redis->zAdd('test2', [], 1, 'a', 2, 'b', 3, 'c');
$rt[] = $redis->zRange('test2', 0, -1, true);
$rt[] = $redis->zAdd('test3', [], 3, 'c', 5, 'd', 6, 'e');
$rt[] = $redis->zRange('test3', 0, -1, true);
$rt[] = $redis->zUnionStore('test4', ['test2', 'test3'], null, 'MAX');
$rt[] = $redis->zRange('test4', 0, -1, true);

var_dump($rt);

$rt = [];

$redis->select(1);
$redis->flushDB();

$redis->set('test1', '1');

$redis->multi(Redis::PIPELINE);

$redis->get('test1');
$redis->incr('test1');
$redis->get('test1');
$redis->set('test1', '10');
$redis->get('test1');

$redis->zAdd('test2', [], 1, 'a', 2, 'b', 3, 'c');
$redis->zRange('test2', 0, -1, true);
$redis->zAdd('test3', [], 3, 'c', 5, 'd', 6, 'e');
$redis->zRange('test3', 0, -1, true);
$redis->zUnionStore('test4', ['test2', 'test3'], null, 'MAX');
$redis->zRange('test4', 0, -1, true);

$rt = $redis->exec();

var_dump($rt);