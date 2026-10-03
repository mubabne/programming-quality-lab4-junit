package mn.edu.must.sqat;

public class GradeCalculator {

    private static final double IRTS_DEED = 10.0;
    private static final double LAB_DEED = 40.0;
    private static final double SORIL1_DEED = 10.0;
    private static final double SORIL2_DEED = 10.0;
    private static final double SHALGALT_DEED = 30.0;

    public String letterGrade(double score) {
        if (Double.isNaN(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Оноо 0-100 хооронд байх ёстой: " + score);
        }
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }

    public double totalScore(double att, double lab, double quiz1, double quiz2, double exam) {
        shalgah("Ирц", att, IRTS_DEED);
        shalgah("Лаб ба бие даалт", lab, LAB_DEED);
        shalgah("Сорил 1", quiz1, SORIL1_DEED);
        shalgah("Сорил 2", quiz2, SORIL2_DEED);
        shalgah("Шалгалт", exam, SHALGALT_DEED);
        return att + lab + quiz1 + quiz2 + exam;
    }

    private void shalgah(String ner, double utga, double deed) {
        if (Double.isNaN(utga) || utga < 0 || utga > deed) {
            throw new IllegalArgumentException(ner + " 0-" + (int) deed + " хооронд байх ёстой: " + utga);
        }
    }
}
