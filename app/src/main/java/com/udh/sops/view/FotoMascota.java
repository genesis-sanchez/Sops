package com.udh.sops.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import com.udh.sops.R;
import java.io.InputStream;

public class FotoMascota {
    public static void mostrar(ImageView imagen, String ruta) {
        imagen.setTag(ruta);
        imagen.setImageResource(R.drawable.ic_huella);
        if (ruta == null || ruta.isEmpty()) return;
        var resolver = imagen.getContext().getApplicationContext().getContentResolver();
        new Thread(() -> {
            try {
                Uri uri = Uri.parse(ruta);
                Bitmap foto;
                if (Build.VERSION.SDK_INT >= 28) {
                    foto = ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri),
                            (decoder, info, source) -> {
                                int mayor = Math.max(info.getSize().getWidth(), info.getSize().getHeight());
                                decoder.setTargetSampleSize(Math.max(1, mayor / 600));
                            });
                } else {
                    BitmapFactory.Options opciones = new BitmapFactory.Options();
                    opciones.inJustDecodeBounds = true;
                    try (InputStream archivo = resolver.openInputStream(uri)) {
                        BitmapFactory.decodeStream(archivo, null, opciones);
                    }
                    opciones.inSampleSize = 1;
                    while (Math.max(opciones.outWidth, opciones.outHeight) / opciones.inSampleSize > 600)
                        opciones.inSampleSize *= 2;
                    opciones.inJustDecodeBounds = false;
                    try (InputStream archivo = resolver.openInputStream(uri)) {
                        foto = BitmapFactory.decodeStream(archivo, null, opciones);
                    }
                }
                if (foto != null) imagen.post(() -> {
                    if (ruta.equals(imagen.getTag())) imagen.setImageBitmap(foto);
                });
            } catch (Exception exception) {
                // Se conserva la huella si el archivo ya no esta disponible.
            }
        }).start();
    }
}
