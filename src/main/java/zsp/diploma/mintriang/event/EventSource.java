package zsp.diploma.mintriang.event;

public interface EventSource<T extends Event> {

    void addListener(EventListener<T> listener);
}
