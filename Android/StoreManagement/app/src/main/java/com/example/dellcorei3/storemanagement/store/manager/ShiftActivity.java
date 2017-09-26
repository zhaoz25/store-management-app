package com.example.dellcorei3.storemanagement.store.manager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.ShiftAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ShiftActivity extends AppCompatActivity {

    EditText etShiftName,etFrom,etTo;
    Button btAddShift;

    private DatabaseReference mDatabase;
    Calendar currentTime = Calendar.getInstance();

    ListView lvShift;
    ArrayList<Shift> alShift;
    ShiftAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alShift = new ArrayList<Shift>();
        adapter = new ShiftAdapter(this,R.layout.listview_shift,alShift);
        // lay du lieu cho mang
        createListViewData();
        // set adapter cho listview
        lvShift.setAdapter(adapter);
        // set context menu
        registerForContextMenu(lvShift);

        btAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etShiftName.getText().toString();
                String from = etFrom.getText().toString();
                String to = etTo.getText().toString();
                // kiểm tra nhập dữ liệu
                if(checkIsEmpty(name) == true){
                    etShiftName.setError("Hãy nhập tên ca");
                    return;
                }
                if(checkIsEmpty(from) == true){
                    etFrom.setError("Hãy chọn giờ vào ca ");
                    return;
                }
                if(checkIsEmpty(to) == true){
                    etTo.setError("Hãy chọn giờ ra ca");
                    return;
                }

                Shift s = new Shift(name,from,to);

                String key = mDatabase.child("calamviec").push().getKey();
                mDatabase.child("calamviec").child(key).setValue(s);

                Toast.makeText(ShiftActivity.this,"Thêm thành công !",Toast.LENGTH_SHORT).show();
            }
        });

        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time = new TimePickerDialog(ShiftActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Gán giờ lên EditText
                                etFrom.setText(hourOfDay + ":" + minute);
                            }
                        },
                        //Định dạng giờ phút
                        currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE),true);
                time.show();
            }
        });
        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time = new TimePickerDialog(ShiftActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Gán giờ lên EditText
                        etTo.setText(hourOfDay + ":" + minute);
                    }
                },
                        //Định dạng giờ phút
                        currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE),true);
                time.show();
            }
        });
        //sự kiện click item listview
        lvShift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShiftActivity.this,ShiftDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shiftid",alShift.get(position).shiftId);

                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });
    }

    private void addControls(){
        etShiftName = (EditText)findViewById(R.id.etShiftName);
        etFrom = (EditText)findViewById(R.id.etFrom);
        etTo = (EditText)findViewById(R.id.etTo);
        lvShift = (ListView)findViewById(R.id.lvShift);

        btAddShift = (Button) findViewById(R.id.btAddShift);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_shift, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()) {
            case R.id.contextRemoveShift:
                mDatabase.child("calamviec").child(alShift.get(position).shiftId).removeValue();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void createListViewData(){

        mDatabase.child("calamviec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shift shift = new Shift();
                shift = dataSnapshot.getValue(Shift.class);
                shift.shiftId = dataSnapshot.getKey();
                alShift.add(shift);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0;i<alShift.size();i++){
                    if(alShift.get(i).shiftId.equals(dataSnapshot.getKey())== true){
                        alShift.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkIsEmpty(String text){
        if(TextUtils.isEmpty(text) == true){
            return true;
        }
        return false;
    }

}
