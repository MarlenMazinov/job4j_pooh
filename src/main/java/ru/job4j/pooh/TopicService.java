package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String,
            ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = new Resp("", "501");
        String source = req.getSourceName();
        String param = req.getParam();
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = map.get(source);
        if ("POST".equals(req.httpRequestType())) {
            if (topic != null) {
                topic.forEachValue(100, v -> v.add(param));
                rsl = new Resp(param, "200");
            }
        } else if ("GET".equals(req.httpRequestType())) {
            map.putIfAbsent(source, new ConcurrentHashMap<>());
            map.get(source).putIfAbsent(param, new ConcurrentLinkedQueue<>());
            String text = map.get(source).get(param).poll();
            rsl = text == null ? new Resp("", "204") : new Resp(text, "200");
        }
        return rsl;
    }
}