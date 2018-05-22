package rw.akimana.officels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Models.IpAddress;

public class SettingActivity extends AppCompatActivity {

    private EditText edIpAddress, edProtocal;
    private TextView tvIpAddress, tvProtocal;
    private Button btnSave, btnChange, btnDeleteTbl;

    private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        edIpAddress = (EditText)findViewById(R.id.et_ip_address);
        edProtocal = (EditText)findViewById(R.id.et_protocal);

        tvIpAddress = (TextView) findViewById(R.id.tv_ip_address);
        tvProtocal = (TextView)findViewById(R.id.tv_protocal);

        btnSave = (Button)findViewById(R.id.btn_save);
        btnChange = (Button)findViewById(R.id.btn_change);
        btnDeleteTbl = (Button)findViewById(R.id.btn_delete_tbl);

        helper = new DatabaseHelper(getApplicationContext());

        //Save ip address

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = edIpAddress.getText().toString().trim();
                String protocal = edProtocal.getText().toString().trim();

                if (ipAddress.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter IP Address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (protocal.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Protocal you are using!", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = helper.findIpDetails();
                if(hashMap == null) {
                    long dataSaved = helper.save(protocal, ipAddress);
                    if (dataSaved == 1) {
                        getData();
                    }
                    else Toast.makeText(getApplicationContext(), "Not saved. Something goes wrong!", Toast.LENGTH_SHORT).show();
                }
                else getData();
            }
        });
        //Update ip address table
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = edIpAddress.getText().toString().trim();
                String protocal = edProtocal.getText().toString().trim();
                if (ipAddress.isEmpty()||protocal.isEmpty()) {
                    HashMap<String, String> hashMap = helper.findIpDetails();
                    if (hashMap == null) {
                        tvIpAddress.setText("Set Protocal and ");
                        tvProtocal.setText("IP ADDRESS");
                    } else {
                        String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
                        String protocalDetails = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
                        String ipAddre = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
                        edProtocal.setText(protocalDetails);
                        edIpAddress.setText(ipAddre);
                    }
                }
                if (!ipAddress.isEmpty()&&!protocal.isEmpty()) {
                    long saved = helper.update(1, protocal, ipAddress);
                    if(saved == 1) {
                        Toast.makeText(getApplicationContext(), "Protocal:" + protocal + " and IP:" + ipAddress + " has saved", Toast.LENGTH_LONG).show();
                        getData();
                    }else Toast.makeText(getApplicationContext(), "Something goes wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Delete IP address table
        btnDeleteTbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long tblDeleted = helper.deleteTable(IpAddress.IpAttributes.TABLE_NAME);
                if (tblDeleted == 1)
                    Toast.makeText(getApplicationContext(), "Table: "+ IpAddress.IpAttributes.TABLE_NAME+" deleted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Something goes wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getData() {
        HashMap<String, String> hashMap = helper.findIpDetails();
        if (hashMap == null) {
            tvIpAddress.setText("Protocal and ");
            tvProtocal.setText("IP ADDRESS");
        } else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocalDetails = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress__ = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            tvIpAddress.setText("IP Address: " + ipAddress__);
            tvProtocal.setText("Protocal: " + protocalDetails);
        }
    }
}
