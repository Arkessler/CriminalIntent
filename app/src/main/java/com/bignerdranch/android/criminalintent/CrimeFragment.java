package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    public static final String PREFS_NAME = "MyPrefsFile";


    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;

    private Crime mCrime;
    private File mPhotoFile0, mPhotoFile1, mPhotoFile2, mPhotoFile3 = null;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView0, mPhotoView1, mPhotoView2, mPhotoView3;
    private View root;
    private FaceDetector detector;
    private Context context;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        // Initialize Face Detection
        context = this.getContext();
        detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        switch(mCrime.getNumberPicturesTaken()%4)
        {
            case 0:
                if (mCrime.getNumberPicturesTaken() == 0) {
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken());
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 1);
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 2);
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 3);
                }
                else {
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 0);
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 3);
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 2);
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 1);
                }
                break;
            case 1:
                if (mCrime.getNumberPicturesTaken() == 1) {
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken());
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 1);
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 2);
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 3);
                }
                else {
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 1);
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 0);
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 3);
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 2);
                }
                break;
            case 2:
                if (mCrime.getNumberPicturesTaken() == 2) {
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken());
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 1);
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 2);
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 3);
                }
                else {
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 2);
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 1);
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 0);
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 3);
                }
                break;
            case 3:
                if (mCrime.getNumberPicturesTaken() == 3) {
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken());
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 1);
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 2);
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, mCrime.getNumberPicturesTaken() - 3);
                }
                else {
                    mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 3);
                    mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 2);
                    mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 1);
                    mPhotoFile0 = CrimeLab.get(getActivity()).getPhotoFile(mCrime, 0);
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) root.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) root.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckbox = (CheckBox) root.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button)root.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)root.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) root.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile0 != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri;
            switch(mCrime.getNumberPicturesTaken()%4)
            {
                case 0:
                    uri = Uri.fromFile(mPhotoFile0);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    break;
                case 1:
                    uri = Uri.fromFile(mPhotoFile1);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    break;
                case 2:
                    uri = Uri.fromFile(mPhotoFile2);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    break;
                case 3:
                    uri = Uri.fromFile(mPhotoFile3);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    break;
            }
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        mPhotoView0 = (ImageView) root.findViewById(R.id.crime_photo);
        mPhotoView1 = (ImageView) root.findViewById(R.id.previous_image1);
        mPhotoView2 = (ImageView) root.findViewById(R.id.previous_image2);
        mPhotoView3 = (ImageView) root.findViewById(R.id.previous_image3);

        System.out.println("Calling update photo view from on create view");
        updatePhotoView();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME,
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();

                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            System.out.println("Calling updatePhotoView from returned photo");
            mCrime.setNumberPicturesTaken(mCrime.getNumberPicturesTaken() + 1);
            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void updatePhotoView() {
        System.out.println("UpdatePhotoView Called for mCrime:"+mCrime.getId());
        System.out.println("Updating photo views, number pictures taken:"+mCrime.getNumberPicturesTaken());
        System.out.println("Photo0 name:"+mPhotoFile0.getName());
        System.out.println("Photo1 name:"+mPhotoFile1.getName());
        System.out.println("Photo2 name:"+mPhotoFile2.getName());
        System.out.println("Photo3 name:"+mPhotoFile3.getName());
        System.out.println("Photo 0 exists:"+ mPhotoFile0.exists());
        System.out.println("Photo 1 exists:"+ mPhotoFile1.exists());
        System.out.println("Photo 2 exists:"+ mPhotoFile2.exists());
        System.out.println("Photo 3 exists:"+ mPhotoFile3.exists());
        if (mPhotoFile0 == null || !mPhotoFile0.exists()) {
            System.out.println("Set photo 0 to null");
            mPhotoView0.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile0.getPath(), getActivity());
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Face> faces = detector.detect(frame);
            TextView faceCountView = (TextView) root.findViewById(R.id.face_detection_report);
            faceCountView.setText(faces.size() + " faces detected");
            /*for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);
                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cx = (int) (landmark.getPosition().x * scale);
                    canvas.drawCircle(cs, cy, 10, paint);
                }
            }*/
            System.out.println("Changing photo view 0");
            mPhotoView0.setImageBitmap(bitmap);
        }
        if (mPhotoFile1 == null || !mPhotoFile1.exists()) {
            System.out.println("Set photo 1 to null");
            mPhotoView1.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile1.getPath(), getActivity());
            System.out.println("Changing photo view 1");
            mPhotoView1.setImageBitmap(bitmap);
        }
        if (mPhotoFile2 == null || !mPhotoFile2.exists()) {
            System.out.println("Set photo 2 to null");
            mPhotoView2.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile2.getPath(), getActivity());
            System.out.println("Changing photo view 2");
            mPhotoView2.setImageBitmap(bitmap);
        }
        if (mPhotoFile3 == null || !mPhotoFile3.exists()) {
            System.out.println("Set photo 3 to null");
            mPhotoView3.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile3.getPath(), getActivity());
            System.out.println("Changing photo view 3");
            mPhotoView3.setImageBitmap(bitmap);
        }
    }
}
