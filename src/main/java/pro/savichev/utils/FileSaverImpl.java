package pro.savichev.utils;

import io.reactivex.Completable;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;

@Component
public class FileSaverImpl implements FileSaver {
    private OkHttpClient okHttpClient;

    @Override
    public void save(String url, String filePath) throws IOException {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl != null) {
            save(httpUrl, filePath);
        }
    }

    @Override
    public void save(HttpUrl httpUrl, String filePath) throws IOException {
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            ResponseBody body = response.body();
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IOException("Directories could not created.");
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(body.bytes());
            fos.close();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found.");
        }
    }

    @Override
    public Completable save1(String url, String filePath) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl != null) {
            return save1(httpUrl, filePath);
        }
        return Completable.error(new Exception("Parsing url failed."));
    }

    @Override
    public Completable save1(HttpUrl httpUrl, String filePath) {
        return Completable.create(emitter -> {
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                ResponseBody body = response.body();
                File file = new File(filePath);
                File parent = file.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    throw new IOException("Directories could not created.");
                }
                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(body.bytes());
                fos.close();
            }
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("File not found.");
            }
            emitter.onComplete();
        });
    }



    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

}
