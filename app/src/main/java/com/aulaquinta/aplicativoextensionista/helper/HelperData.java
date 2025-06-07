package com.aulaquinta.aplicativoextensionista.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HelperData {

    public static String ajustarData(long timeStamp){
        Date databaseDate = (new Date(timeStamp));
        SimpleDateFormat formatoPTBR = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));
        return formatoPTBR.format(databaseDate);
    }
}
