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

public class UpdateCalender extends AppCompatActivity {

    static WebView webView;
    WebView webHT;
    String userName, passWord;
    static ArrayList<String> dataNK=new ArrayList<>();
    static ArrayList<String> dataDH=new ArrayList<>();
    static ArrayList<String> dataDHtem=new ArrayList<>();
    ArrayAdapter adapterNK,adapterDH;
    Spinner spNienKhoa,spDotHoc;
    Context context;
    ProgressDialog progressDialog;
    ProgressDialog progressLH;
    static TextView thongBao;
    String thongBaoText;
    static Element lichHTML;
    String matruong="http://dangkytinchi.ictu.edu.vn";
     boolean ck1=false;
     boolean ck2=false;
     boolean checkFirt=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_calender);
        webView = this.findViewById(R.id.web);
        spNienKhoa=this.findViewById(R.id.spNienKhoa);
        spDotHoc=this.findViewById(R.id.spDotHocNK);
        thongBao=this.findViewById(R.id.thongBao);
        webHT=this.findViewById(R.id.webhtlh);
        ImageView btn_save=this.findViewById(R.id.btn_save);
        context=this;
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading(Khoảng 10s) ...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressLH=new ProgressDialog(this);
        progressLH.setMessage("Loading(Khoảng 10s) ...");
        progressLH.setCancelable(false);
        ///
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this,adapterNK,adapterDH), "HtmlViewer");
        webView.setWebViewClient(webViewClient);


        webView.loadUrl(matruong);

        /////
        spDotHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveLich saveLich=new SaveLich();
                ArrayList<itemSQL> dataLichHoc= saveLich.tachLHFromHTML(lichHTML);
                if(dataLichHoc!=null) {
                    saveLich.SaveLichHoc(dataLichHoc,context);
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Lỗi khi phân tích lịch học");
                    builder.setMessage("Không thể phân tích lịch học này. Vui lòng thử lại sau");
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
        ////


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
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
                   tUrl=tUrl.substring(0,tUrl.indexOf("Study")) +"Reports/Form/StudentTimeTable.aspx";
                    webView.loadUrl(tUrl);
            }

            else if (url.indexOf("TimeTable") > 0) {
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

            /////
            Document doc= Jsoup.parse(html);
            Element nienKhoa=doc.getElementById("drpSemester");
            Element dotHoc=doc.getElementById("drpTerm");
            Elements dsNienKhoa=nienKhoa.getElementsByTag("option");
            for(int i=0;i<dsNienKhoa.size();i++){
                tdataNK.add(dsNienKhoa.get(i).text());
            }

            Elements dsDotHoc=dotHoc.getElementsByTag("option");
            for(int i=0;i<dsDotHoc.size();i++){
                tdataDH.add(dsDotHoc.get(i).text());
            }
            UpdateCalender.dataNK=tdataNK;
            UpdateCalender.dataDH=tdataDH;
            ////
            Element tb=doc.getElementById("lblSemester");
            thongBaoText=tb.text().toString();

            ///
            Element element=doc.getElementById("gridRegistered");
            UpdateCalender.lichHTML=element;
            //set spiner

            UpdateCalender.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressDialog.dismiss();
                    progressLH.dismiss();
                    thongBao.setText("Đã tải lịch:"+thongBaoText+ ". Hãy nhấn lưu lại để đổi lịch.");
                   String s="<html lang='vi'><head>" +
                           "<title>.: Thời khóa biểu sinh viên :.</title>" +
                           "<meta name=\"GENERATOR\" content=\"Microsoft Visual Studio .NET 7.1\">" +
                           "<meta name=\"CODE_LANGUAGE\" content=\"C#\">" +
                           "<meta name=\"vs_defaultClientScript\" content=\"JavaScript\">" +
                           "<meta name=\"vs_targetSchema\" content=\"http://schemas.microsoft.com/intellisense/ie5\">" +
                           "<meta content=\"progid:DXImageTransform.Microsoft.Fade(duration=.5)\" http-equiv=\"Page-Exit\">" +
                           "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../Includes/Default.css\">" +
                           "<script type=\"text/javascript\" src=\"../../Includes/util.js\"></script>" +
                           "</head><body>"+UpdateCalender.lichHTML.toString()+"</body></html>";
                  webHT.loadData(s,"text/html","UTF-8");
                  if(checkFirt) {
                   checkFirt=false;
                      ItemNKCick(0);
                  }
                    if( adapterNK==null) {
                        if (dataNK.size() > 0) {
                            adapterNK = new ArrayAdapter(context, android.R.layout.simple_spinner_item, UpdateCalender.dataNK);
                            adapterNK.setDropDownViewResource
                                    (android.R.layout.simple_list_item_single_choice);
                            spNienKhoa.setAdapter(adapterNK);
                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            //builder.setTitle("Thông tin ứng dụng");
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
                        adapterDH=new ArrayAdapter(context,android.R.layout.simple_spinner_item,UpdateCalender.dataDH);
                        adapterDH.setDropDownViewResource
                                (android.R.layout.simple_list_item_single_choice);
                        spDotHoc.setAdapter(adapterDH);

                    }
                }
            });

//            System.out.println(dotHoc.toString());
        }


    }
    void ItemDHCick(int index){
        webView.loadUrl("javascript:(function(){" +
                "document.getElementById('drpTerm').selectedIndex ="+index+";" +
                "document.getElementById('drpTerm').onchange();" +
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
}






