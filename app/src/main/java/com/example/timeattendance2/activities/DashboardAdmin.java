package com.example.timeattendance2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.timeattendance2.R;
import com.example.timeattendance2.model.DataFromServer;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    AnyChartView totalPieChart;
    Button signOutBtn;
    String[] units = {"unit1", "unit2", "unit3", "unit4"};
    int[] totals = {100, 200, 300, 400};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        setupPieChart2();
        dropDownConfig();
        dropDownReport();
        signOut();
    }
    public void setupPieChart2() {

        PieChart chart = (PieChart) findViewById(R.id.pie_chart);
        final ArrayList<DataFromServer> listStudent = DataFromServer.getSampleUnitData(5);
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (DataFromServer student : listStudent) {
            entries.add(new PieEntry(student.getTotals(), student.getUnits()));
        }

        PieDataSet dataset = new PieDataSet(entries, "Unit");

        chart.setUsePercentValues(true);
        dataset.setValueFormatter(new PercentFormatter());
        dataset.setSelectionShift(10);
        dataset.setValueTextSize(14);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextColor(Color.BLACK);

        chart.setCenterText("ยอดสุทธิ เดือน ตุลาคม");
        chart.setCenterTextSize(15);
        chart.setDrawEntryLabels(true);

        PieData data = new PieData(dataset);

        chart.setData(data);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);
        chart.getDescription().setEnabled(true);

        Description description = new Description();
        description.setText("ยอดสุทธิคิดเป็น %");
        chart.setDescription(description);

        chart.animateY(3000);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });
    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < units.length; i++) {
            dataEntries.add(new ValueDataEntry(units[i], totals[i]));
        }

        pie.title("ยอดสุทธิ เดือน ตุลาคม");
        pie.labels().position("outside");

        pie.legend()
                .position("left-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        pie.data(dataEntries);
        totalPieChart.setChart(pie);

    }

    public void signOut() {
        signOutBtn = findViewById(R.id.logOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenus();
            }
        });
    }

    public void mainMenus() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void dropDownConfig() {
        Spinner spinnerConfig = findViewById(R.id.configList);
        ArrayAdapter<CharSequence> adapterConfig = ArrayAdapter.createFromResource(this, R.array.configs, android.R.layout.simple_spinner_item);
        adapterConfig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConfig.setAdapter(adapterConfig);
        spinnerConfig.setOnItemSelectedListener(this);
    }

    public void dropDownReport() {
        Spinner spinnerReport = findViewById(R.id.reportList);
        ArrayAdapter<CharSequence> adapterReport = ArrayAdapter.createFromResource(this, R.array.reports, android.R.layout.simple_spinner_item);
        adapterReport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapterReport);
        spinnerReport.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        ArrayAdapter<CharSequence> adapterDaily;
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        if (parent.getItemAtPosition(position).equals("Payroll")) {
            Intent intent = new Intent(this, Payroll.class);
            startActivity(intent);
        }

        if (parent.getItemAtPosition(position).equals("Image")) {
            Intent intent = new Intent(this, ImageDaily.class);
            startActivity(intent);
        }

        if (parent.getItemAtPosition(position).equals("Data Log")) {
            Intent intent = new Intent(this, DataLog.class);
            startActivity(intent);
        }

        if (parent.getItemAtPosition(position).equals("Wage per site")) {
            Intent intent = new Intent(this, WagePerSite.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}