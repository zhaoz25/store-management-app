package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.ChartHandler;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.interface_controller.FragmentSendActivity;
import com.example.dellcorei3.storemanagement.store.manager.interface_controller.ISwipeTab;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.HoaDonAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.TimeKeepingAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.HDChartData;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RevenueActivity extends AppCompatActivity implements FragmentSendActivity {

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

    ArrayList<HoaDon> alHoaDon;

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

        //sự kiện swipe tab
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ISwipeTab fragment = (ISwipeTab) mSectionsPagerAdapter.instantiateItem(mViewPager, position);
                //neu tab 2, có dữ liệu thi thuc hien method
                if (fragment != null && position==1 && alHoaDon != null) {
                    fragment.displayChart(alHoaDon);
                }
                else if(position == 1 && alHoaDon == null){
                    Toast.makeText(RevenueActivity.this, "Hãy chọn thời gian muốn xem",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onSuccessData(ArrayList<HoaDon> al) {
        alHoaDon = al;
    }

    @Override
    public ArrayList<HoaDon> getData() {
        if(alHoaDon == null) {
            alHoaDon = new ArrayList<>();
        }
        return alHoaDon;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements ISwipeTab{
        View rootView;
        ProgressDialog progressDialog;
        // tab 1
        EditText etFrom,etTo;
        private DatabaseReference mDatabase;
        ListView lvRevenue;
        TextView tvTongHD,tvTongTien;

        ArrayList<HoaDon> alHoaDon;
        HoaDonAdapter adapterHD;
        Boolean checkFragment = false;
        // tab 2
        BarChart chart ;
        ArrayList<BarEntry> BARENTRY ;
        ArrayList<String> BarEntryLabels ;
        BarDataSet Bardataset ;
        BarData BARDATA ;
        RadioGroup rdGroup;
        RadioButton rdMax,rdMin;

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
            rootView = inflater.inflate(resourceIds[index], container, false);

            switch (index){
                case 0:
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    etFrom = (EditText)rootView.findViewById(R.id.etRevenueFrom);
                    etTo = (EditText)rootView.findViewById(R.id.etRevenueTo);
                    lvRevenue = (ListView)rootView.findViewById(R.id.lvRevenue);
                    tvTongHD = (TextView)rootView.findViewById(R.id.tvTongHoadon);
                    tvTongTien = (TextView)rootView.findViewById(R.id.tvTongTien);

                    registerForContextMenu(lvRevenue);

                    alHoaDon = new ArrayList<>();
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
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(etFrom.getText().toString().equals("") == false && etTo.getText().toString().equals("") == false) {
                                // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                                long from = TimeFormat.convertDateToSeconds(etFrom.getText().toString());
                                long to = TimeFormat.convertDateToSeconds(etTo.getText().toString()) + 85680000;

                                getHoaDon(from,to);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                    etTo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s.equals("") == false && etFrom.getText().toString().equals("") == false) {
                                // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                                long from = TimeFormat.convertDateToSeconds(etFrom.getText().toString());
                                long to = TimeFormat.convertDateToSeconds(etTo.getText().toString()) + 85680000;
                                Log.d("to",to+"");
                                getHoaDon(from,to);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });

                    lvRevenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent it = new Intent(getContext(),BillDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("hoadon",alHoaDon.get(position));

                            it.putExtra("data",bundle);
                            startActivity(it);
                        }
                    });
                    break;
                case 1:
                    chart = (BarChart)rootView.findViewById(R.id.chart1);

                    rdGroup = (RadioGroup)rootView.findViewById(R.id.rdGroupChart);
                    rdMax = (RadioButton)rootView.findViewById(R.id.rdChartMax);
                    rdMin = (RadioButton)rootView.findViewById(R.id.rdChartMin);

                    rdMax.setChecked(true);
                    rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            // lấy dữ liệu từ activity
                            alHoaDon = ((RevenueActivity)getActivity()).getData();
                            if(alHoaDon.size() > 0){
                                displayChart(alHoaDon);
                            }

                        }
                    });

                    break;
            }

            return rootView;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);

            MenuInflater inflater = this.getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_hoadon, menu);

            checkFragment = true;
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            if(checkFragment == true){
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                int position = info.position;

                checkFragment = false;
                switch (item.getItemId()){
                    case R.id.contextRemoveHD:
                        mDatabase.child("Hoadon").child(alHoaDon.get(position).hoadonId).removeValue();
                        mDatabase.child("Hoadon_chitiet").child(alHoaDon.get(position).hoadonId).removeValue();
                        // cập nhật listview
                        alHoaDon.remove(position);
                        adapterHD.notifyDataSetChanged();
                        // tính tổng hóa đơn
                        sum(alHoaDon);
                        Toast.makeText(getContext(), "Xóa thành công",
                                Toast.LENGTH_SHORT).show();

                        return true;
                    default:
                        return super.onContextItemSelected(item);
                }
            }
            else{
                return super.onContextItemSelected(item);
            }
        }

        public void AddValuesToBARENTRY(ArrayList<HDChartData> datas){
            for(int i=0;i<datas.size();i++){
                BARENTRY.add(new BarEntry((float)datas.get(i).sumData,i));
            }
        }

        public void AddValuesToBarEntryLabels(ArrayList<HDChartData> datas){

            for(int i=0;i<datas.size();i++){
                BarEntryLabels.add(datas.get(i).timeData.substring(0,5));
            }

        }

        private void getHoaDon(Long from,Long to){
            alHoaDon.clear();

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            Query query = mDatabase.child("Hoadon").orderByChild("ngay/timestamp").startAt(from).endAt(to);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            Log.d("data2",issue.getValue().toString());
                            HoaDon hd = issue.getValue(HoaDon.class);
                            hd.hoadonId = issue.getKey();
                            alHoaDon.add(hd);

                        }
                        //sắp xếp mảng hoadon, đã thanh toán ở trên
                        sort();
                        adapterHD = new HoaDonAdapter(getContext(),R.layout.listview_hoadon,alHoaDon);
                        lvRevenue.setAdapter(adapterHD);
                        adapterHD.notifyDataSetChanged();

                        // tính tổng hóa đơn
                        sum(alHoaDon);
                        //gửi dữ liệu cho activity
                        ((RevenueActivity)getActivity()).onSuccessData(alHoaDon);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Không có dữ liệu!",
                                Toast.LENGTH_SHORT).show();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        }

        private void sum(ArrayList<HoaDon> al){
            tvTongHD.setText(al.size()+"");

            int sum = 0;
            for(int i=0;i<al.size();i++){
                if(al.get(i).tongTien.equals("") == false && al.get(i).trangthai.equals("dathanhtoan") == true) {
                    sum += Integer.parseInt(al.get(i).tongTien);
                }
            }
            // currency format
            NumberFormat formatter = new DecimalFormat("#,###");
            String formatNumber = formatter.format(sum);
            tvTongTien.setText(formatNumber);
        }

        private void sort(){
            // sắp xếp mảng hoadon
            for(int i=0;i<alHoaDon.size();i++){
                for(int j=i;j<alHoaDon.size();j++){
                    String tt2 = alHoaDon.get(j).trangthai;

                    if(tt2.equals("dathanhtoan")){
                        HoaDon temp = alHoaDon.get(i);
                        alHoaDon.set(i,alHoaDon.get(j));
                        alHoaDon.set(j,temp);
                    }
                }
            }
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

        @Override
        public void displayChart(ArrayList<HoaDon> al) {
            if(chart != null) {
                // check radio button
                int type;
                if(rdMax.isChecked() == true){
                    type=1;
                }
                else{
                    type=0;
                }
                // lọc dữ liệu cho chart
                ArrayList<HDChartData> datas = ChartHandler.getHDForChart(al,type);

                BARENTRY = new ArrayList<>();
                BarEntryLabels = new ArrayList<String>();
                // tạo giá trị cho cột
                AddValuesToBARENTRY(datas);
                // tạo tên cho cột
                AddValuesToBarEntryLabels(datas);

                Bardataset = new BarDataSet(BARENTRY, "Tổng doanh thu theo ngày");

                BARDATA = new BarData(BarEntryLabels, Bardataset);

                Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                Bardataset.setValueTextSize(12);

                chart.setData(BARDATA);
                chart.notifyDataSetChanged(); // let the chart know it's data changed
                chart.invalidate();
                chart.setVisibleXRangeMaximum(5);
                chart.setVisibleXRangeMinimum(5);

                chart.animateY(3000);
            }

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
