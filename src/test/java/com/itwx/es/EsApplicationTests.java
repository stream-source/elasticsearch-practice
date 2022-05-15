package com.itwx.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class EsApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    //请求选项：设置授权等
    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        //设置授权
        //builder.addHeader("Authorization", "Bearer " + TOKEN);
        //自定义消费响应
        //builder.setHttpAsyncResponseConsumerFactory(
        //new HttpAsyncResponseConsumerFactory
        //.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Test
    void contextLoads() {
    }

    /**
     * 文档存储格式可以支持JSON字符串，MAP对象，或者key-value参数值
     * @throws IOException
     */
    @Test
    void testDocument01() throws IOException {
        //建立索引请求
        IndexRequest request = new IndexRequest("posts");
        //文档id
        request.id("1");
        //JSON串
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);
        //索引响应
        IndexResponse index = client.index(request, COMMON_OPTIONS);
        System.out.println(index);
    }

    @Test
    void testDocument02() throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("1").source(jsonMap);
        //索引响应
        IndexResponse index = client.index(indexRequest, COMMON_OPTIONS);
        System.out.println(index);
    }

    @Test
    void testDocument03() throws IOException {
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("2")
                .source("user", "kimchy",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch");
        //索引响应
        IndexResponse index = client.index(indexRequest, COMMON_OPTIONS);
        System.out.println(index);
    }


    /**
     * 设置索引请求参数
     * @throws IOException
     */
    @Test
    void testDocument04() throws IOException {
        IndexRequest request = new IndexRequest("posts")
                .id("2")
                .source("user", "kimchy",
                        "message", "trying out Elasticsearch");
        request.routing("routing");
//        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        request.setRefreshPolicy("wait_for");
//        request.version(2);
//        request.opType(DocWriteRequest.OpType.CREATE);
//        request.opType("create");

        //索引响应
        IndexResponse index = client.index(request, COMMON_OPTIONS);
        System.out.println(index);
    }


    /**
     * 异步响应结果
     * @throws IOException
     */
    @Test
    void testDocument05() throws IOException {
        IndexRequest indexRequest = new IndexRequest("posts")
                .id("2")
                .source("user", "kimchy",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch");


        ActionListener<IndexResponse> listener = new ActionListener<>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        //异步监听响应结果
        Cancellable cancellable = client.indexAsync(indexRequest, COMMON_OPTIONS, listener);

    }

}
