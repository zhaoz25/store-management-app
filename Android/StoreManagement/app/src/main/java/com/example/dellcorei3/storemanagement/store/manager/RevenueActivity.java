package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RevenueActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    // Tab titles
    private String[] tabs = {"Danh sách", "Biểu đồ"};
    private TabLayout tabLayout;
    //Layout
    public static int[] resourceIds = {
            R.layout.fragment_revenue
            ,R.layout.fragment_revenue_chart
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //giasutinhoc.vn
        tabLayout = (TabLayout) findViewById(R.id.tabsRevenue);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_revenue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        // tab 1
        EditText etFrom,etTo;
        // tab 2
        BarChart chart ;
        ArrayList<BarEntry> BARENTRY ;
        ArrayList<String> BarEntryLabels ;
        BarDataSet Bardataset ;
        BarData BARDATA ;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(resourceIds[index], container, false);

            switch (index){
                case 0:
                    etFrom = (EditText)rootView.findViewById(R.id.etRevenueFrom);
                    etTo = (EditText)rootView.findViewById(R.id.etRevenueTo);
                    // sự kiện click edit text
                    etFrom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createTimePickerDialog(etFrom);
                        }
                    });
                    etTo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createTimePickerDialog(etTo);


                        }
                    });
                    // sự kiện thay đổi edittext
                    etFrom.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(etFrom.getText().toString().equals("") == false && etTo.getText().toString().equals("") == false) {
                                // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                                long from = TimeFormat.convertDateToSeconds(etFrom.getText().toString()) - 36000000;
                                long to = TimeFormat.convertDateToSeconds(etTo.toString()) + 46620000;


                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    etTo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s.equals("") == false && etFrom.getText().toString().equals("") == false) {
                                // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                                long from = TimeFormat.convertDateToSeconds(etFrom.getText().toString()) - 36000000;
                                long to = TimeFormat.convertDateToSeconds(etTo.toString()) + 46620000;


                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    break;
                case 1:
                    chart = (BarChart)rootView.findViewById(R.id.chart1);

                    BARENTRY = new ArrayList<>();
                    BarEntryLabels = new ArrayList<String>();

                    AddValuesToBARENTRY();

                    AddValuesToBarEntryLabels();

                    Bardataset = new BarDataSet(BARENTRY, "Projects");

                    BARDATA = new BarData(BarEntryLabels, Bardataset);

                    Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                    chart.setData(BARDATA);
                    chart.setVisibleXRangeMaximum(5);

                    chart.animateY(3000);


                    break;
            }

            return rootView;
        }

        public void AddValuesToBARENTRY(){

            BARENTRY.add(new BarEntry(2f, 0));
            BARENTRY.add(new BarEntry(4f, 1));
            BARENTRY.add(new BarEntry(6f, 2));
            BARENTRY.add(new BarEntry(8f, 3));
            BARENTRY.add(new BarEntry(7f, 4));
            BARENTRY.add(new BarEntry(3f, 5));
            BARENTRY.add(new BarEntry(10f, 6));
            BARENTRY.add(new BarEntry(15f, 7));
            BARENTRY.add(new BarEntry(6f, 8));

        }

        public void AddValuesToBarEntryLabels(){

            BarEntryLabels.add("1");
            BarEntryLabels.add("2");
            BarEntryLabels.add("3");
            BarEntryLabels.add("4");
            BarEntryLabels.add("5");
            BarEntryLabels.add("6");
            BarEntryLabels.add("7");
            BarEntryLabels.add("8");
            BarEntryLabels.add("9");

        }

        private void createTimePickerDialog(final EditText et){
            Calendar currentDate = Calendar.getInstance();
            DatePickerDialog date = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int yearP, int monthP, int dayOfMonth) {
                            et.setText(dayOfMonth + "/" + (monthP+1) + "/" + yearP);
                        }
                    },
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));
            date.show();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
