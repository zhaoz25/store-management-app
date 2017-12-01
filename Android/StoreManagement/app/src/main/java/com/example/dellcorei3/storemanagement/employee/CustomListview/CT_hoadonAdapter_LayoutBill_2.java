package com.example.dellcorei3.storemanagement.employee.CustomListview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 31/08/2017.
 */

public class CT_hoadonAdapter_LayoutBill_2 extends ArrayAdapter<CT_hoadon_layout_2> {
    Context context;
    int layoutResourceId;
    List<CT_hoadon_layout_2> data;
    ArrayList<CT_hoadon_layout_2> arrayList;
    DecimalFormat decimalFormat;

    public CT_hoadonAdapter_LayoutBill_2(Context context, int resource, List<CT_hoadon_layout_2> data) {
        super(context, resource, data);
        this.context = context;
        this.layoutResourceId = resource;// error ở đây
        this.data = data;
        arrayList = new ArrayList<CT_hoadon_layout_2>();
        arrayList.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CT_BillHolder ct_billHolder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            ct_billHolder = new CT_BillHolder();
            decimalFormat = new DecimalFormat("###,###,###");
            ct_billHolder.txt_tenmon = (TextView) row.findViewById(R.id.txt_id_tenmon);
            ct_billHolder.txt_tennhommonan = (TextView) row.findViewById(R.id.txt_id_tennhommon);

            ct_billHolder.txt_gia = (TextView)row.findViewById(R.id.txt_id_gia);

            row.setTag(ct_billHolder);
        } else {
            ct_billHolder = (CT_BillHolder) row.getTag();
        }
        CT_hoadon_layout_2 pf = data.get(position);

        ct_billHolder.txt_tenmon.setText(pf.thucdon_ten.toString());
        ct_billHolder.txt_tennhommonan.setText(pf.nhom_ten.toString());
        float gia = Float.parseFloat(pf.thucdon_gia);
        ct_billHolder.txt_gia.setText(decimalFormat.format(gia));


//        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview);
//        row.startAnimation(animation);
        return row;
    }
    class CT_BillHolder{
        TextView txt_tenmon,txt_tennhommonan,txt_gia;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public CT_hoadon_layout_2 getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //filter class
    public void filter(String chartext){
        chartext = removeAccent(chartext).toLowerCase(Locale.getDefault());

        data.clear();
        if (chartext.length() == 0){
            data.addAll(arrayList);

        }else {
            for (CT_hoadon_layout_2 ct : arrayList){

                if (removeAccent(ct.thucdon_ten).toLowerCase(Locale.getDefault()).contains(chartext)){
                    data.add(ct);
                }
            }
        }
        notifyDataSetChanged();
    }


    // Mang cac ky tu goc co dau
    private static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự', };

    // Mang cac ky tu thay the khong dau
    private static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u', };


     // Bo dau 1 ky tu

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }


     // Bo dau 1 chuoi

    public static String removeAccent(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }


}