package com.example.android.sudoku_game_443;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public boolean Row(int i, int k, String n, String[][] m) {
        for (int j = 0; j < 9; j++) {
            if (m[i][j] == n && j != k) {
                return false;
            }
        }
        return true;
    }

    public boolean Col(int j, int k, String n, String[][] m) {
        for (int i = 0; i < 9; i++) {
            if (m[i][j] == n && i != k) {
                return false;
            }
        }
        return true;
    }

    // check the table for any repeated numbers in the table

    public boolean checktable(String[][] table) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Row(i, j, table[i][j], table) && Col(j, i, table[i][j], table)) {
                    list.add("true");
                } else {
                    list.add("false");
                }
            }
        }
        if (list.contains("false")) {
            return false;
        } else {
            return true;
        }
    }

    public class Game {
        int[] m[];
        int N; // # of col & row
        int q;
        int K; // empty places

        Game(int N, int K) {
            this.N = N;
            this.K = K;

            Double q1 = Math.sqrt(N);
            q = q1.intValue();

            m = new int[N][N];
        }

        public void enterValue() {
            enterNum();
            fillRem(0, q);
            removeDig();
        }

        void enterNum() {
            for (int i = 0; i < N; i = i + q) {
                fillPlace(i, i);
            }
        }

        boolean emptyPlace(int row2, int col2, int n) {
            for (int i = 0; i < q; i++) {
                for (int j = 0; j < q; j++) {
                    if (m[row2 + i][col2 + j] == n) {
                        return false;
                    }
                }
            }

            return true;
        }

        void fillPlace(int row1, int col1) {
            int num;
            for (int i = 0; i < q; i++) {
                for (int j = 0; j < q; j++) {
                    do {
                        num = randomGenerator(N);
                    } while (!emptyPlace(row1, col1, num));

                    m[row1 + i][col1 + j] = num;
                }
            }
        }

        int randomGenerator(int n) { // ()
            return (int) ((Math.random() * n + 1));
        }

        boolean Row1(int i, int n) {
            for (int j = 0; j < N; j++) {
                if (m[i][j] == n) {
                    return false;
                }
            }
            return true;
        }

        boolean Col1(int j, int n) { // (col1)
            for (int i = 0; i < N; i++) {
                if (m[i][j] == n) {
                    return false;
                }
            }
            return true;
        }

        boolean isAcceptable(int i, int j, int n) {
            return (Row1(i, n) // (col1)
                    && Col1(j, n)
                    && emptyPlace(i - i % q, j - j % q, n));
        }

        boolean fillRem(int i, int j) {
            if (j >= N && i < N - 1) {
                i = i + 1;
                j = 0;
            }
            if (i >= N && j >= N) {
                return true;
            }

            if (i < q) {
                if (j < q) {
                    j = q;
                }
            } else if (i < N - q) {
                if (j == (int) (i / q) * q) {
                    j = j + q;
                }
            } else {
                if (j == N - q) {
                    i = i + 1;
                    j = 0;
                    if (i >= N) {
                        return true;
                    }
                }
            }

            for (int num = 1; num <= N; num++) {
                if (isAcceptable(i, j, num)) {
                    m[i][j] = num;
                    if (fillRem(i, j + 1)) {
                        return true;
                    }

                    m[i][j] = 0;
                }
            }
            return false;
        }

        public void removeDig() {
            int count = K;
            while (count != 0) {
                int val = randomGenerator(N * N) - 1;
                int i = (val / N);
                int j = val % 9;
                if (j != 0) {
                    j = j - 1;
                }
                if (m[i][j] != 0) {
                    count--;
                    m[i][j] = 0;
                }
            }
        }
    }

    private class Cell {
        int value;
        boolean b;
        Button button;

        public Cell(int value0, Context THIS) {
            value = value0;
            b = value != 0;
            button = new Button(THIS);
            if (b) {
                button.setText(String.valueOf(value));
            } else {
                button.setText(" ");
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor(Color.DKGRAY);
//                button.setBackgroundColor(Color.parseColor("#816060"));
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (b) {
                        return;
                    }
                    value++;
                    if (value > 9) {
                        value = 1;
                    }
                    button.setText(String.valueOf(value));
                }
            });
        }
    }

    Cell[][] table;
    String input;
    TableLayout tableLayout;
    TextView textView;
    LinearLayout linearLayout;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int N = 9, K = 10;
        Game sudoku = new Game(N, K);
        sudoku.enterValue(); //0fill

        String[] split = new String[81]; // (c)

        table = new Cell[9][9];
        int c = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (sudoku.m[i][j] != 0) {
                    split[c] = String.valueOf(sudoku.m[i][j]);
                } else {
                    split[c] = "?";
                }
                c++;
            }
        }

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        int d = 0;
        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < 9; j++) {
                String s = split[d];
                d++;
                Character c1 = s.charAt(0);
                table[i][j] = new Cell(c1 == '?' ? 0 : c1 - '0', MainActivity.this);
                tableRow.addView(table[i][j].button);
            }
            tableLayout.addView(tableRow);
        }

        // Refresh Button
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setBackgroundColor(Color.BLACK);
        refresh.setTextColor(Color.WHITE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] split = new String[81];
                table = new Cell[9][9];
                tableLayout.removeAllViews();
                int N = 9, K = 10;
                Game sudoko = new Game(N, K);
                sudoko.enterValue();
                int c = 0; // (a)
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if (sudoko.m[i][j] != 0) {
                            split[c] = String.valueOf(sudoko.m[i][j]);
                        } else {
                            split[c] = "?";
                        }
                        c++;
                    }
                }
                int d = 0;
                for (int i = 0; i < 9; i++) {
                    TableRow tableRow = new TableRow(MainActivity.this);
                    for (int j = 0; j < 9; j++) {
                        String s = split[d];
                        d++;
                        Character cc = s.charAt(0);
                        table[i][j] = new Cell(cc == '?' ? 0 : cc - '0', MainActivity.this);
                        tableRow.addView(table[i][j].button);
                    }
                    tableLayout.addView(tableRow);
                }
            }
        });

        // check
        Button check = (Button) findViewById(R.id.check);
        check.setBackgroundColor(Color.BLACK);
        check.setTextColor(Color.WHITE);
        TextView textView = (TextView) findViewById(R.id.textView);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[][] tableList = new String[9][9];
//                String tes = " ";
                for (int n = 0; n < 9; n++) {
                    View a = tableLayout.getChildAt(n);
                    if (a != null && a instanceof TableRow) {
                        TableRow row = (TableRow) a;
                        for (int i = 0; i < row.getChildCount(); i++) {
                            View input = row.getChildAt(i);
                            if (input != null && input instanceof Button) {
                                try {
                                    Button But = (Button) input;
//                                    tes = tes + But.getText().toString();
                                    tableList[n][i] = But.getText().toString();

                                } catch (Exception e) {

                                }
                            }
                        }

                    }
                }

                if (checktable(tableList)) {
                    textView.setText(" You Won! ");
                    textView.setTextColor(Color.GREEN);
                    // call refresh to get new game
                } else {
                    textView.setText(" Try Again!! ");
                    textView.setTextColor(Color.RED);
                }

            }
        });

        // solve
        Button solve = (Button) findViewById(R.id.solve);
        solve.setBackgroundColor(Color.BLACK);
        solve.setTextColor(Color.WHITE);
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[][] tableList = new String[9][9];
//                String tes = " ";
                for (int n = 0; n < 9; n++) {
                    View a = tableLayout.getChildAt(n);
                    if (a != null && a instanceof TableRow) {
                        TableRow row = (TableRow) a;
                        for (int i = 0; i < row.getChildCount(); i++) {
                            View input = row.getChildAt(i);
                            if (input != null && input instanceof Button) {
                                try {
                                    Button button = (Button) input;
//                                    tes = tes + But.getText().toString();
                                    tableList[n][i] = button.getText().toString();

                                } catch (Exception e) {

                                }
                            }
                        }

                    }

                }

                String[] solve = new String[81];
                int z = 0;
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (tableList[i][j] == " " || !Row(i, j, tableList[i][j], tableList) || !Col(j, i, tableList[i][j], tableList)) {
                            for (int s = 1; s < 10; s++) {
                                String SolverValue = String.valueOf(s);
                                if (Row(i, j, SolverValue, tableList) && Col(j, i, SolverValue, tableList)) {
                                    tableList[i][j] = SolverValue;
                                }
                            }
                        }
                        solve[z] = tableList[i][j];
                        z++;

                    }
                }
                int d = 0;
                tableLayout.removeAllViews();
                for (int i = 0; i < 9; i++) {
                    TableRow trr = new TableRow(MainActivity.this);
                    for (int j = 0; j < 9; j++) {
                        String s = solve[d];
                        d++;
                        Character cc = s.charAt(0);
                        table[i][j] = new Cell(cc == '?' ? 0 : cc - '0', MainActivity.this);
                        trr.addView(table[i][j].button);
                    }
                    tableLayout.addView(trr);
                }
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
    }
}