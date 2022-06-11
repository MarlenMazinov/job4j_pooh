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
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = map.get(source);
        if ("POST".equals(req.httpRequestType())) {
            if (topic != null) {
                topic.forEachValue(100, v -> v.add(req.getParam()));
                rsl = new Resp("", "200");
            }
        } else {
            map.putIfAbsent(source, new ConcurrentHashMap<>());
            map.get(source).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String text = map.get(source).get(req.getParam()).poll();
            rsl = text == null ? new Resp("", "204") : new Resp(text, "200");
        }
        return rsl;
    }
}