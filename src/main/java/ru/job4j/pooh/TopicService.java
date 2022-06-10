package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private ConcurrentHashMap<String, ConcurrentHashMap<String,
            ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        String source = req.getSourceName();
        String status;
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = map.get(source);
        if ("POST".equals(req.httpRequestType())) {
            if (topic != null) {
                topic.forEachValue(100, v -> v.add(req.getParam()));
                status = "=200";
                rsl = new Resp("", status);
            }
        } else {
            if (topic == null) {
                map.put(source, new ConcurrentHashMap<>());
                map.get(source).put(req.getParam(), new ConcurrentLinkedQueue<>());
                status = "=204";
                rsl = new Resp("", status);
            } else {
                ConcurrentLinkedQueue<String> queue = topic.get(req.getParam());
                if (queue == null) {
                    topic.put(req.getParam(), new ConcurrentLinkedQueue<>());
                    status = "=204";
                    rsl = new Resp("", status);
                } else {
                    String text = queue.poll();
                    status = "=200";
                    rsl = new Resp(text, status);
                }
            }
        }
        return rsl;
    }
}