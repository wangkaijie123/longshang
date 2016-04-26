package com.lst.lstjx.fragment.setting;

import com.lst.yuewo.R;

import android.os.Message;
import android.view.View;
import android.widget.Toast;

import io.rong.database.ConversationDatabase;
import io.rong.database.DraftDao;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.BaseSettingFragment;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.ipc.RLog;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 15/7/31.
 */
public class RongClearConversationMsgFragment  extends BaseSettingFragment implements AlterDialogFragment.AlterDialogBtnListener {

    private Conversation conversation;

    @Override
    protected void initData() {

    }

    @Override
    protected String setTitle() {
        return getString(R.string.de_setting_clear_msg_name);
    }

    @Override
    protected boolean setSwitchButtonEnabled() {
        return false;
    }

    @Override
    protected int setSwitchBtnVisibility() {
        return View.GONE;
    }

    @Override
    protected void onSettingItemClick(View v) {
        conversation = new Conversation();
        conversation.setConversationType(getConversationType());
        conversation.setTargetId(getTargetId());


        final AlterDialogFragment dialogFragment = AlterDialogFragment.newInstance(getString(R.string.rc_setting_name), getString(R.string.rc_setting_clear_msg_prompt), getString(R.string.rc_dialog_cancel), getString(R.string.rc_dialog_ok));
        dialogFragment.setOnAlterDialogBtnListener(this);
        dialogFragment.show(getFragmentManager());

    }


    @Override
    public void onDialogNegativeClick(AlterDialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDialogPositiveClick(AlterDialogFragment dialog) {
        if (conversation == null)
            return;

        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().clearMessages(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {

                @Override
                public void onSuccess(Boolean aBoolean) {
                    ConversationDatabase.getDraftDao().queryBuilder().where(DraftDao.Properties.Type.eq(conversation.getConversationType().getValue()), DraftDao.Properties.Id.eq(conversation.getTargetId())).buildDelete().executeDeleteWithoutDetachingEntities();

                    RLog.d(this, "clearMessages", "-----onSuccess-------");
                    Toast.makeText(getActivity(), getString(R.string.rc_setting_clear_msg_success), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    Toast.makeText(getActivity(), getString(R.string.rc_setting_clear_msg_fail), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void toggleSwitch(boolean toggle) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
