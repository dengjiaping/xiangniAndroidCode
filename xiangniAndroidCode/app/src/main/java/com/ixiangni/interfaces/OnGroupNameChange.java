package com.ixiangni.interfaces;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

/**
 * 群名称改变，群列表刷新，当前群名称，由修改群名称引起
 * @ClassName:OnGroupNameChange

 * @PackageName:com.ixiangni.interfaces

 * @Create On 2017/7/6 0006   11:26

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/7/6 0006 handongkeji All rights reserved.
 */
public interface OnGroupNameChange {

    void onNameChange(String groupno,String groupName);
}
