package pl.edu.pwr.nadolny.bumptempo.TempoBundle;

import java.util.LinkedList;

public class BpmCalculator {
    private static final Long MILLISECONDS_IN_A_MINUTE = 60000L;
    private static final Long MILISECONDS_LIMIT = 3000L;

    public LinkedList<Long> times;

    public BpmCalculator() {
        times = new LinkedList<>();
    }

    public void addPoint() {
        Long time = System.currentTimeMillis();
        if(isRecording() && time - times.getLast() > MILISECONDS_LIMIT){
            clearTimes();
        }
        times.add(time);
    }

    public int getBpm() {
        LinkedList<Long> deltas = getDeltas();
        return calculateBpm(deltas);
    }

    public void clearTimes() {
        times.clear();
    }

    public boolean isRecording() {
        return times.size() != 0;
    }

    public int getNumOfPoints(){
        return times.size();
    }

    //////////////// Prywatne metody //////////////////

    private int calculateBpm(LinkedList<Long> deltas) {
        Long sum = 0L;

        for (Long delta : deltas) {
            sum = sum + delta;
        }

        Long average = sum / deltas.size();

        return (int) (MILLISECONDS_IN_A_MINUTE / average);
    }

    private LinkedList<Long> getDeltas() {
        LinkedList<Long> deltas = new LinkedList<>();

        for (int i = 0; i < times.size() - 1; i++) {
            Long delta = times.get(i + 1) - times.get(i);
            deltas.add(delta);
        }

        return deltas;
    }
}
