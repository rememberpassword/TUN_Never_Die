package com.example.mycompute.lichhoc;

import java.util.Date;

/**
 * Created by dat on 09/07/2015.
 */
 class ChinaCalendar {
    final float PI = 3.14f;
    private int mDay;
    private int mMonth;
    private int mYear;
    private int mTimeZone = 8;
    private long mJulius;

    public int mLunarDay;
    public int mLunarYear;
    public int mLunarMonth;

    public ChinaCalendar(int day, int month, int year, int timeZone){
        mDay = day;
        mMonth = month;
        mYear = year;
        mTimeZone = timeZone;
        Date date = new Date();
    }

    private long convertToJuliusDay(){
        float a = (14 - mMonth) / 12;
        float y = mYear+4800-a;
        float m = mMonth+12*a-3;
        long jd = (long)(mDay + (153*m+2)/5 + 365*y + y/4 - y/100 + y/400 - 32045);
        return jd;
    }

    private int getNewMoonDay(int k, int timeZone) {
        float T, T2, T3, dr, Jd1, M, Mpr, F, C1, deltat, JdNew;
        T = k / 1236.85f; // Time in Julian centuries from 1900 January 0.5
        T2 = T * T;
        T3 = T2 * T;
        dr = PI / 180;
        Jd1 = 2415020.75933f + 29.53058868f * k + 0.0001178f * T2 - 0.000000155f * T3;
        Jd1 = (float) (Jd1 + 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr)); // Mean new moon
        M = (float) (359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3); // Sun's mean anomaly
        Mpr = (float) (306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3); // Moon's mean anomaly
        F = (float) (21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3); // Moon's argument of latitude
        C1 = (float) ((0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * dr * M));
        C1 = (float) (C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr));
        C1 = (float) (C1 - 0.0004 * Math.sin(dr * 3 * Mpr));
        C1 = (float) (C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051 * Math.sin(dr * (M + Mpr)));
        C1 = (float) (C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004 * Math.sin(dr * (2 * F + M)));
        C1 = (float) (C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006 * Math.sin(dr * (2 * F + Mpr)));
        C1 = (float) (C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005 * Math.sin(dr * (2 * Mpr + M)));
        if (T < -11)

        {
            deltat = (float) (0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3);
        } else

        {
            deltat = (float) (-0.000278 + 0.000265 * T + 0.000262 * T2);
        }

        ;
        JdNew = Jd1 + C1 - deltat;
        return (int) (JdNew + 0.5 + timeZone / 24);
    }

    private int jdFromDate(int dd, int mm, int yy) {
        int a, y, m, jd;
        a = (int) ((14 - mm) / 12);
        y = yy + 4800 - a;
        m = mm + 12 * a - 3;
        jd = dd + (int) ((153 * m + 2) / 5) + 365 * y + (int) (y / 4) - (int) (y / 100) + (int) (y / 400) - 32045;
        if (jd < 2299161) {
            jd = dd + (int) ((153 * m + 2) / 5) + 365 * y + (int) (y / 4) - 32083;
        }
        return jd;
    }

    private int getSunLongitude(int jdn,int timeZone) {
        float T, T2, dr, M, L0, DL, L;
        T = (float)(jdn - 2451545.5 - timeZone / 24) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        T2 = T * T;
        dr = PI / 180; // degree to radian
        M = (float)(357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2); // mean anomaly, degree
        L0 = (float)(280.46645 + 36000.76983 * T + 0.0003032 * T2); // mean longitude, degree
        DL = (float)((1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M));
        DL =(float)( DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M));
        L = L0 + DL; // true longitude, degree
        L = L * dr;
        L = (float)(L - PI * 2 * ((int)(L / (PI * 2)))); // Normalize to (0, 2*PI)
        return (int)(L / PI * 6);
    }

    private int getLunarMonth11(int yy,int timeZone){

        int k, off, nm, sunLong;
        off= jdFromDate(31,12,yy) -2415021;
        k= (int)(off/29.530588853);
        nm= getNewMoonDay(k, timeZone);
        sunLong= getSunLongitude(nm, timeZone); // sun longitude at local midnight
        if(sunLong>=9)
        {
            nm = getNewMoonDay(k - 1, timeZone);
        }
        return nm;
    }

    private int getLeapMonthOffset(int a11,int timeZone) {
        int k, last, arc, i;
        k = (int)((a11 - 2415021.076998695) / 29.530588853 + 0.5);
        last = 0;
        i = 1; // We start with the month following lunar month 11
        arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        do {
            last = arc;
            i++;
            arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        } while (arc != last && i < 14);
        return i - 1;
    }

    public Date ConVertToLunar() {
        int k, dayNumber, monthStart, a11, b11, lunarDay, lunarMonth, lunarYear, lunarLeap;
        dayNumber = jdFromDate(mDay, mMonth, mYear);
        k = (int)((dayNumber - 2415021.076998695) / 29.530588853);
        monthStart = getNewMoonDay(k + 1, mTimeZone);
        if (monthStart > dayNumber) {
            monthStart = getNewMoonDay(k, mTimeZone);
        }
        a11 = getLunarMonth11(mYear, mTimeZone);
        b11 = a11;
        if (a11 >= monthStart) {
            lunarYear = mYear;
            a11 = getLunarMonth11(mYear - 1, mTimeZone);
        } else {
            lunarYear = mYear + 1;
            b11 = getLunarMonth11(mYear + 1, mTimeZone);
        }
        lunarDay = dayNumber - monthStart + 1;
        int diff = (int)((monthStart - a11) / 29);
        lunarLeap = 0;
        lunarMonth = diff + 11;
        if (b11 - a11 > 365) {
            int leapMonthDiff = getLeapMonthOffset(a11, mTimeZone);
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10;
                if (diff == leapMonthDiff) {
                    lunarLeap = 1;
                }
            }
        }
        if (lunarMonth > 12) {
            lunarMonth = lunarMonth - 12;
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1;
        }
        mLunarDay = lunarDay;
        mLunarMonth= lunarMonth;
        mLunarYear = lunarYear;
        Date date = new Date(lunarYear,lunarMonth-1,lunarDay);
        return date;
    }
    public int getLNDay(){
        return  mLunarDay;
    }
    public int getLNYear(){
        return  mLunarYear;
    }
    public int getLNMonth(){
        return mLunarMonth;
    }

    public String getLunarDate(){
        String[] can = new String[] {"Giap", "At","Binh","Dinh","Mau","Ki","Canh","Tan","Nham","Qui"};
        String[] chi = new String[] {"Ti","Suu","Dan","Mao","Thinh","Ti","Ngo","Mui","Than","Dau","Tuat","Hoi"};
        long juliusDay = convertToJuliusDay();
        return can[(int)((juliusDay + 9) % 10)] + " " + chi[(int)((juliusDay +1) % 12)];
    }

    public String getLunarMonth(){
        String[] can = new String[] {"Giap", "At","Binh","Dinh","Mau","Ki","Canh","Tan","Nham","Qui"};
        String[] chi = new String[] {"Dan","Mao","Thinh","Ti","Ngo","Mui","Than","Dau","Tuat","Hoi","Ti","Suu"};

        int mod = (mLunarYear*12+ mLunarMonth+3)%10;
        return can[mod]+" "+chi[mLunarMonth-1];
    }

    public String getLunarYear(){
        String[] can = new String[] {"Giap", "At","Binh","Dinh","Mau","Ki","Canh","Tan","Nham","Qui"};
        String[] chi = new String[] {"Ty","Suu","Dan","Mao","Thinh","Ti","Ngo","Mui","Than","Dau","Tuat","Hoi"};
        return can[(mYear+6)%10] +" "+chi[(mYear+8)%12];
    }

}