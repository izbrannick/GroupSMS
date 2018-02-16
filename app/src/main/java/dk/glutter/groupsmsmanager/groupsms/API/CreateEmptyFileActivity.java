package dk.glutter.groupsmsmanager.groupsms.API;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by izbra on 16/02/2018.
 */

public class CreateEmptyFileActivity extends BasicDriveActivity {
    @Override
    protected void onDriveClientReady() {
        createEmptyFile();
    }

    private void createEmptyFile() {
        Log.e(TAG, "[START create_empty_file]");
        // [START create_empty_file]
        DriveResourceClient driveResourceClient = null;
        try {
            Log.e(TAG, "Getting DriveResourceClient ");
            driveResourceClient = getDriveResourceClient();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        Task<DriveFolder> rootFolder = null;
        try {
            Log.e(TAG, "Getting Drive rootFolder ");
            rootFolder = driveResourceClient.getRootFolder();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        Log.e(TAG, "rootFolder.continueWithTask... ");
        rootFolder.continueWithTask(new Continuation<DriveFolder, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<DriveFolder> task) throws Exception {
                        DriveFolder parentFolder = task.getResult();
                        Log.e(TAG, "new MetadataChangeSet.Builder()... ");
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("New file")
                                .setMimeType("text/plain")
                                .setStarred(true)
                                .build();
                        Log.e(TAG, "return getDriveResourceClient().createFile... ");
                        return getDriveResourceClient().createFile(parentFolder, changeSet, null);
                    }
                })
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveFile>() {
                            @Override
                            public void onSuccess(DriveFile driveFile) {
                                Log.e(TAG, "Finished create file");
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        finish();
                    }
                });
        // [END create_empty_file]
    }
}
