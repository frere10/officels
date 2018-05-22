package rw.akimana.officels.Models;

import android.provider.BaseColumns;

public class IpAddress {
    private IpAddress(){

    }
    public static class IpAttributes implements BaseColumns {
        public static final String TABLE_NAME = "addresses";
        public static final String COL_ID = "id";
        public static final String COL_PROTOCAL = "protocal";
        public static final String COL_IPADDRESS = "ip_address";
        public static final String COL_PORT = "port";
    }
}
