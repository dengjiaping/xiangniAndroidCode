package com.ixiangni.presenters.contract;



import com.ixiangni.bean.CircleListBean;
import com.ixiangni.bean.FavortItem;
import com.ixiangni.presenters.CommentConfig;

import java.util.List;
import com.ixiangni.bean.CommentBean;

/**
 * Created by Administrator on 2016/11/12.
 */
public interface CircleContract {
    interface View extends BaseView {
        void update2DeleteCircle(String circleId);//删除朋友圈    因为在Activity中实现了他所以 这个方法在Activity中处理结果和用户交互界面的东西

        void update2AddFavorite(int adapterPosition, boolean flag);//添加取消赞

        void update2AddComment(int circlePosition, CommentBean addItem);//添加评论
        void update2DeleteComment(int circlePosition, String commentId);//删除评论
        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);//键盘情况
        void update2loadData(int loadType, List<CircleListBean.DataBean> list);//加载数据
        public void onError();

        void updateForward(int adapterPosition);

        void onFailed(String errorMsg);
        void onLoading(String loadingmessage);
        void loadingFinish();

    }

    interface Presenter extends BasePresenter {
        void loadData(int loadType);//加载数据
        void deleteCircle(final String circleId);//删除朋友圈
        void addFavort(int adapterPosition,final int momentnewsid,boolean flag);//添加赞
        void deleteFavort(final int circlePosition, final String favortId);//删除赞
        void deleteComment(final int circlePosition, final String commentId);//*删除评论

        void forwardCircle(int adapterPosition,int momentnewsid);//转发朋友圈

    }
}
