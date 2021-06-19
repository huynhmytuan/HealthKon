package com.pinkteam.android.healthkon.database;

public class dbHealthSchema {
    //xoa run cha class
    public static final class UserTable{
        public static final String TABLE_NAME = "user";
        public static final String Id = "id";
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String GENDER = "gender";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
    }
    public static final class AccountTable{
        public static final String TABLE_NAME = "account";
        public static final String Id = "id";
        public static final String Username = "username";
        public static final String Password = "password";
    }
    public static final class HeightTable{
        public static final String TABLE_NAME = "height";
        public static final String Id = "id";
        public static final String Value = "value";
        public static final String Date = "date";
    }
    public static final class WeightTable{
        public static final String TABLE_NAME = "weight";
        public static final String Id = "id";
        public static final String Value = "value";
        public static final String Date = "date";
    }
    public static final class BmiTable{
        public static final String TABLE_NAME = "bmi";
        public static final String Id = "id";
        public static final String Date = "date";
        public static final String CurBMI = "cur_bmi";
        public static final String  Target_weight= "target_weight";
    }
    public static final class TopTable{
        public static final String TABLE_NAME = "top";
        public static final String Id = "id";
        public static final String Username = "username";
        public static final String Distance = "distance";
        public static final String  Start_Day = "start_day";
        public static final String End_Day = "end_day";
        public static final String  Top = "top";
    }
    public static final class JourneyTable{
        public static final String TABLE_NAME = "journey";
        public static final String JourneyId = "journeyID";
        public static final String Duration = "duration";
        public static final String Distance = "distance";
        public static final String Date = "date";
        public static final String Name = "name";
        public static final String Rating = "rating";
        public static final String Comment = "comment";
        public static final String Image = "image";
    }
    public static final class LocationTable{
        public static final String TABLE_NAME = "location";
        public static final String LocationID = "id";
        public static final String JourneyID = "journeyID";
        public static final String Altitude = "altitude";
        public static final String Longitude = "longitude";
        public static final String Latitude = "latitude";
    }
}
