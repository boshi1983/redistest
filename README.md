# redistest
redis的通道测试

有同学说redis的管道内部，会把管道中的读写完全分开，也就是读写读写->写写读读。按照redis事务的定义，虽然没有原子性，但是必定顺序执行。
Talk is cheap, Show me the code.写个测试用例来佐证redis一定会严格执行通道顺序执行的规则不会修改顺序。

php的版本为5.6.40 cli模式和7.2.6 cli模式

java的jdk版本为1.8.0_241

java的redis代码与php的redis代码，redis指令相同，顺序相同。

以执行结果证明redis的管道模式严格执行通道顺序执行的规则

php命令模式返回顺序：
array(12) {
  [0] => bool(true)
  [1] => string(1) "1"
  [2] => int(2)
  [3] => string(1) "2"
  [4] => bool(true)
  [5] => string(2) "10"
  [6] => int(3)
  [7] => array(3) {
    'a' => double(1)
    'b' => double(2)
    'c' => double(3)
  }
  [8] => int(3)
  [9] => array(3) {
    'c' => double(3)
    'd' => double(5)
    'e' => double(6)
  }
  [10] => int(5)
  [11] => array(5) {
    'a' => double(1)
    'b' => double(2)
    'c' => double(3)
    'd' => double(5)
    'e' => double(6)
  }
}

php redis通道模式返回顺序：
注：通道模式少一个，是我把第一个set命令拿到pipe外执行了，之后的执行顺序不变。
array(11) {
  [0] => string(1) "1"
  [1] => int(2)
  [2] => string(1) "2"
  [3] => bool(true)
  [4] => string(2) "10"
  [5] => int(3)
  [6] => array(3) {
    'a' => double(1)
    'b' => double(2)
    'c' => double(3)
  }
  [7] => int(3)
  [8] => array(3) {
    'c' => double(3)
    'd' => double(5)
    'e' => double(6)
  }
  [9] => int(5)
  [10] => array(5) {
    'a' => double(1)
    'b' => double(2)
    'c' => double(3)
    'd' => double(5)
    'e' => double(6)
  }
}

java jedis 指令模式，执行返回顺序：
[OK, 1, 2, 2, OK, 10, 3, [a, b, c], 3, [c, d, e], 5, [a, b, c, d, e]]

java jedis pipe模式，执行返回顺序：
OK, [1, 2, 2, OK, 10, 3, [a, b, c], 3, [c, d, e], 5, [a, b, c, d, e]]
