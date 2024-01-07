package it.lanos.eventbuddy.util;

import java.util.Comparator;
import java.util.Date;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;

public class DateTimeComparator implements Comparator<EventWithUsers> {
    @Override
    public int compare(EventWithUsers obj1, EventWithUsers obj2) {
        Date dateTime1 = obj1.getEvent().getDateObject();
        Date dateTime2 = obj2.getEvent().getDateObject();

        // Null check e confronto
        if (dateTime1 == null || dateTime2 == null) {
            return 0; // Gestisci il caso in cui una delle date/ora è null
        }

        return dateTime1.compareTo(dateTime2);
    }

    @Override
    public Comparator<EventWithUsers> reversed() {
        return Comparator.super.reversed();
    }

    @Override
    public Comparator<EventWithUsers> thenComparing(Comparator<? super EventWithUsers> other) {
        return Comparator.super.thenComparing(other);
    }

    @Override
    public <U> Comparator<EventWithUsers> thenComparing(Function<? super EventWithUsers, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return Comparator.super.thenComparing(keyExtractor, keyComparator);
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<EventWithUsers> thenComparing(Function<? super EventWithUsers, ? extends U> keyExtractor) {
        return Comparator.super.thenComparing(keyExtractor);
    }

    @Override
    public Comparator<EventWithUsers> thenComparingInt(ToIntFunction<? super EventWithUsers> keyExtractor) {
        return Comparator.super.thenComparingInt(keyExtractor);
    }

    @Override
    public Comparator<EventWithUsers> thenComparingLong(ToLongFunction<? super EventWithUsers> keyExtractor) {
        return Comparator.super.thenComparingLong(keyExtractor);
    }

    @Override
    public Comparator<EventWithUsers> thenComparingDouble(ToDoubleFunction<? super EventWithUsers> keyExtractor) {
        return Comparator.super.thenComparingDouble(keyExtractor);
    }
}