package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
@DisplayName("GradeCalculator классын нэгжийн тестүүд")
class GradeCalculatorTest {

    private GradeCalculator calc;

    @BeforeEach
    void beltgeh() {
        calc = new GradeCalculator();
    }

    @Test
    @DisplayName("90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)")
    void yesonAravA() {
        double onoo = 90.0;

        String dun = calc.letterGrade(onoo);

        assertEquals("A", dun);
    }

    @Test
    @DisplayName("89.99 оноо B дүн байх ёстой (A-гийн доод хязгаараас яг доор)")
    void aGiinDoorB() {
        double onoo = 89.99;

        String dun = calc.letterGrade(onoo);

        assertEquals("B", dun);
    }

    @Test
    @DisplayName("Ердийн утгууд: 95→A, 85→B, 75→C, 65→D, 30→F")
    void yerdiinUtguud() {
        double[] onoonuud = {95, 85, 75, 65, 30};
        String[] huleegdeh = {"A", "B", "C", "D", "F"};

        String[] bodit = new String[onoonuud.length];
        for (int i = 0; i < onoonuud.length; i++) {
            bodit[i] = calc.letterGrade(onoonuud[i]);
        }

        assertAll(
                () -> assertEquals(huleegdeh[0], bodit[0]),
                () -> assertEquals(huleegdeh[1], bodit[1]),
                () -> assertEquals(huleegdeh[2], bodit[2]),
                () -> assertEquals(huleegdeh[3], bodit[3]),
                () -> assertEquals(huleegdeh[4], bodit[4]));
    }

    @Test
    @DisplayName("60 нь D, 59.99 нь F байх ёстой (тэнцэх хязгаар)")
    void tentsehHyazgaar() {
        double tentssen = 60.0;
        double unasan = 59.99;

        String dun1 = calc.letterGrade(tentssen);
        String dun2 = calc.letterGrade(unasan);

        assertAll(
                () -> assertEquals("D", dun1),
                () -> assertEquals("F", dun2));
    }

    @Test
    @DisplayName("Хүрээний захын утгууд: 0→F, 100→A")
    void huree0ba100() {
        double dood = 0.0;
        double deed = 100.0;

        String dun1 = calc.letterGrade(dood);
        String dun2 = calc.letterGrade(deed);

        assertAll(
                () -> assertEquals("F", dun1),
                () -> assertEquals("A", dun2));
    }

    @Test
    @DisplayName("-1 оноо өгөхөд IllegalArgumentException шидэх ёстой")
    void sorogOnoo() {
        double onoo = -1.0;

        IllegalArgumentException aldaa = assertThrows(IllegalArgumentException.class,
                () -> calc.letterGrade(onoo));

        assertEquals("Оноо 0-100 хооронд байх ёстой: -1.0", aldaa.getMessage());
    }

    @Test
    @DisplayName("101 оноо өгөхөд IllegalArgumentException шидэх ёстой")
    void hetersenOnoo() {
        double onoo = 101.0;

        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(onoo));
    }

    @Test
    @DisplayName("Бүх хэсэг дээд оноотой бол нийлбэр 100 байх ёстой")
    void niilberZuv() {
        double irts = 10, lab = 40, soril1 = 10, soril2 = 10, shalgalt = 30;

        double niit = calc.totalScore(irts, lab, soril1, soril2, shalgalt);

        assertEquals(100.0, niit, 1e-9);
    }

    @Test
    @DisplayName("Ирц сөрөг (-5) бол totalScore exception шидэх ёстой")
    void irtsSorog() {
        double irts = -5;

        assertThrows(IllegalArgumentException.class,
                () -> calc.totalScore(irts, 30, 8, 8, 20));
    }

    @Test
    @DisplayName("Лабын оноо 41 буюу дээд хязгаараас хэтэрвэл exception шидэх ёстой")
    void labHetersen() {
        double lab = 41;

        IllegalArgumentException aldaa = assertThrows(IllegalArgumentException.class,
                () -> calc.totalScore(10, lab, 10, 10, 30));

        assertEquals("Лаб ба бие даалт 0-40 хооронд байх ёстой: 41.0", aldaa.getMessage());
    }

    @ParameterizedTest(name = "{0} оноо → {1}")
    @CsvSource({"95,A", "90,A", "89.99,B", "80,B", "79.99,C", "70,C", "69.99,D", "60,D", "59.99,F", "0,F", "100,A"})
    @DisplayName("letterGrade-ийн хязгаарын утгууд")
    void letterGradeHyazgaaruud(double onoo, String huleegdeh) {
        GradeCalculator tootsooluur = new GradeCalculator();

        String dun = tootsooluur.letterGrade(onoo);

        assertEquals(huleegdeh, dun);
    }

    @ParameterizedTest(name = "{0} оноо буруу оролт")
    @ValueSource(doubles = {-1, -0.01, 100.01, 101, Double.NaN})
    @DisplayName("letterGrade хүрээнээс гадуурх утгад exception шиднэ")
    void letterGradeBuruuOrolt(double onoo) {
        GradeCalculator tootsooluur = new GradeCalculator();

        assertThrows(IllegalArgumentException.class, () -> tootsooluur.letterGrade(onoo));
    }

    @ParameterizedTest(name = "{0}+{1}+{2}+{3}+{4} = {5}")
    @CsvSource({
            "10, 40, 10, 10, 30, 100",
            "0, 0, 0, 0, 0, 0",
            "8, 32.5, 7, 9, 21, 77.5",
            "10, 40, 10, 10, 0, 70",
            "5.5, 20.25, 3, 4, 15, 47.75"
    })
    @DisplayName("totalScore хүчинтэй утгуудын нийлбэрийг зөв тооцно")
    void totalScoreZuvNiilber(double irts, double lab, double soril1, double soril2, double shalgalt, double huleegdeh) {
        GradeCalculator tootsooluur = new GradeCalculator();

        double niit = tootsooluur.totalScore(irts, lab, soril1, soril2, shalgalt);

        assertEquals(huleegdeh, niit, 1e-9);
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}, {2}, {3}, {4}")
    @CsvSource({
            "-0.01, 40, 10, 10, 30",
            "10.01, 40, 10, 10, 30",
            "10, -1, 10, 10, 30",
            "10, 40.5, 10, 10, 30",
            "10, 40, -2, 10, 30",
            "10, 40, 11, 10, 30",
            "10, 40, 10, -3, 30",
            "10, 40, 10, 12, 30",
            "10, 40, 10, 10, -0.5",
            "10, 40, 10, 10, 31"
    })
    @DisplayName("totalScore хэсэг бүрийн сөрөг ба хэтэрсэн утгад exception шиднэ")
    void totalScoreBuruuOrolt(double irts, double lab, double soril1, double soril2, double shalgalt) {
        GradeCalculator tootsooluur = new GradeCalculator();

        assertThrows(IllegalArgumentException.class,
                () -> tootsooluur.totalScore(irts, lab, soril1, soril2, shalgalt));
    }
}
