package com.example.mycompute.lichhoc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    WebView webView;


    Context context;
    ProgressDialog progressDialog;
    String userName,passWord;
    String matruong="http://dangkytinchi.ictu.edu.vn";
    String fullName="";
    boolean checkFirt=true;
    ArrayList<String> listSchool;
    ArrayList<String> listID;
    Spinner spSchool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final TextView euserName=this.findViewById(R.id.userName);
        final TextView epassWord=this.findViewById(R.id.passWord);
        Button login=this.findViewById(R.id.btn_login);

        ///
        euserName.setHint("Mã sinh viên");
        epassWord.setHint("Mật khẩu");
        getSupportActionBar().hide();
        //
        webView = this.findViewById(R.id.weblogin);




        context=this;
        ///
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        ///
        setListSchool();
        spSchool=this.findViewById(R.id.spSchool);
        ArrayAdapter adapter;
                adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,listSchool);
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spSchool.setAdapter(adapter);
        spSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                matruong=listID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Login.MyJavaScriptInterface(this), "HtmlViewer");
        webView.setWebViewClient(webViewClient);


        /////
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                passWord=epassWord.getText().toString().trim();
                userName=euserName.getText().toString().trim();
                if(passWord!=""&& userName!="") {
                    webView.loadUrl(matruong);
                    progressDialog.show();
                }else
                    Toast.makeText(context,"Tài khoản hoặc mật khẩu không đúng !",Toast.LENGTH_LONG).show();

            }else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Không có kết nối internet ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        });



    }
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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
            } else
            if(url.indexOf("StudyRegister") > 0 || url.indexOf("Home") > 0)
            {
                    webView.loadUrl("javascript:window.HtmlViewer.showHTML " +
                            "(document.getElementsByTagName('html')[0].innerHTML);");
                progressDialog.dismiss();
            }
//            else
//            {
//                progressDialog.dismiss();
//                Toast.makeText(context,"Đăng nhập thất bại. Vui lòng thử lại!",Toast.LENGTH_LONG).show();
//            }


        }
    };
    //document.getDocumentById('drpSemester').selectedIndex=index;document.getDocumentById('drpSemester').onchange();
    //
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

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


        public MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;

        }

        @JavascriptInterface
        public void showHTML(String html) {
            html="<html>"+html+"</html>";
            /////
            Document doc= Jsoup.parse(html);

            ////lay html
            Element element=doc.getElementById("PageHeader1_lblUserFullName");
            fullName=element.text();
            fullName=fullName.substring(0,fullName.indexOf('('));
            ///


            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 //thuc hien xu ly html
                    if(checkFirt) {
                        checkFirt=false;
                        SharedPreferences sharedPreferences = getSharedPreferences("temp", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("fullName", fullName);
                        editor.putString("user", userName);
                        editor.putString("temp", passWord);
                        editor.putString("matruong",matruong);
                        editor.commit();
                        Intent intent = new Intent(context, UpdateCalender.class);
                        startActivity(intent);
                    }
                }
            });

//
        }


    }
    private void setListSchool(){
        listID=new ArrayList<>();
        listID.add("http://dangkytinchi.ictu.edu.vn");//cntt
        listID.add("https://daotao.tnu.edu.vn/knn");//khoa ngoai ngu
        listID.add("https://daotao2.tnu.edu.vn/dhnl");///dh nong lam
        listID.add("https://daotao.tnu.edu.vn/dhkt");//dai h kinh te
        listID.add("https://daotao2.tnu.edu.vn/dhkh");//dh khoa hoc
        listID.add("https://daotao2.tnu.edu.vn/kqt");//k quoc te
        listID.add("http://daotao.dhsptn.edu.vn/dhsp");//dh su pham
        listID.add("http://222.254.76.91/cdkt");//cd ky thuat cong ngiep
        listSchool=new ArrayList<>();
        listSchool.add("Đại học Công nghệ thông tin và truyền thông");
        listSchool.add("Khoa ngoại ngữ");
        listSchool.add("Đại học nông lâm");
        listSchool.add("Đại học kinh tế");
        listSchool.add("Đại học khoa học");
        listSchool.add("Khoa quốc tế");
        listSchool.add("Đại học sư phạm");
        listSchool.add("Cao đănge kỹ thuật công nghiệp");
    }


}






