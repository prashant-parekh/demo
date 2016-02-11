package com.example;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.itextpdf.text.BaseColor;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;

import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;


import com.lowagie.text.pdf.PdfPCell;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;

public class MainActivity extends Activity {


    private Button b, btn_save;
    DBAdapter db;
    EditText ed_fname, ed_lname, ed_email, ed_company, ed_city, ed_pincode, ed_mobilno;
    String imageUrl;
    Font paraFont,font1;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);
        bindsView();

        db = new DBAdapter(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub

                /*String fname=ed_fname.getText().toString();
                String lname=ed_lname.getText().toString();
                String email=ed_email.getText().toString();
                String company=ed_company.getText().toString();
                String city=ed_city.getText().toString();
                String pincode=ed_pincode.getText().toString();
                String mobilno=ed_mobilno.getText().toString();

                // ---add a contact---
                db.open();
                long id = db.insertContact(fname,lname,email,company,city,pincode,mobilno);
                db.close();

                Toast.makeText(getApplicationContext(),"inserted",Toast.LENGTH_SHORT).show();*/

                createPDF();

            }
        });

    }

    private void bindsView() {

        ed_fname = (EditText) findViewById(R.id.ed_fname);
        ed_lname = (EditText) findViewById(R.id.ed_lname);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_company = (EditText) findViewById(R.id.ed_company);
        ed_city = (EditText) findViewById(R.id.ed_city);
        ed_pincode = (EditText) findViewById(R.id.ed_pincode);
        ed_mobilno = (EditText) findViewById(R.id.ed_mobilno);
        btn_save = (Button) findViewById(R.id.btn_save);


    }


    public void createPDF() {

        try {

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText";
            image = Environment.getExternalStorageDirectory().getAbsolutePath() + "/droidText/imgv_shirt.jpeg";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, "detail.pdf");

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            paraFont = new Font(Font.HELVETICA ,25, Font.BOLD, harmony.java.awt.Color.blue);
            font1 = new Font(Font.NORMAL,20);

            Paragraph preface = new Paragraph();

            // get input stream
            InputStream ims = getAssets().open("abc.jpg");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Image.ALIGN_RIGHT | Image.ALIGN_TOP);

            //image.setAbsolutePosition(500f, 650f);
            preface.add(image);

            // We add one empty line
            addEmptyLine(preface, 1);
            // Lets write a big header

            Paragraph title=new Paragraph("Resume",paraFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            addEmptyLine(preface, 1);

            Paragraph userdetail=new Paragraph("PRASHANT PAREKH",font1);
            preface.add(new Paragraph(userdetail));
            addEmptyLine(preface, 1);

            Paragraph date=new Paragraph(""+new Date(),font1);
            preface.add(date);
            addEmptyLine(preface, 3);

            Paragraph hello=new Paragraph("Hello Sir,",font1);
            preface.add(hello);
            addEmptyLine(preface, 2);

            Paragraph detail=new Paragraph("MySelf prashant parekh.I complted my mca from scet and currently working at GA technology as android developer.My acdamic details are as follow..",font1);
            preface.add(detail);
            addEmptyLine(preface, 4);

            Paragraph faith=new Paragraph("Regards,",font1);
            preface.add(faith);
            addEmptyLine(preface, 1);

            Paragraph sign=new Paragraph("Prashant Parekh",font1);
            preface.add(sign);
            addEmptyLine(preface, 1);

            document.add(preface);

            document.newPage();

            float[] columnWidths = {120, 120, 120, 120, 120, 120, 120};
            PdfPTable table = new PdfPTable(7);

            table.setTotalWidth(700F);
            table.setLockedWidth(true);


            buildNestedTables(table);


            Paragraph p = new Paragraph("User Detail",paraFont);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            p.setFont(paraFont);
            p.setSpacingAfter(20);


            document.add(p);
            document.add(table);


            document.close();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }


    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void buildNestedTables(PdfPTable outerTable) {

        PdfPCell c1 = new PdfPCell(new Phrase("FNAME"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("LNAME"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("EMAIL"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("COMPANY"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("CITY"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("PINCODE"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);

        c1 = new PdfPCell(new Phrase("MOBIL NO"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPadding(5);
        outerTable.addCell(c1);
        outerTable.setHeaderRows(1);


        /*outerTable.addCell("Fname");
        outerTable.addCell("Lname");
        outerTable.addCell("Email");
        outerTable.addCell("Company");
        outerTable.addCell("City");
        outerTable.addCell("Pincode");
        outerTable.addCell("Mobil No");*/


        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
            do {

                c1 = new PdfPCell(new Phrase(c.getString(1)));
                c1.setPadding(5);
                outerTable.addCell(c1);

                c1 = new PdfPCell(new Phrase(c.getString(2)));
                c1.setPadding(5);
                outerTable.addCell(c1);

                c1 = new PdfPCell(new Phrase(c.getString(3)));
                c1.setPadding(5);
                outerTable.addCell(c1);

                c1 = new PdfPCell(new Phrase(c.getString(4)));
                c1.setPadding(5);
                outerTable.addCell(c1);

                c1 = new PdfPCell(new Phrase(c.getString(5)));
                c1.setPadding(5);
                outerTable.addCell(c1);

                c1 = new PdfPCell(new Phrase(c.getString(6)));
                c1.setPadding(5);
                outerTable.addCell(c1);

                c1 = new PdfPCell(new Phrase(c.getString(7)));
                c1.setPadding(5);
                outerTable.addCell(c1);

            } while (c.moveToNext());
        }
        db.close();

    }


}


