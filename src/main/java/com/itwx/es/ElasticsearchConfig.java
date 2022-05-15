package com.itwx.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxuan
 * @date 2022/5/9 9:05 下午
 * @describe
 */
@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;

    @Value("${elasticsearch.scheme}")
    private String scheme;

    /**
     * 创建es客户端，设置主机服务器
     *
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {

        RestClientBuilder builder = RestClient.builder(
                //单机部署
                new HttpHost(host, port, scheme)
        );

        return new RestHighLevelClient(builder);
    }

}
