package com.asktun.mg.fragment;

import java.io.File;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.asktun.mg.BaseFragment;
import com.asktun.mg.R;
import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.download.DownloadFile;
import com.asktun.mg.download.DownloadTask.OnDeleteTaskListener;
import com.asktun.mg.download.FinalDBChen;

public class UserDownloadFragment extends BaseFragment{
	
	@ViewInject(id = R.id.download_listview_lin)
	private LinearLayout listview_lin;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.fragment_download, container,false);
	}

	
	class DeleteTask implements OnDeleteTaskListener{

		
		private LinearLayout lin;
		/** 
		 * Title:
		 * Description:
		 */
		public DeleteTask(LinearLayout lin)
		{
			this.lin = lin;
		}
		/** (�� Javadoc) 
		* Title: onDelete
		* Description:
		* @param taskView
		* @param down
		* @see demo.mydownload.newdownload.DownloadTask.OnDeleteTaskListener#onDelete(android.view.View, demo.mydownload.DownloadMovieItem)
		*/ 
		@Override
		public void onDelete(final View taskView, final GameInfo down)
		{
			new AlertDialog.Builder(act)
			.setTitle("��ʾ")
			.setMessage("ɾ��"+down.getGame_name()+"?") //������ʾ
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			})
			.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					lin.removeView(taskView);
					//ֹͣ�������
					DownloadFile d = down.getDownloadFile();
					if(d != null) {
						d.stopDownload();
					}
					//ɾ�������ļ�
					File df = new File(down.getFilePath());
					System.out.println("file~~~~~~" + df.getPath() + " file exist() " + df.exists());
					if(df.exists()) {
						//����ļ�����
						df.delete();
					}
					
					//ɾ�����ݿ��е�����
					new FinalDBChen(act, DBNAME).deleteItem(TABNAME_DOWNLOADTASK, "game_name=?", new String[] {down.getGame_name()});
					//����һ��ɾ���ļ��Ĺ㲥.����ҳ�����ذ�ť��Ϊ������
					Intent i = new Intent();
					i.putExtra(DOWNLOAD_TYPE, DOWNLOAD_STATE_DELETE);
					i.setAction(DOWNLOAD_TYPE);
					act.getMyApplication().setDownloadSuccess(down);
					act.sendBroadcast(i);
				}
			}).show();
		}
		
	}
}
