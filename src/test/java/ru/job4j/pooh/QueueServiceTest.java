package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenPostThenGetFor3UsersQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod1 = "temperature=18";
        String paramForPostMethod2 = "temperature=19";
        String paramForPostMethod3 = "temperature=20";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod1)
        );
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod2)
        );
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod3)
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", "user1")
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", "user2")
        );
        Resp result3 = queueService.process(
                new Req("GET", "queue", "weather", "user3")
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is("temperature=19"));
        assertThat(result3.text(), is("temperature=20"));
    }

    @Test
    public void whenPost1ThenGetFor2UsersQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", "user1")
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", "user2")
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }
}