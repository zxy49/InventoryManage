package team.androidinventory.servlet;

/**
 * Created by 彦辉 on 05/12.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
