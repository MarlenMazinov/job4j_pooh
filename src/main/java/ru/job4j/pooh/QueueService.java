package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = new Resp("", "501");
        String source = req.getSourceName();
        String param = req.getParam();
        if ("POST".equals(req.httpRequestType())) {
            map.putIfAbsent(source, new ConcurrentLinkedQueue<>());
            rsl = map.get(source).add(param) ? new Resp(param, "200")
                    : new Resp("", "204");

        } else if ("GET".equals(req.httpRequestType())) {
            String text = map.getOrDefault(source, new ConcurrentLinkedQueue<>()).poll();
            if (text == null) {
                rsl = new Resp("", "204");
            } else {
                rsl = new Resp(text, "200");
            }
        }
        return rsl;
    }
}