package MeritService;

import java.math.BigDecimal;

import Model.Student;

public class MeritServ {

    private static final double MAX_ENTRANCE = 200.0;

    
    public double compute(BigDecimal hscPct, BigDecimal entranceScore) {
        double hsc = hscPct.doubleValue();
        double entrance = entranceScore.doubleValue();
        double normalizedEntrance = (entrance / MAX_ENTRANCE) * 100.0;
        double merit = 0.6 * hsc + 0.4 * normalizedEntrance;
        return Math.round(merit * 100.0) / 100.0;
    }

    
    public double compute(Student s) {
        return compute(s.getHscPercentage(), s.getEntranceScore());
    }
}
