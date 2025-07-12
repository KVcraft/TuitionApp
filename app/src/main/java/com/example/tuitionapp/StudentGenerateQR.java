package com.example.tuitionapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class StudentGenerateQR extends AppCompatActivity {

    // Variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    private EditText dataEdt;
    private Button generateQrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_generate_qr);

        // Initializing all variables.
        qrCodeIV = findViewById(R.id.idIVQrcode);
        dataEdt = findViewById(R.id.idEdt);
        generateQrBtn = findViewById(R.id.idBtnGenerateQR);

        // Initializing onclick listener for button.
        generateQrBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        if (TextUtils.isEmpty(
                                dataEdt.getText().toString())) {

                            // If the edittext inputs are empty
                            // then execute this method showing
                            // a toast message.
                            Toast.makeText(StudentGenerateQR.this,"Enter some text to generate QR Code",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            generateQRCode(
                                    dataEdt.getText().toString());
                        }
                    }
                });
    }

    private void generateQRCode(String text)
    {
        BarcodeEncoder barcodeEncoder
                = new BarcodeEncoder();
        try {

            // This method returns a Bitmap image of the
            // encoded text with a height and width of 400
            // pixels.
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);

            // Sets the Bitmap to ImageView
            qrCodeIV.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}