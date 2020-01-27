# redistest
redis的通道测试

有同学说redis的管道内部，会把管道中的读写完全分开，也就是读写读写->写写读读。按照redis事务的定义，虽然没有原子性，但是必定顺序执行。
Talk is cheap, Show me the code.写个测试用例来佐证redis一定会严格执行通道顺序执行的规则不会修改顺序。

php的版本为5.6.40 cli模式和7.2.6 cli模式

java的jdk版本为1.8.0_241

以执行结果证明redis的管道模式严格执行通道顺序执行的规则
