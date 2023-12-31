package sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.tune.two.DummyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SqlJMH {

    @Benchmark
    public void tst1() {
        String strSQL = "";
        strSQL += "SELECT * ";
        strSQL += "FROM   (SELECT ROWNUM AS RNUM, ";
        strSQL += "               A.* ";
        strSQL += "        FROM   (SELECT * ";
        strSQL += "                FROM   TBL_BOARD ";
        strSQL += "                ORDER  BY BOARD_NO DESC) A) ";
        strSQL += "WHERE  RNUM BETWEEN ? AND ? ";

        // 이하 생략

    }

    @Benchmark
    public void tst2() {
        StringBuilder strSQL = new StringBuilder();
        strSQL.append("SELECT * ");
        strSQL.append("FROM   (SELECT ROWNUM AS RNUM, ");
        strSQL.append("               A.* ");
        strSQL.append("        FROM   (SELECT * ");
        strSQL.append("                FROM   TBL_BOARD ");
        strSQL.append("                ORDER  BY BOARD_NO DESC) A) ");
        strSQL.append("WHERE  RNUM BETWEEN ? AND ? ");


        // 이하 생략

    }
}
