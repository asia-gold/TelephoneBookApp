package ru.asia.mytelephonebookapp.dialogs;

import ru.asia.mytelephonebookapp.R;
import ru.asia.mytelephonebookapp.tasks.DataContactsExportTask;
import ru.asia.mytelephonebookapp.tasks.DataContactsImportTask;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Build and show dialog for import and export data, call appropriate async task for chosen item.
 * 
 * @author Asia
 *
 */
public class ImportExportDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.title_import_export)
				.setItems(R.array.array_import_export, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							new DataContactsImportTask(getActivity()).execute();
							break;
						case 1:
							new DataContactsExportTask(getActivity()).execute();
							break;
						}						
					}
				});
		
		return builder.create();
	}	
}
