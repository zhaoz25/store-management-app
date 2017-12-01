[1mdiff --cc Android/StoreManagement/app/src/main/AndroidManifest.xml[m
[1mindex 2d0cf2e,800cfca..0000000[m
[1m--- a/Android/StoreManagement/app/src/main/AndroidManifest.xml[m
[1m+++ b/Android/StoreManagement/app/src/main/AndroidManifest.xml[m
[36m@@@ -9,7 -9,7 +9,11 @@@[m
      <application[m
          android:allowBackup="true"[m
          android:debuggable="true"[m
[32m++<<<<<<< HEAD[m
[32m +        android:icon="@drawable/icon"[m
[32m++=======[m
[32m+         android:icon="@mipmap/ic_launcher"[m
[32m++>>>>>>> faaf0fa5fab433dc8b9c7544f10affe45587283d[m
          android:label="@string/app_name"[m
          android:roundIcon="@mipmap/ic_launcher_round"[m
          android:supportsRtl="true"[m
[1mdiff --git a/Android/StoreManagement/app/src/main/java/com/example/dellcorei3/storemanagement/employee/CustomListview/CT_hoadonAdapter.java b/Android/StoreManagement/app/src/main/java/com/example/dellcorei3/storemanagement/employee/CustomListview/CT_hoadonAdapter.java[m
[1mindex 2ac317e..189a908 100644[m
[1m--- a/Android/StoreManagement/app/src/main/java/com/example/dellcorei3/storemanagement/employee/CustomListview/CT_hoadonAdapter.java[m
[1m+++ b/Android/StoreManagement/app/src/main/java/com/example/dellcorei3/storemanagement/employee/CustomListview/CT_hoadonAdapter.java[m
[36m@@ -2,6 +2,7 @@[m [mpackage com.example.dellcorei3.storemanagement.employee.CustomListview;[m
 [m
 import android.app.Activity;[m
 import android.content.Context;[m
[32m+[m[32mimport android.graphics.Color;[m
 import android.view.LayoutInflater;[m
 import android.view.View;[m
 import android.view.ViewGroup;[m
[36m@@ -57,14 +58,17 @@[m [mpublic class CT_hoadonAdapter extends ArrayAdapter<Show_hoadon> {[m
         ct_billHolder.txt_ban.setText(pf.ten.toString());[m
         ct_billHolder.txt_nhanvien.setText(pf.tennv.toString());[m
         if(pf.trangthai.equals("chothanhtoan")){[m
[31m-          tt  = "Chờ thanh toán";[m
[32m+[m[32m            tt  = "Chờ thanh toán";[m
             ct_billHolder.txt_trangthai.setText(tt);[m
[32m+[m[32m            ct_billHolder.txt_trangthai.setBackgroundColor(Color.parseColor("#FA5858"));[m
         }else if(pf.trangthai.equals("chuaphucvu")){[m
             tt = "Chưa phục vụ";[m
             ct_billHolder.txt_trangthai.setText(tt);[m
[32m+[m[32m            ct_billHolder.txt_trangthai.setBackgroundColor(Color.parseColor("#FFBF00"));[m
         }else if (pf.trangthai.equals("phucvu")){[m
             tt="Phục vụ";[m
             ct_billHolder.txt_trangthai.setText(tt);[m
[32m+[m[32m            ct_billHolder.txt_trangthai.setBackgroundColor(Color.parseColor("#00FF80"));[m
         }[m
 [m
 [m
