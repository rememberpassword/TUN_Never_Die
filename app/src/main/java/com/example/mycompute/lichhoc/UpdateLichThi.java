package com.example.mycompute.lichhoc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UpdateLichThi extends AppCompatActivity {

    static WebView webView;
    TextView txtLT;
    String userName, passWord;
    static ArrayList<String> dataNK=new ArrayList<>();
    static ArrayList<String> dataDH=new ArrayList<>();
    static ArrayList<String> dataDHtem=new ArrayList<>();
    static ArrayList<String> dataLT=new ArrayList<>();
    static ArrayList<String> dataLTtem=new ArrayList<>();
    ArrayAdapter adapterNK,adapterDH;
    Spinner spNienKhoa,spDotThi,spLanThi;
    Context context;
    ProgressDialog progressDialog;
    ProgressDialog progressLH;
    static TextView thongBao;
    String thongBaoText;
    static Element lichHTML;
    String matruong="http://dangkytinchi.ictu.edu.vn";
    boolean ck1=false;
    boolean ck2=false;
    boolean ck3=false;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lichthi);
        webView = this.findViewById(R.id.webThi);
        spNienKhoa=this.findViewById(R.id.spNKthi);
        spDotThi=this.findViewById(R.id.spDTthi);
        spLanThi=this.findViewById(R.id.spLanThi);
        txtLT=this.findViewById(R.id.thongbaolt);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ////
        SharedPreferences sharedPreferences=getSharedPreferences("temp",MODE_PRIVATE);
        userName=sharedPreferences.getString("user","");
        passWord=sharedPreferences.getString("temp","");
        matruong=sharedPreferences.getString("matruong","");
        passWord=MaHoa.decode(passWord);
        if(userName==""|| passWord==""){
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        }
        ///

        ImageView btn_save=this.findViewById(R.id.btnsaveLichThi);
        context=this;
        ///
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading(khoảng 10s) ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressLH=new ProgressDialog(this);
        progressLH.setMessage("Loading(khoảng 10s) ...");
        progressLH.setCancelable(true);
        ///
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this,adapterNK,adapterDH), "HtmlViewer");
        webView.setWebViewClient(webViewClient);


        webView.loadUrl(matruong);

        /////
        spDotThi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(ck1)
                    ItemDHCick(position);
                if(!ck1) ck1=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNienKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(ck2)
                    ItemNKCick(position);
                if(!ck2) ck2=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLanThi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(ck3)
                    ItemLTCick(position);
                if(!ck3) ck3=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveLich saveLich=new SaveLich();
                ArrayList<itemSQL> dataLichThi= saveLich.tachLTFromHTML(lichHTML);
                if(dataLichThi!=null) {
                    saveLich.SaveLichThi(dataLichThi,context);
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Lỗi khi phân tích lịch thi");
                    builder.setMessage("Không thể phân tích lịch thi này. Vui lòng thử lại sau");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }

                //System.out.println("ok");

            }
        });

    }


    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.indexOf("login") > 0) {
                String s = "javascript:(function(){" +
                        "var x= document.forms['Form1'];" +
                        " var i ; for (i = 0; i < x.length ;i++) {" +
                        "        if(x.elements[i].name=='txtUserName') x.elements[i].value='" + userName + "'; " +
                        "else if(x.elements[i].name=='txtPassword') x.elements[i].value='" + convertPassMd5(passWord) + "'; " +
                        "}})()";
                webView.loadUrl(s);
                s = "javascript:(function(){" +
                        "document.getElementById('btnSubmit').click();" +
                        "})()";
                webView.loadUrl(s);
            } else if (url.indexOf("StudyRegister") > 0){
                String tUrl=webView.getUrl();
                if(tUrl.indexOf("StudyRegister") > 0)
                    tUrl=tUrl.substring(0,tUrl.indexOf("Study")) +"StudentViewExamList.aspx";
                webView.loadUrl(tUrl);
            }

            else if (url.indexOf("StudentViewExam") > 0) {
                webView.loadUrl("javascript:window.HtmlViewer.showHTML " +
                        "(document.getElementsByTagName('html')[0].innerHTML);");

            }



        }
    };
    //document.getDocumentById('drpSemester').selectedIndex=index;document.getDocumentById('drpSemester').onchange();
    //

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }


    class MyJavaScriptInterface {

        private Context ctx;
        private ArrayAdapter adapterNK ;
        private ArrayAdapter adapterDH;

        public MyJavaScriptInterface(Context ctx, ArrayAdapter adapterNK , ArrayAdapter adapterDH) {
            this.ctx = ctx;
            this.adapterNK = adapterNK;
            this.adapterDH = adapterDH;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            html="<html>"+html+"</html>";
            ArrayList<String> tdataNK=new ArrayList<>();
            ArrayList<String> tdataDH=new ArrayList<>();
            ArrayList<String> tdataLT=new ArrayList<>();

            /////
            Document doc= Jsoup.parse(html);
            Element nienKhoa=doc.getElementById("drpSemester");
            Element dotThi=doc.getElementById("drpDotThi");
            Element lanThi=doc.getElementById("drpExaminationNumber");
            //
            Elements dsNienKhoa=nienKhoa.getElementsByTag("option");
            for(int i=0;i<dsNienKhoa.size();i++){
                tdataNK.add(dsNienKhoa.get(i).text());
            }

            Elements dsDotHoc=dotThi.getElementsByTag("option");
            for(int i=0;i<dsDotHoc.size();i++){
                tdataDH.add(dsDotHoc.get(i).text());
            }
            Elements dsLanThi=lanThi.getElementsByTag("option");
            for(int i=0;i<dsLanThi.size();i++){
                tdataLT.add(dsLanThi.get(i).text());
            }
            UpdateLichThi.dataNK=tdataNK;
            UpdateLichThi.dataDH=tdataDH;
            UpdateLichThi.dataLT=tdataLT;
            ////


            ///
            final Element element=doc.getElementById("tblCourseList");
            UpdateLichThi.lichHTML=element;
            //set spiner

            UpdateLichThi.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    progressLH.dismiss();
                    Elements elements=UpdateLichThi.lichHTML.getElementsByTag("tr");
                    if(elements.size()>2)
                    txtLT.setText("Lịch thi đã được tải thành công. Hãy nhấn lưu để lưu lại.");
                    if( adapterNK==null) {
                        if (dataNK.size() > 0) {
                            adapterNK = new ArrayAdapter(context, android.R.layout.simple_spinner_item, UpdateLichThi.dataNK);
                            adapterNK.setDropDownViewResource
                                    (android.R.layout.simple_list_item_single_choice);
                            spNienKhoa.setAdapter(adapterNK);
                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Không thể tải lịch học. Vui lòng thử lại sau !");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                        }
                    }
                    if(dataDH.size()>0 && !dataDH.equals(dataDHtem) ){
                        dataDHtem=dataDH;
                        adapterDH=new ArrayAdapter(context,android.R.layout.simple_spinner_item,UpdateLichThi.dataDH);
                        adapterDH.setDropDownViewResource
                                (android.R.layout.simple_list_item_single_choice);
                        spDotThi.setAdapter(adapterDH);

                    }
                    if(dataLT.size()>0 && !dataLT.equals(dataLTtem) ){
                        dataLTtem=dataLT;
                        adapterDH=new ArrayAdapter(context,android.R.layout.simple_spinner_item,UpdateLichThi.dataLT);
                        adapterDH.setDropDownViewResource
                                (android.R.layout.simple_list_item_single_choice);
                        spLanThi.setAdapter(adapterDH);

                    }
                }
            });

//            System.out.println(dotHoc.toString());
        }


    }
    void ItemDHCick(int index){
        webView.loadUrl("javascript:(function(){" +
                "document.getElementById('drpDotThi').selectedIndex ="+index+";" +
                "document.getElementById('drpDotThi').onchange();" +
                "})()");
        progressLH.show();
    }
    void ItemNKCick(int index){
        webView.loadUrl("javascript:(function(){" +
                // "document.getElementById('drpSemester').selectedIndex ="+index+";" +
                "document.getElementById('drpSemester').options["+index+"].selected = 'selected';" +
                "document.getElementById('drpSemester').onchange();" +
                "})()");
        progressLH.show();
    }
    void ItemLTCick(int index){
        webView.loadUrl("javascript:(function(){" +
                "document.getElementById('drpExaminationNumber').selectedIndex ="+index+";" +
                "document.getElementById('drpExaminationNumber').onchange();" +
                "})()");
        progressLH.show();
    }
}






