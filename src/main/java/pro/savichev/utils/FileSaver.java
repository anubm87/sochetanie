package pro.savichev.utils;

import io.reactivex.Completable;
import okhttp3.HttpUrl;

import java.io.IOException;

public interface FileSaver {
    void save(String url, String filePath) throws IOException;
    void save(HttpUrl httpUrl, String filePath) throws IOException;
    Completable save1(HttpUrl httpUrl, String filePath);
    Completable save1(String url, String filePath);
}
