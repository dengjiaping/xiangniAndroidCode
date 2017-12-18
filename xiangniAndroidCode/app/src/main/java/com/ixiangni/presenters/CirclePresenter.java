package com.ixiangni.presenters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.CircleListBean;
import com.ixiangni.bean.CommentBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.CircleContract;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */
public class CirclePresenter implements CircleContract.Presenter {

    private CircleContract.View view;
    private Context context;
    private static final int pageSize = 10;
    private int currentPage = 1;
    List<CircleListBean.DataBean> CricleList;

    public CirclePresenter(CircleContract.View view, Context context) {
        this.view = view;
        this.context = context;

    }

    public Context getContext() {
        return context;
    }

    /**
     * 首页极有圈的数据
     *
     * @param loadType
     */
    @Override
    public void loadData(final int loadType) {
        if (loadType == 1) {
            currentPage = 1;
        } else if (loadType == 2) {
            currentPage++;
        }
        CricleList = new ArrayList<>();//极有圈首页数据
        String url = UrlString.URL_MY_JIYOUQUAN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("pageSize", pageSize + "");
        params.put("currentPage", currentPage + "");
        RemoteDataHandler.asyncTokenPost(url, context, true, params, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (TextUtils.isEmpty(json) || json == null || "null".equals(json)) {
                    if (view != null) {
                        view.onError();
                    }
                    return;
                }

                CircleListBean circleListBean = new Gson().fromJson(json, CircleListBean.class);
                CricleList = circleListBean.getData();
                if (CricleList != null && view != null) {
                    view.update2loadData(loadType, CricleList);
                }


            }
        });
    }

    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    @Override
    public void deleteCircle(String circleId) {

    }

    @Override
    public void addFavort(int adapterPosition,int momentnewsid, final boolean flag) {

        if(view!=null){
            view.showLoading(flag?"点赞中...":"取消点赞...");
        }
        FriendCirclePresenter.dianzan(getContext(), "" + momentnewsid, flag, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if(view!=null){
                    view.update2AddFavorite(adapterPosition,flag);
                    view.loadingFinish();
                    view.onFailed(flag?"点赞成功":"取消成功");
                }
            }

            @Override
            public void onFailed(String errorMsg) {

                if(view!=null){
                    view.onFailed(errorMsg);
                }
            }
        });
    }


    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    @Override
    public void deleteFavort(int circlePosition, String favortId) {

    }

    /**
     * @param content
     * @param config  CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(final String content, final CommentConfig config) {
        if (config == null) {
            return;
        }
        //如果没有评论的id的话删除会删除不了

        //单评论
        final CommentBean item = new CommentBean();//pinglun de
        item.setCommentcontent(content);
        item.setUserid(LoginHelper.getInstance().getUserid(context));
        item.setMomentnewsid(config.momentnewsid);//极有圈id
        item.setUsernick(DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo().getNickname());

        if (config.commentType == CommentConfig.Type.PUBLIC) {
            item.setAdditionalmark("0");

        } else if (config.commentType == CommentConfig.Type.REPLY) {
            item.setAdditionalmark("1");
            item.setMainnick(config.plusernick);//pl者的
            item.setMainuserid(config.pluserid);
        }

//        //回复评论
//        final CircleListBean.DataBean.CommentListBean PlItem = new CircleListBean.DataBean.CommentListBean();//pinglun de
//        PlItem.setCommentcontent(content);
//        PlItem.setUserid(MyApp.getInstance().getUserid());
//        PlItem.setMomentnewsid(config.momentnewsid);//极有圈id
//        PlItem.setUsernick(MyApp.getInstance().getUserNick());
//        PlItem.setAdditionalmark("1");
//        PlItem.setMainnick(config.plusernick);//pl者的
//        PlItem.setMainuserid(config.pluserid);


        String url = UrlString.URL_ADD_COMMENT;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("momentnewsid", config.momentnewsid);//极有圈id
        params.put("commentcontent", content);//内容
        params.put("newsuserid", config.userid);//发布者的userid
        if (config.commentType == CommentConfig.Type.PUBLIC) {
            //评论

        } else if (config.commentType == CommentConfig.Type.REPLY) {
            params.put("mainuserid", config.pluserid);//发布者的userid
            //回复
        }
        if(view!=null){
            view.onLoading("评论中...");
        }
        RemoteDataHandler.asyncPost(url, params, context, true, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                Log.i(TAG, "评论结果: "+json);
                if (!CommonUtils.isStringNull(json)) {

                    if(view!=null){
                        view.loadingFinish();
                    }else {
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(json);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        if ("1".equals(status)) {
                            //操作成功
                            Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                            String commentid = object.getString("data");
                            item.setCommentid(commentid);
                            if (view != null) {
                                view.update2AddComment(config.circlePosition, item);
                            }
                        } else {
                            Toast.makeText(context, object.getString(Constants.message), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


    }

    private static final String TAG = "CirclePresenter";

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    @Override
    public void deleteComment(final int circlePosition, final String commentId) {//commentid
        String url = UrlString.URL_DELETE_COMMENT;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", ((MyApp) context.getApplicationContext()).getUserTicket());
        params.put("commentid", commentId);


        if(view!=null){
            view.onLoading("删除中...");
        }
        RemoteDataHandler.asyncPost(url, params, context, true, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (json == null || "null".equals(json)) {
                    return;
                }
                if(view==null){
                    return;
                }
                view.loadingFinish();
                try {
                    JSONObject object = new JSONObject(json);
                    String status = object.getString("status");
                    String message = object.getString("message");
                    if ("1".equals(status)) {
                        //操作成功
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        if (view != null) {
                            view.update2DeleteComment(circlePosition, commentId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void forwardCircle(int adapterPosition, int momentnewsid) {
        if(view!=null){
            view.onLoading("转发中...");
        }
        FriendCirclePresenter.forwardToCircle(getContext(), "" + momentnewsid, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if(view!=null){
                    view.onFailed("转发成功");
                    view.loadingFinish();
                    view.updateForward(adapterPosition);

                }
            }

            @Override
            public void onFailed(String errorMsg) {

                if(view!=null){
                    view.onFailed(errorMsg);
                }
            }
        });
    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig) {
        if (view != null) {
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
        }
    }


    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
    }

    public int getTotalSize() {
        return currentPage * pageSize;
    }
}
