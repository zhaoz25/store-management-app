package com.example.dellcorei3.storemanagement.store.manager.controller;

import android.util.Log;

import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.example.dellcorei3.storemanagement.store.manager.models.EEChartData;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.HDChartData;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.example.dellcorei3.storemanagement.store.manager.models.LineChartData;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by DELL on 9/29/2017.
 */

public class ChartHandler {

    public static ArrayList<HDChartData> getHDForChart(ArrayList<HoaDon> alHD,int type){
        ArrayList<HDChartData> alChart = new ArrayList<>();
        ArrayList<HoaDon> alFilter = removeDuplicate(alHD);
        // tính tổng tiền cho từng ngày
        for(int i=0;i<alFilter.size();i++){
            HDChartData hd = new HDChartData();
            hd.timeData = alFilter.get(i).time;

            //cắt ngày để so sánh
            String time1 = hd.timeData.substring(0,10);
            for(int j=0;j<alHD.size();j++){
                String time2 = alHD.get(j).time.substring(0,10);
                // tính tổng tiền theo mỗi ngày
                if(time1.equals(time2) == true){
                    // loại bỏ hoadon chưa thanh toán
                    if(alHD.get(j).tongTien.equals("")==false && alHD.get(j).trangthai.equals("dathanhtoan") == true) {
                        hd.sumData += Integer.parseInt(alHD.get(j).tongTien);
                    }
                }
            }

            alChart.add(hd);
        }
        // sắp xếp mảng alchart theo tổng tiền
        Collections.sort(alChart, new Comparator<HDChartData>() {
            @Override
            public int compare(HDChartData o1, HDChartData o2) {
                return Integer.valueOf(o1.sumData).compareTo(o2.sumData);
            }
        });

        ArrayList<HDChartData> chartSorted = new ArrayList<>();
        int count = 0;
        // lấy 5 ngày có tổng tiền cao nhất
            // lấy ngày cao nhất
            if(type == 1) {
                for (int i = alChart.size() - 1; i >= 0; i--) {
                    chartSorted.add(alChart.get(i));
                    count++;

                }
            }
            else{
                for (int i = 0; i < alChart.size(); i++) {
                    chartSorted.add(alChart.get(i));
                    count++;

                }
            }


        return chartSorted;
    }

    private static ArrayList<HoaDon> removeDuplicate(ArrayList<HoaDon> alHD){
        ArrayList<HoaDon> alFilter = new ArrayList<>();
        ArrayList<HoaDon> al = new ArrayList<HoaDon>(alHD);
        //sorting the elements
        for(int i=0;i<al.size();i++)
        {
            for(int j=i;j<al.size();j++)
            {
                Long arrI = (Long)al.get(i).ngay.get("timestamp");
                Long arrJ = (Long)al.get(j).ngay.get("timestamp");
                if(arrI > arrJ)
                {
                    HoaDon temp=al.get(i);
                    al.set(i,al.get(j));
                    al.set(j,temp);
                }
            }
        }

        //After sorting
        for(int i=0;i<al.size();i++)
        {
            Log.d("sort",al.get(i).ngay.get("timestamp").toString());
            al.get(i).time = TimeFormat.convertTimeStampToDate(al.get(i).ngay);

        }

        int b = 0;
        al.set(b,al.get(0));
        // loại bỏ thành phần giống nhau theo ngày
        for(int i=0;i<al.size();i++)
        {
            String sB = al.get(b).time.substring(0,10);
            Log.d("a",sB);
            String sI = al.get(i).time.substring(0,10);
            if (sB.equals(sI) == false)
            {
                b++;
                al.set(b,al.get(i));
            }
        }

        for (int i=0;i<=b;i++ )
        {
            alFilter.add(al.get(i));
        }

        Log.d("sizeAL",alFilter.size()+"");
        return alFilter;
    }

    public static ArrayList<EEChartData> dataForPieChart(ArrayList<Employee> alEmp,ArrayList<HoaDon> alHoaDon){
        ArrayList<EEChartData> alChart = new ArrayList<>();

        for(int i=0;i<alEmp.size();i++){
            Employee emp = alEmp.get(i);
            EEChartData data = new EEChartData();
            data.name = emp.lastName;

            for(int j=0;j<alHoaDon.size();j++){
                HoaDon hd = alHoaDon.get(j);
                if(emp.id.equals(hd.nhanvien_id) == true){
                    data.value += 1;
                }
            }
            alChart.add(data);
        }

        return alChart;
    }

    public static ArrayList<LineChartData> dataForLineChart(ArrayList<HoaDon> alHoaDon){
        ArrayList<String> alHour = new ArrayList<>();
        ArrayList<LineChartData> alChart = new ArrayList<>();
        //tạo thời gian cho biểu đồ
        for(int i=1;i<24;i+=2) {
            if(i<10) {
                alHour.add("0"+i);
            }
            else{
                alHour.add(i+"");
            }
        }

        for(int i=0;i<alHour.size();i++) {
            LineChartData chart = new LineChartData();

            chart.name = alHour.get(i);
            int hourLabel = Integer.parseInt(chart.name);

            for(int j=0;j<alHoaDon.size();j++){
                String time = TimeFormat.convertTimeStampToDate(alHoaDon.get(j).ngay);
                int start = time.indexOf(" ")+1;
                int end = time.indexOf(":");
                int hourHD = Integer.parseInt(time.substring(start,end));
                // công tổng tiền mỗi hóa đơn vào giờ,(vd: 11h = hd từ 10->11)
                if(hourHD == (hourLabel-1) || hourHD == hourLabel){
                    if(alHoaDon.get(j).tongTien.equals("") == false) {
                        chart.value += Integer.parseInt(alHoaDon.get(j).tongTien);
                    }
                }
            }
            alChart.add(chart);
        }

        return alChart;
    }
}
