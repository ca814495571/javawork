package com.cqfc.samplemodule.client;

import com.cqfc.samplemodule.pool.ThriftClientPool;
import com.cqfc.samplemodule.protocol.SampleService;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class PoolClient {
    public static void main(String[] args) {
        try {

            GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
            poolConfig.maxActive = 80;
            poolConfig.minIdle = 5;
            poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
            poolConfig.testOnBorrow = true;
            poolConfig.testWhileIdle = true;
            poolConfig.numTestsPerEvictionRun = 10;
            poolConfig.maxWait = 3000;

            final ThriftClientPool<SampleService.Client> pool = new ThriftClientPool<SampleService.Client>(
                    new ThriftClientPool.ClientFactory<SampleService.Client>() {
                        @Override
                        public SampleService.Client make(TProtocol tProtocol) {
                            return new SampleService.Client(tProtocol);
                        }
                    }, poolConfig, "localhost", 10010);

            SampleService.Client javaClient = pool.getResource();
            System.out.println(javaClient.doService("Tom"));
            pool.returnResource(javaClient);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
