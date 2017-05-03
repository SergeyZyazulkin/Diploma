package zsp.diploma.mintriang.event;

public interface EventListener<T extends Event> {

    void onEvent(T event);
}
