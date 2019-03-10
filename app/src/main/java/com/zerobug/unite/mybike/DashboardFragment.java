package com.zerobug.unite.mybike;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.LinearGauge;
import com.anychart.enums.Anchor;
import com.anychart.enums.Orientation;
import com.anychart.enums.Position;
import com.anychart.scales.Base;
import com.anychart.scales.Linear;
import com.github.anastr.speedviewlib.SpeedView;


public class DashboardFragment extends Fragment {

    private View mDashboardView;
    private SpeedView speedoMeter;
    private AnyChartView anyChartView;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        speedoMeter = mDashboardView.findViewById(R.id.speedView);
        speedoMeter.speedTo(50, 4000);

        anyChartView = mDashboardView.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(mDashboardView.findViewById(R.id.progress_bar));

        LinearGauge linearGauge = AnyChart.linear();

        // TODO data
        linearGauge.data(new SingleValueDataSet(new Integer[] { 2 }));

        linearGauge.tooltip()
                .useHtml(true)
                .format(
                        "function () {\n" +
                                "          return this.value + '&deg;' + 'C' +\n" +
                                "            ' (' + (this.value * 1.8 + 32).toFixed(1) +\n" +
                                "            '&deg;' + 'F' + ')'\n" +
                                "    }");

        linearGauge.label(0).useHtml(true);
        linearGauge.label(0)
                .text("C&deg;")
                .position(Position.LEFT_BOTTOM)
                .anchor(Anchor.LEFT_BOTTOM)
                .offsetY("18px")
                .offsetX("36%")
                .fontColor("black")
                .fontSize(15);

        linearGauge.label(1)
                .useHtml(true)
                .text("F&deg;")
                .position(Position.RIGHT_BOTTOM)
                .anchor(Anchor.RIGHT_BOTTOM)
                .offsetY("18px")
                .offsetX("45%")
                .fontColor("black")
                .fontSize(15);

        Base scale = linearGauge.scale()
                .minimum(-30)
                .maximum(40);
//                .setTicks

        linearGauge.axis(0).scale(scale);
        linearGauge.axis(0)
                .offset("-1%")
                .width("0.5%");

        linearGauge.axis(0).labels()
                .format("{%Value}&deg;")
                .useHtml(true);

        linearGauge.thermometer(0)
                .name("Thermometer")
                .id(1);

        linearGauge.axis(0).minorTicks(true);
        linearGauge.axis(0).labels()
                .format(
                        "function () {\n" +
                                "    return '<span style=\"color:black;\">' + this.value + '&deg;</span>'\n" +
                                "  }")
                .useHtml(true);

        linearGauge.axis(1).minorTicks(true);
        linearGauge.axis(1).labels()
                .format(
                        "function () {\n" +
                                "    return '<span style=\"color:black;\">' + this.value + '&deg;</span>'\n" +
                                "  }")
                .useHtml(true);
        linearGauge.axis(1)
                .offset("3.5%")
                .orientation(Orientation.RIGHT);

        Linear linear = Linear.instantiate();
        linear.minimum(-20)
                .maximum(100);
//                .setTicks
        linearGauge.axis(1).scale(linear);

        anyChartView.setChart(linearGauge);

        return mDashboardView;
    }


}
