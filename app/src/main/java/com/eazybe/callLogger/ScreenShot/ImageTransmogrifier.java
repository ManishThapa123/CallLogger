package com.eazybe.callLogger.ScreenShot;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.Image;
import android.media.ImageReader;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.eazybe.callLogger.keyboard.keyboardHelper.ContactsModel;
import com.eazybe.callLogger.keyboard.keyboardHelper.PhoneNumberScreenshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ImageTransmogrifier implements ImageReader.OnImageAvailableListener {
    private final int width;
    private final int height;
    private final ImageReader imageReader;
    private final ScreenshotService svc;
    private Bitmap latestBitmap = null;
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    private ArrayList<ContactsModel> contactList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    ImageTransmogrifier(ScreenshotService svc) {
        this.svc = svc;
        Display display = svc.getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getRealSize(size);

        int width = size.x;
        int height = size.y;

        while (width * height > (2 << 19)) {
            width = width >> 1;
            height = height >> 1;
        }

        this.width = width;
        this.height = height;

        imageReader = ImageReader.newInstance(width, height,
                PixelFormat.RGBA_8888, 2);

        imageReader.setOnImageAvailableListener(this, svc.getHandler());


        final Image image = imageReader.acquireLatestImage();
        if (image != null) {
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            int bitmapWidth = width + rowPadding / pixelStride;

            if (latestBitmap == null ||
                    latestBitmap.getWidth() != bitmapWidth ||
                    latestBitmap.getHeight() != height) {
                if (latestBitmap != null) {
                    latestBitmap.recycle();
                }

                latestBitmap = Bitmap.createBitmap(bitmapWidth,
                        height, Bitmap.Config.ARGB_8888);
            }

            latestBitmap.copyPixelsFromBuffer(buffer);
            image.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap cropped = Bitmap.createBitmap(latestBitmap, 0, 200,
                    width, height);

            cropped.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] newPng = baos.toByteArray();

            svc.processImage(newPng);
        }
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        final Image image = imageReader.acquireLatestImage();

        if (image != null) {
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            int bitmapWidth = width + rowPadding / pixelStride;

            if (latestBitmap == null ||
                    latestBitmap.getWidth() != bitmapWidth ||
                    latestBitmap.getHeight() != height) {
                if (latestBitmap != null) {
                    latestBitmap.recycle();
                }

                latestBitmap = Bitmap.createBitmap(bitmapWidth,
                        height, Bitmap.Config.ARGB_8888);
            }

            latestBitmap.copyPixelsFromBuffer(buffer);
            image.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Bitmap cropped = Bitmap.createBitmap(latestBitmap, (int) (latestBitmap.getWidth() * 0.16), (int) (latestBitmap.getHeight() * 0.04),
                    270, 70);

            InputImage imageOCR = InputImage.fromBitmap(cropped, 0);
            Task<Text> result =
                    recognizer.process(imageOCR)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {

                                    Log.d("OCR Text is:", String.valueOf(visionText));

                                    processTextRecognitionResult(visionText);

                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });

            cropped.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] newPng = baos.toByteArray();

            svc.processImage(newPng);
        }
    }

    private void processTextRecognitionResult(Text result) {
        String resultText = result.getText();
        Log.d("Texts", resultText);

        if (validCellPhone(resultText)) {
            Log.d("isPhoneNumber", "Is phone Number");
            PhoneNumberScreenshot.INSTANCE.passPhoneNumberToKeyboard("Hello Testing for phone number = " + resultText);
        } else {
            Log.d("isPhoneNumber", "Is not a phone Number.");
            getNumberFromContacts(resultText);
        }

//        for (Text.TextBlock block : result.getTextBlocks()) {
//            String blockText = block.getText();
//            Point[] blockCornerPoints = block.getCornerPoints();
//            Rect blockFrame = block.getBoundingBox();
//            for (Text.Line line : block.getLines()) {
//                String lineText = line.getText();
//                Point[] lineCornerPoints = line.getCornerPoints();
//                Rect lineFrame = line.getBoundingBox();
//                for (Text.Element element : line.getElements()) {
//                    String elementText = element.getText();
//                    Point[] elementCornerPoints = element.getCornerPoints();
//                    Rect elementFrame = element.getBoundingBox();
//                }
//            }
//        }
    }

    private void getNumberFromContacts(String resultText) {
        String phoneNumberToShare = "";
        ContentResolver contentResolver = svc.getContentResolver();
        String[] fieldListProjection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC";
        Cursor phones = contentResolver
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        , null, null, null, sort);

        if (phones != null && phones.getCount() > 0) {
            while (phones.moveToNext()) {
                @SuppressLint("Range")
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range")
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactsModel contactsModel = new ContactsModel();
                contactsModel.setNames(name);
                contactsModel.setNumbers(phoneNumber);
                contactList.add(contactsModel);
            }
            for (ContactsModel contactList : contactList) {
                if (contactList.getName().equals(resultText)) {
                    Log.d("ContactList", contactList.getName() + contactList.getNumber());
                    phoneNumberToShare = contactList.getNumber();
                }

            }

            phones.close();
        }
        PhoneNumberScreenshot.INSTANCE.passPhoneNumberToKeyboard("Phone number = " + phoneNumberToShare);
    }

    public boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    Surface getSurface() {
        return (imageReader.getSurface());
    }

    int getWidth() {
        return (width);
    }

    int getHeight() {
        return (height);
    }

    void close() {
        imageReader.close();
    }
}
