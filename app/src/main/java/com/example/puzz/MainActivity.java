package com.example.puzz;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout[] towers = new FrameLayout[3];

    ArrayList<Integer>[] rings = new ArrayList[3];

    TextView txtMoves, txtMessage;

    Button btnRestart;

    int moves = 0;
    int selectedTower = -1;

    int[] ringColors = {
            Color.parseColor("#FF5252"),
            Color.parseColor("#FFB300"),
            Color.parseColor("#00C853"),
            Color.parseColor("#2979FF"),
            Color.parseColor("#D500F9")
    };

    int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        towers[0] = findViewById(R.id.towerA);
        towers[1] = findViewById(R.id.towerB);
        towers[2] = findViewById(R.id.towerC);

        txtMoves = findViewById(R.id.txtMoves);
        txtMessage = findViewById(R.id.txtMessage);

        btnRestart = findViewById(R.id.btnRestart);

        for (int i = 0; i < 3; i++) {
            rings[i] = new ArrayList<>();
        }

        for (int i = 0; i < 3; i++) {
            final int position = i;

            towers[i].setOnClickListener(v ->
                    selectTower(position));
        }

        btnRestart.setOnClickListener(v -> resetGame());

        resetGame();
    }

    void resetGame() {

        for (int i = 0; i < 3; i++) {
            rings[i].clear();
        }

        rings[0].add(5);
        rings[0].add(4);
        rings[0].add(3);
        rings[0].add(2);
        rings[0].add(1);

        moves = 0;
        selectedTower = -1;

        txtMoves.setText("Moves: 0");
        txtMessage.setText("Select Tower A");

        drawTowers();
    }

    void selectTower(int tower) {

        if (selectedTower == -1) {

            if (rings[tower].isEmpty()) {
                txtMessage.setText("This tower is empty!");
                return;
            }

            selectedTower = tower;
            txtMessage.setText("Now select destination");

        } else {

            if (selectedTower == tower) {
                selectedTower = -1;
                txtMessage.setText("Selection cancelled");
                return;
            }

            ArrayList<Integer> source = rings[selectedTower];
            ArrayList<Integer> destination = rings[tower];

            int movingRing = source.get(source.size() - 1);

            if (!destination.isEmpty()) {

                int topRing =
                        destination.get(destination.size() - 1);

                if (movingRing > topRing) {

                    txtMessage.setText(
                            "Big ring cannot go over small ring!");

                    selectedTower = -1;
                    return;
                }
            }

            source.remove(source.size() - 1);
            destination.add(movingRing);

            moves++;

            txtMoves.setText("Moves: " + moves);

            selectedTower = -1;

            if (rings[2].size() == 5) {

                txtMessage.setText("🎉 YOU WIN! Moves: " + moves);

            } else {

                txtMessage.setText("Great! Make your next move.");
            }
        }

        drawTowers();
    }

    void drawTowers() {

        for (int t = 0; t < 3; t++) {

            FrameLayout tower = towers[t];
            tower.removeAllViews();

            tower.post(() -> {

                tower.removeAllViews();

                int width = tower.getWidth();

                // Pillar
                View pole = new View(this);

                GradientDrawable poleShape =
                        new GradientDrawable();

                poleShape.setColor(Color.parseColor("#AAB6C8"));
                poleShape.setCornerRadius(dp(8));

                pole.setBackground(poleShape);

                FrameLayout.LayoutParams poleParams =
                        new FrameLayout.LayoutParams(
                                dp(8), dp(210),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

                poleParams.bottomMargin = dp(15);

                tower.addView(pole, poleParams);

                // Base
                View base = new View(this);

                GradientDrawable baseShape =
                        new GradientDrawable();

                baseShape.setColor(Color.parseColor("#64748B"));
                baseShape.setCornerRadius(dp(8));

                base.setBackground(baseShape);

                FrameLayout.LayoutParams baseParams =
                        new FrameLayout.LayoutParams(
                                (int)(width * 0.9),
                                dp(12),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

                baseParams.bottomMargin = dp(8);

                tower.addView(base, baseParams);

                // Tower Name
                TextView label = new TextView(this);

                label.setText(new String[]{"A", "B", "C"}[
                        getTowerIndex(tower)]);

                label.setTextColor(Color.WHITE);
                label.setTextSize(18);
                label.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams labelParams =
                        new FrameLayout.LayoutParams(
                                -1, dp(35), Gravity.TOP);

                tower.addView(label, labelParams);

                // Rings
                int towerIndex = getTowerIndex(tower);

                for (int i = 0; i < rings[towerIndex].size(); i++) {

                    int size = rings[towerIndex].get(i);

                    View ring = new View(this);

                    GradientDrawable shape =
                            new GradientDrawable();

                    shape.setColor(ringColors[size - 1]);
                    shape.setCornerRadius(dp(12));

                    shape.setStroke(
                            dp(2),
                            Color.WHITE);

                    ring.setBackground(shape);

                    int ringWidth = (int)(
                            width * (0.88 - (5 - size) * 0.13));

                    FrameLayout.LayoutParams ringParams =
                            new FrameLayout.LayoutParams(
                                    ringWidth,
                                    dp(25),
                                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

                    ringParams.bottomMargin = dp(18 + i * 27);

                    tower.addView(ring, ringParams);
                }
            });
        }
    }

    int getTowerIndex(FrameLayout tower) {

        for (int i = 0; i < 3; i++) {
            if (towers[i] == tower) {
                return i;
            }
        }

        return 0;
    }
}
