package wangchen.java.com.cnews;

import java.io.IOException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

  public static final int LOGIN_SUCCESS = 1;
  public static final int LOGIN_FAIL = 2;

  EditText emailText;
  EditText passwordText;
  Button loginButton;
  TextView signupLink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    TextInputLayout layout1 = findViewById(R.id.user_layout);
    TextInputLayout layout2 = findViewById(R.id.pw_layout);
    emailText = layout1.getEditText();
    passwordText = layout2.getEditText();

    loginButton = findViewById(R.id.btn_login);
    signupLink = findViewById(R.id.link_signup);

    loginButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        loginButtonClicked();
      }
    });

    signupLink.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        //startActivityForResult(intent, REQUEST_SIGNUP);
        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
      }
    });
  }

//  private void loginButtonClicked() {
//    String email = emailText.getText().toString();
//    String password = passwordText.getText().toString();
//    if(validate(email, password)) {
//      NewsDBHelper db = ((CNewsApp)getApplication()).getDB();
//      int res = db.checkPassword(email, password);
//      if(res == NewsDBHelper.LOGIN_SUCCESS) {
//        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent();
//        intent.putExtra("USERNAME", email);
//        setResult(LOGIN_SUCCESS, intent);
//        finish();
//
//      } else if(res == NewsDBHelper.PASSWORD_ERROR) {
//        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
//      } else if(res == NewsDBHelper.USER_NOT_FOUND) {
//        Toast.makeText(getApplicationContext(), "没有找到相应的用户", Toast.LENGTH_SHORT).show();
//      }
//    }
//  }

  private static final String ipAdress = "101.5.121.239";
  private void loginButtonClicked() {
    final String email = emailText.getText().toString();
    final String password = passwordText.getText().toString();
    if(!validate(email, password)) return;

    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
            R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("正在登陆...");
    progressDialog.show();

    new Thread() {
      public void run() {
        try {
          Socket s1 = new Socket(ipAdress, 6666);
          OutputStream os = s1.getOutputStream();
          DataOutputStream dos = new DataOutputStream(os);
          dos.writeUTF(email + " " + password);
//      new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//
//        }
//      }, 1000);
          InputStream is = s1.getInputStream();
          DataInputStream dis = new DataInputStream(is);
          String getStr = dis.readUTF();
          if(getStr.equals("USERNOTEXIST")) {
            LoginActivity.this.runOnUiThread(new Runnable()
            {
              public void run()
              {
                Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
              }
            });
            return;
          } else if(getStr.equals("PWERROR")) {
            LoginActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
              }
            });
            return;
          }
          else if(getStr.equals("SUCCESS")) {
            LoginActivity.this.runOnUiThread(new Runnable()
            {
              public void run()
              {
                Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", email);
                setResult(LOGIN_SUCCESS, intent);
                startActivity(intent);
                finish();
              }
            });
            return;
          }
          dis.close();
          dos.close();
          s1.close();

        } catch(IOException e) {
          Toast.makeText(getApplicationContext(), "登录失败 ", Toast.LENGTH_SHORT).show();
        }
      }
    }.start();
//    try {
//      Socket s1 = new Socket(ipAdress, 6666);
//      OutputStream os = s1.getOutputStream();
//      DataOutputStream dos = new DataOutputStream(os);
//      dos.writeUTF(email + " " + password);
////      new Handler().postDelayed(new Runnable() {
////        @Override
////        public void run() {
////
////        }
////      }, 1000);
//      InputStream is = s1.getInputStream();
//      DataInputStream dis = new DataInputStream(is);
//      String getStr = dis.readUTF();
//      if(getStr.equals("YES")) {
//        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
//      } else if(getStr.equals("NO")) {
//        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
//      }
//      dis.close();
//      dos.close();
//      s1.close();
//
//    } catch(IOException e) {
//      Toast.makeText(getApplicationContext(), "登录失败 ", Toast.LENGTH_SHORT).show();
//    }
  }

  public boolean validate(String email, String password) {
    boolean valid = true;

    if (email.isEmpty() || email.length() < 4 || email.length() > 10) {
      emailText.setError("用户名需在4到10位");
      valid = false;
    } else {
      emailText.setError(null);
    }
    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      passwordText.setError("密码应在4到10位");
      valid = false;
    } else {
      passwordText.setError(null);
    }
    return valid;
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent();
    setResult(LOGIN_FAIL, intent);
    super.onBackPressed();
  }
}
